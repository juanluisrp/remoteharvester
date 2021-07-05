package net.geocat.runner;

import net.geocat.service.BlobStorageService;
import net.geocat.xml.XmlDoc;
import net.geocat.xml.XmlDocumentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.w3c.dom.NodeList;

@Component
public class MyCommandLineRunner implements CommandLineRunner {
    private static final Logger logger =    LoggerFactory.getLogger(MyCommandLineRunner.class);


    @Autowired
    BlobStorageService blobStorageService;

    @Autowired
    XmlDocumentFactory xmlDocumentFactory;

    @Override
    public void run(String...args) throws Exception {
        String xml = blobStorageService.findXML("D32CAF828150514F0986BADA0C6A5E250BCFE7287966D0CD39325BBDDB8E76AD");
       // String xml = blobStorageService.findXML("7F3F9891B56B1E0A31B0122B25A82D2C83F95553BF08D71889E88C40B767DBA4");

        XmlDoc xmlDoc = xmlDocumentFactory.create(xml);
        NodeList nl = xmlDoc.xpath_nodeset("//srv:containsOperations[srv:SV_OperationMetadata/srv:operationName/gco:CharacterString = 'GetCapabilities']") ;
    }
}

