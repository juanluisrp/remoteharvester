package net.geocat.service;

import net.geocat.database.harvester.entities.BlobStorage;
import net.geocat.database.linkchecker.entities.LinkCheckBlobStorage;
import net.geocat.database.linkchecker.repos.LinkCheckBlobStorageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Optional;

@Component
@Scope("prototype")
public class LinkCheckBlobStorageService {

    @Autowired
    LinkCheckBlobStorageRepo linkCheckBlobStorageRepo;

    public String findXML(String sha2) {
        return linkCheckBlobStorageRepo.findById(sha2).get().getTextValue();
    }



    public void ensureBlobExists(String xmlStr, String sha2) {
        Optional<LinkCheckBlobStorage> item = linkCheckBlobStorageRepo.findById(sha2);
        if (item.isPresent())
            return;
        LinkCheckBlobStorage blob = new LinkCheckBlobStorage();
        blob.setSha2(sha2);
        blob.setTextValue(xmlStr);
        linkCheckBlobStorageRepo.save(blob); // very very slight chance this could throw, but so unlikely and it will fix itself when rerun by camel
    }
}
