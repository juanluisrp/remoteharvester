package geocat.csw.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Scope("prototype")
@Qualifier("redirectAwareHTTPRetriever")
public class RedirectAwareHTTPRetriever implements IHTTPRetriever {

    public static int MAXREDIRECTS = 5;
    @Autowired
    @Qualifier("basicHTTPRetriever")
    public BasicHTTPRetriever retriever; // public for testing
    Logger logger = LoggerFactory.getLogger(RedirectAwareHTTPRetriever.class);

    public RedirectAwareHTTPRetriever() {

    }


    @Override
    public String retrieveXML(String verb, String location, String body, String cookie)
            throws IOException, SecurityException, ExceptionWithCookies, RedirectException {
        return _retrieveXML(verb, location, body, cookie, MAXREDIRECTS);
    }


    protected String _retrieveXML(String verb, String location, String body, String cookie, int nRedirectsRemaining)
            throws IOException, SecurityException, ExceptionWithCookies, RedirectException {
        try {
            return retriever.retrieveXML(verb, location, body, cookie);
        } catch (RedirectException re) {
            if (nRedirectsRemaining <= 0)
                throw new IOException("too many redirects!");
            logger.debug("     REDIRECTED TO location=" + re.getNewLocation());
            return _retrieveXML(verb, re.getNewLocation(), body, cookie, nRedirectsRemaining--);
        }
    }
}
