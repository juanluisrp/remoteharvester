package net.geocat.xml.helpers;

import net.geocat.xml.XmlDoc;
import org.springframework.stereotype.Component;

@Component
public class CapabilityDeterminer {

    public CapabilitiesType determineCapabilitiesType(XmlDoc doc) throws Exception {

        String tag = doc.getRootTagName();
        String ns = doc.getRootNS();
        return determineType(ns.toLowerCase(),tag);
    }

    private CapabilitiesType determineType(String ns, String rootTagName) throws Exception {
        if (rootTagName.equals("WMS_Capabilities") && (ns.equals("http://www.opengis.net/wms")) )
            return CapabilitiesType.WMS;
        if (rootTagName.equals("Capabilities") && (ns.equals("http://www.opengis.net/wmts/1.0")) )
            return CapabilitiesType.WMTS;
        if (rootTagName.equals("WFS_Capabilities") && (ns.equals("http://www.opengis.net/wfs/2.0")) )
            return CapabilitiesType.WFS;
//        if (rootTagName.equals("Capabilities")&& (ns.equals("http://www.opengis.net/wcs/2.0")) )
//            return CapabilitiesType.WCS;
//        if (rootTagName.equals("Capabilities")&& (ns.equals("http://www.opengis.net/sos/2.0")) )
//            return CapabilitiesType.SOS;
        if (rootTagName.equals("feed")&& (ns.equals("http://www.w3.org/2005/atom")) )
            return CapabilitiesType.Atom;
        if (rootTagName.equals("Capabilities")&& (ns.equals("http://www.opengis.net/cat/csw/2.0.2")) )
            return CapabilitiesType.CSW;

        throw new Exception("not a known capabilities doc");
    }

}
