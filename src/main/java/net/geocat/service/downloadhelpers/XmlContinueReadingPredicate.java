package net.geocat.service.downloadhelpers;

import net.geocat.http.IContinueReadingPredicate;

import static net.geocat.service.downloadhelpers.CapabilitiesContinueReadingPredicate.isXML;

public class XmlContinueReadingPredicate  implements IContinueReadingPredicate {

    @Override
    public boolean continueReading(byte[] head) {
        try {
            String doc = new String(head).trim();
            if (!isXML(doc))
                return false; //not XML
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
