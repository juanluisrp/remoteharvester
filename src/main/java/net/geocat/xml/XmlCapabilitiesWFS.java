package net.geocat.xml;

import net.geocat.xml.XmlCapabilitiesDocument;
import net.geocat.xml.XmlDoc;
import net.geocat.xml.helpers.CapabilitiesType;

public class XmlCapabilitiesWFS extends XmlCapabilitiesDocument {

    public XmlCapabilitiesWFS(XmlDoc doc ) throws Exception {
        super(doc, CapabilitiesType.WFS);
    }
}