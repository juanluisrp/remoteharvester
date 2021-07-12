package net.geocat.http;

import java.io.IOException;

public interface IHTTPRetriever {

    HttpResult retrieveXML(String verb, String location, String body, String cookie, IContinueReadingPredicate predicate)
            throws IOException, SecurityException, ExceptionWithCookies, RedirectException;

}
