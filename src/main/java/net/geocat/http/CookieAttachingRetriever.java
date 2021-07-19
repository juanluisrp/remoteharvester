package net.geocat.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Scope("prototype")
@Qualifier("cookieAttachingRetriever")
public class CookieAttachingRetriever implements IHTTPRetriever {

    @Autowired
    @Qualifier("redirectAwareHTTPRetriever")
    public RedirectAwareHTTPRetriever retriever; // public for testing
    Logger logger = LoggerFactory.getLogger(CookieAttachingRetriever.class);

    public CookieAttachingRetriever() {

    }

    @Override
    public HttpResult retrieveXML(String verb, String location, String body, String cookie, IContinueReadingPredicate predicate) throws IOException, SecurityException, ExceptionWithCookies, RedirectException {

            HttpResult result= retriever.retrieveXML(verb, location, body, cookie, predicate);
            if (result.isErrorOccurred()) {
                return retriever.retrieveXML(verb, location, body, result.getSpecialToSendCookie(), predicate);
            }
            return result;
    }
}
