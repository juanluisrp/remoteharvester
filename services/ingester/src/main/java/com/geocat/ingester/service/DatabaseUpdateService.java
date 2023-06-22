package com.geocat.ingester.service;

import com.geocat.ingester.dao.ingester.IngestJobRepo;
import com.geocat.ingester.model.ingester.IngestJob;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;

@Component
@Scope("prototype")
public class DatabaseUpdateService {

    @Autowired
    private IngestJobRepo ingestJobRepo;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    //synchronized so other threads cannot update while we are writing...
    public synchronized void errorOccurred(Exchange exchange) {
        Exception e = (Exception) exchange.getMessage().getHeader("exception");
        if (e == null)
            return;
        String processId = (String) exchange.getMessage().getHeader("processID");
        IngestJob job = ingestJobRepo.findById(processId).get();
        if (job.getMessages() == null)
            job.setMessages("");
        String thisMessage = "\n--------------------------------------\n";
        thisMessage += "WHEN:" + Instant.now().toString() + "\n\n";
        thisMessage += convertToString(e);
        thisMessage += "\n--------------------------------------\n";
        job.setMessages(job.getMessages() + thisMessage);
        IngestJob j2 = ingestJobRepo.save(job);
    }


    public String convertToString(Throwable e) {
        String result = e.getClass().getCanonicalName() + " - " + e.getMessage();

        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String stackTraceStr = sw.toString();

        result += stackTraceStr;
        if (e.getCause() != null)
            return result + convertToString(e.getCause());
        return result;
    }

}
