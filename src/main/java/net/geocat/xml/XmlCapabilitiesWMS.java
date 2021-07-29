package net.geocat.xml;

import net.geocat.service.capabilities.DatasetLink;
import net.geocat.service.capabilities.WMSCapabilitiesDatasetLinkExtractor;
import net.geocat.xml.XmlCapabilitiesDocument;
import net.geocat.xml.XmlDoc;
import net.geocat.xml.helpers.CapabilitiesType;

import java.util.List;

public class XmlCapabilitiesWMS extends XmlCapabilitiesDocument {

    static WMSCapabilitiesDatasetLinkExtractor wmsCapabilitiesDatasetLinkExtractor = new WMSCapabilitiesDatasetLinkExtractor();



    public XmlCapabilitiesWMS(XmlDoc doc ) throws Exception {
        super(doc, CapabilitiesType.WMS);
        setup_XmlCapabilitiesWMS();
    }

    private void setup_XmlCapabilitiesWMS() throws Exception {
        datasetLinksList = wmsCapabilitiesDatasetLinkExtractor.findLinks(this);
    }

    public List<DatasetLink> getDatasetLinksList() {
        return datasetLinksList;
    }

    public void setDatasetLinksList(List<DatasetLink> datasetLinksList) {
        this.datasetLinksList = datasetLinksList;
    }
}
