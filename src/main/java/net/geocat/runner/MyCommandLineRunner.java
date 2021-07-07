package net.geocat.runner;

import net.geocat.database.linkchecker.entities.Link;
import net.geocat.database.linkchecker.repos.LinkRepo;
import net.geocat.http.IHTTPRetriever;
import net.geocat.service.BlobStorageService;
import net.geocat.xml.XmlDoc;
import net.geocat.xml.XmlDocumentFactory;
import net.geocat.xml.XmlServiceRecordDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.w3c.dom.NodeList;

import java.util.Optional;

@Component
public class MyCommandLineRunner implements CommandLineRunner {
    private static final Logger logger =    LoggerFactory.getLogger(MyCommandLineRunner.class);


    @Autowired
    BlobStorageService blobStorageService;

    @Autowired
    XmlDocumentFactory xmlDocumentFactory;

    @Autowired
    @Qualifier("cookieAttachingRetriever")
    IHTTPRetriever retriever;

    @Autowired
    LinkRepo linkRepo;

    @Override
    public void run(String...args) throws Exception {

        //Optional<Link> l = linkRepo.findById(1L);

        String sha2_wms = "65B3FFA90B5B277B34C7206D0283B7CD1FE89AE473998A566239992C4A59A417";
        String sha2_wfs = "8759C3E02840BC5DE3F81B23F4AF1D124821384252AC018A0BDEB0386B76E663";
        String sha2_atom = "546108E2F8D91EEE3E0684736F1469953EF8F71A73A63849E19B84D027704857";
        String sha2_wmts = "49D36EFBA4B7A2541D31E44FC9557A8BEB0A7E275F1F22AAA00EEE953C41FF70";

        String sha2 = sha2_atom;

        String xml = blobStorageService.findXML(sha2);
       // String xml = blobStorageService.findXML("7F3F9891B56B1E0A31B0122B25A82D2C83F95553BF08D71889E88C40B767DBA4");

        XmlDoc xmlDoc = xmlDocumentFactory.create(xml);

        if (xmlDoc instanceof XmlServiceRecordDoc)
        {
            XmlServiceRecordDoc xmlServiceRecord = (XmlServiceRecordDoc) xmlDoc;
            String capURL = xmlServiceRecord.getConnectPoints().get(0).getRawURL();
            String capXML = new String(retriever.retrieveXML("GET",capURL,null,null,null));
            XmlDoc capXMLDoc = xmlDocumentFactory.create(capXML);
            int t=0;
        }

    }
}

