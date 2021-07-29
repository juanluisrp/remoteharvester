package net.geocat.xml;

import net.geocat.xml.XmlCapabilitiesDocument;
import net.geocat.xml.XmlDoc;
import net.geocat.xml.helpers.CapabilitiesType;
import org.w3c.dom.Node;

import javax.xml.xpath.XPathExpressionException;

public class XmlCapabilitiesWFS extends XmlCapabilitiesDocument {

    private String SpatialDataSetIdentifier_metadataURL;
    private String SpatialDataSetIdentifier_Code;

    public XmlCapabilitiesWFS(XmlDoc doc ) throws Exception {
        super(doc, CapabilitiesType.WFS);
        setup_XmlCapabilitiesWFS();
    }

    private void setup_XmlCapabilitiesWFS() throws  Exception {
            Node sdi= xpath_node("//*[local-name()='ExtendedCapabilities']/inspire_dls:SpatialDataSetIdentifier");
            if (sdi != null)
            {
                SpatialDataSetIdentifier_metadataURL =  this.xpath_attribute(sdi,".","metadataURL");
                Node code = xpath_node(sdi,"./inspire_common:Code");
                if (code != null)
                    SpatialDataSetIdentifier_Code = code.getTextContent();
            }
    }


}