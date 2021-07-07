package net.geocat.service;

import net.geocat.database.harvester.repos.BlobStorageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class BlobStorageService {

    @Autowired
    BlobStorageRepo blogStorageRepo;

    public String findXML(String sha2) {
        return blogStorageRepo.findById(sha2).get().getTextValue();
    }
}
