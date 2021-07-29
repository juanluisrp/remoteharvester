package net.geocat.service.downloadhelpers;

import net.geocat.database.linkchecker.entities.helper.RetrievableSimpleLink;
import net.geocat.database.linkchecker.entities2.IndicatorStatus;
import net.geocat.http.HttpResult;
import net.geocat.http.IContinueReadingPredicate;
import net.geocat.http.IHTTPRetriever;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Component
@Scope("prototype")
public class RetrievableSimpleLinkDownloader {

    private static final Logger logger =    LoggerFactory.getLogger(RetrievableSimpleLinkDownloader.class);


    @Autowired
    @Qualifier("cookieAttachingRetriever")
    IHTTPRetriever retriever;

    @Autowired
    PartialDownloadPredicateFactory partialDownloadPredicateFactory;




    public RetrievableSimpleLink process (RetrievableSimpleLink link)  {
        try {

            HttpResult data = null;

            String url = (link.getFixedURL() == null) ? link.getRawURL() : link.getFixedURL();

            IContinueReadingPredicate continueReadingPredicate = partialDownloadPredicateFactory.create(link);

            try {
                data = retriever.retrieveXML("GET", url, null, null, continueReadingPredicate);
            } catch (Exception e) {
                link.setIndicator_LinkResolves(IndicatorStatus.FAIL);
                link.setLinkHTTPException(e.getClass().getSimpleName() + " - " + e.getMessage());
                return link;
            }
            if ((data.getHttpCode() == 200))
                link.setIndicator_LinkResolves(IndicatorStatus.PASS);
            else
                link.setIndicator_LinkResolves(IndicatorStatus.FAIL);

            link.setLinkHTTPStatusCode(data.getHttpCode());
            link.setLinkMIMEType(data.getContentType());
            link.setFinalURL(data.getFinalURL());
            link.setLinkIsHTTS(data.isHTTPS());
            if (data.isHTTPS()) {
                link.setLinkSSLTrustedByJava(data.isSslTrusted());
                link.setLinkSSLUntrustedByJavaReason(data.getSslUnTrustedReason());
            }

            byte[] headData = Arrays.copyOf(data.getData(), Math.min(1000, data.getData().length));
            link.setLinkContentHead(headData);

            link.setLinkIsXML(isXML(data));

            link.setUrlFullyRead(data.isFullyRead());
            if (data.isFullyRead()) {
                link.setFullData(data.getData());
            }

            return link;
        }
        catch (Exception e){
            logger.error("RetrievableSimpleLinkDownloader - error occurred processing link", e);
            return link;
        }
    }

    public boolean isXML(HttpResult result){
        try {
            return CapabilitiesContinueReadingPredicate.isXML(new String(result.getData()));
        }
        catch (Exception e){
            return false;
        }
    }

}
