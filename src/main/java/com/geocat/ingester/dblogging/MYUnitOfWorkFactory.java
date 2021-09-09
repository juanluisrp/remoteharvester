package com.geocat.ingester.dblogging;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.engine.DefaultUnitOfWork;
import org.apache.camel.spi.InflightRepository;
import org.apache.camel.spi.UnitOfWork;
import org.apache.camel.spi.UnitOfWorkFactory;
import org.slf4j.Logger;

//taken from DefaultUnitOfWorkFactory
public class MYUnitOfWorkFactory implements UnitOfWorkFactory {

    private InflightRepository inflightRepository;
    private boolean usedMDCLogging;
    private String mdcLoggingKeysPattern;
    private boolean allowUseOriginalMessage;
    private boolean useBreadcrumb;

    @Override
    public void warmup(Logger log) {

    }

    @Override
    public UnitOfWork createUnitOfWork(Exchange exchange) {
        UnitOfWork answer;
        if (usedMDCLogging) {
            answer = new MyUnitOfWork(
                    exchange, inflightRepository, mdcLoggingKeysPattern, allowUseOriginalMessage, useBreadcrumb);
        } else {
            answer = new DefaultUnitOfWork(exchange, inflightRepository, allowUseOriginalMessage, useBreadcrumb);
        }
        return answer;
    }

    @Override
    public void afterPropertiesConfigured(CamelContext camelContext) {
        // optimize to read configuration once
        inflightRepository = camelContext.getInflightRepository();
        usedMDCLogging = camelContext.isUseMDCLogging() != null && camelContext.isUseMDCLogging();
        mdcLoggingKeysPattern = camelContext.getMDCLoggingKeysPattern();
        allowUseOriginalMessage
                = camelContext.isAllowUseOriginalMessage() != null ? camelContext.isAllowUseOriginalMessage() : false;
        useBreadcrumb = camelContext.isUseBreadcrumb() != null ? camelContext.isUseBreadcrumb() : false;
    }

}
