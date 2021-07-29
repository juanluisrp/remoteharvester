package net.geocat.service.downloadhelpers;

import net.geocat.http.IContinueReadingPredicate;

import static net.geocat.service.downloadhelpers.CapabilitiesContinueReadingPredicate.*;

public class MetadataContinueReadingPredicate implements IContinueReadingPredicate {
    @Override
    public boolean continueReading(byte[] head) {
        try {
            String doc = new String(head).trim();
            if (!isXML(doc))
                return false; //not XML

            doc = replaceXMLDecl(doc).trim();
            doc = getRootTag(doc).trim();

            String prefix = getPrefix(doc);
            String tag = getTagName(doc);
            String ns = getNS( prefix, doc);

            return (tag.equals("MD_Metadata") || tag.equals("GetRecordsResponse") || tag.equals("GetRecordByIdResponse"));


        } catch (Exception e) {
            return false;
        }
    }
}
