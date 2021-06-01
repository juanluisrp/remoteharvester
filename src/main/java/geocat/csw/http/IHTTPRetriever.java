package geocat.csw.http;

import java.io.IOException;

public interface IHTTPRetriever {

    String retrieveXML(String verb, String location, String body, String cookie)
            throws IOException, SecurityException, ExceptionWithCookies, RedirectException;

}
