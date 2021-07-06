package net.geocat.xml;

import net.geocat.xml.XmlCapabilitiesDocument;
import net.geocat.xml.XmlDoc;
import net.geocat.xml.helpers.CapabilitiesType;

public class XmlCapabilitiesCSW extends XmlCapabilitiesDocument {

    public XmlCapabilitiesCSW(XmlDoc doc ) throws Exception {
        super(doc, CapabilitiesType.CSW);
    }
}