package geocat.database.service;

import geocat.database.entities.BlobStorage;
import geocat.database.repos.BlogStorageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Optional;

@Component
@Scope("prototype")
public class BlobStorageService {

    @Autowired
    BlogStorageRepo blogStorageRepo;

    public void ensureBlobExists(String xmlStr, String sha2) {
        Optional<BlobStorage> item = blogStorageRepo.findById(sha2);
        if (item.isPresent())
            return;
        BlobStorage blob = new BlobStorage();
        blob.setSha2(sha2);
        blob.setTextValue(xmlStr);
        blogStorageRepo.save(blob); // very very slight chance this could throw, but so unlikely and it will fix itself when rerun by camel
    }

    public String computeSHA2(String xml) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(xml.getBytes(StandardCharsets.UTF_8));
        String hexHash = javax.xml.bind.DatatypeConverter.printHexBinary(hash);
        return hexHash;
    }

}
