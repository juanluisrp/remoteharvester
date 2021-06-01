package geocat.csw.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import sun.misc.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Scope("prototype")
@Qualifier("basicHTTPRetriever")
public class BasicHTTPRetriever implements IHTTPRetriever {

    Logger logger = LoggerFactory.getLogger(BasicHTTPRetriever.class);

    /**
     * @param verb     GET or POST
     * @param location url
     * @param body     for POST, body
     * @param cookie   cookies to attach -   http.setRequestProperty("Cookie", cookie);
     * @return response from server
     * @throws Exception
     */
    public String retrieveXML(String verb, String location, String body, String cookie) throws IOException, SecurityException, ExceptionWithCookies, RedirectException {

        if (body == null)
            body = "";
        URL url = new URL(location);
        if (!url.getProtocol().equalsIgnoreCase("http") && (!url.getProtocol().equalsIgnoreCase("https")))
            throw new SecurityException("Security violation - url should be HTTP or HTTPS");

        if (!verb.equals("POST") && !verb.equals("GET"))
            throw new SecurityException("verb should be 'POST' or 'GET'");

        logger.debug("      * " + verb + " to " + location + " with body " + body.replace("\n", ""));

        byte[] body_bytes = body.getBytes(StandardCharsets.UTF_8);
        byte[] response_bytes;

        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection) con;
        http.setRequestMethod(verb);
        http.setDoOutput(true);
        http.setDoInput(true);
        if (verb.equals("POST")) {
            http.setFixedLengthStreamingMode(body_bytes.length);
            http.setRequestProperty("Content-Type", "application/xml");
        }
        if ((cookie != null) && (!cookie.isEmpty()))
            http.setRequestProperty("Cookie", cookie);

        String response;
        http.connect();
        try {
            // send body
            if (body_bytes.length > 0) {
                try (OutputStream os = http.getOutputStream()) {
                    os.write(body_bytes);
                }
            }
            // get response
            try (InputStream is = http.getInputStream()) {
                response_bytes = IOUtils.readAllBytes(is);
                response = new String(response_bytes, StandardCharsets.UTF_8);
            }
        } catch (IOException ioException) {
            List<String> cookies = http.getHeaderFields().get("Set-Cookie");
            if ((cookies == null) || (cookies.isEmpty()))
                throw ioException;
            throw new ExceptionWithCookies(ioException.getMessage(), cookies.get(0), ioException);
        } finally {
            http.disconnect();
        }

        if (http.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM || http.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
            String newUrl = http.getHeaderField("Location");
            throw new RedirectException("redirect requested", newUrl);
        }
        return response;
    }

}
