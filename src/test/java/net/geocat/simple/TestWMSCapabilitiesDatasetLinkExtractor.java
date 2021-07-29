package net.geocat.simple;

import net.geocat.service.capabilities.DatasetLink;
import net.geocat.service.capabilities.WMSCapabilitiesDatasetLinkExtractor;
import net.geocat.xml.XmlCapabilitiesWMS;
import net.geocat.xml.XmlDocumentFactory;
import net.geocat.xml.helpers.CapabilityDeterminer;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class TestWMSCapabilitiesDatasetLinkExtractor {

    XmlDocumentFactory xmlDocumentFactory;
    WMSCapabilitiesDatasetLinkExtractor wmsCapabilitiesDatasetLinkExtractor;

    @Before
    public void setup(){
        xmlDocumentFactory = new XmlDocumentFactory();
        xmlDocumentFactory.capabilityDeterminer = new CapabilityDeterminer();

        wmsCapabilitiesDatasetLinkExtractor =  new WMSCapabilitiesDatasetLinkExtractor();
    }


    @Test
    public void info_fully_in_single_nested_layer() throws Exception {
        XmlCapabilitiesWMS xmlCapabilitiesDocument = read("wms_cap_full_nested.xml");
        List<DatasetLink> links = wmsCapabilitiesDatasetLinkExtractor.findLinks(xmlCapabilitiesDocument);
        assertEquals(1,links.size());
        int t=0;
    }

    @Test
    public void info_fully_in_single_nested_layer_parent() throws Exception {
        XmlCapabilitiesWMS xmlCapabilitiesDocument = read("wms_cap_full_nested_in_parent.xml");
        List<DatasetLink> links = wmsCapabilitiesDatasetLinkExtractor.findLinks(xmlCapabilitiesDocument);

        assertEquals(1,links.size()); // 1 because the will both be the same
        int t=0;
    }

    public XmlCapabilitiesWMS read(String fname) throws Exception {
        String text = new Scanner(TestWMSCapabilitiesDatasetLinkExtractor.class.getClassLoader().getResourceAsStream(fname), "UTF-8")
                .useDelimiter("\\A").next();
        return (XmlCapabilitiesWMS)xmlDocumentFactory.create(text);
    }
}
