package net.geocat.service.downloadhelpers;

import net.geocat.database.linkchecker.entities.helper.PartialDownloadHint;
import net.geocat.database.linkchecker.entities.helper.RetrievableSimpleLink;
import net.geocat.http.IContinueReadingPredicate;
import net.geocat.xml.helpers.CapabilityDeterminer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class PartialDownloadPredicateFactory {

    public IContinueReadingPredicate create (RetrievableSimpleLink link) throws Exception {
        return create(link.getPartialDownloadHint());
    }

    public IContinueReadingPredicate create(PartialDownloadHint hint) throws Exception {
        if ( (hint == null) || (hint== PartialDownloadHint.ALWAYS_DOWNLOAD))
            return null; // null is interpreted as always download

        switch (hint){
            case XML_ONLY:
                return new XmlContinueReadingPredicate();
            case METADATA_ONLY:
                return new MetadataContinueReadingPredicate();
            case CAPABILITIES_ONLY:
                return new CapabilitiesContinueReadingPredicate(new CapabilityDeterminer());


        }
        throw new Exception("PartialDownloadPredicateFactory - unknown option - "+hint);
    }
}
