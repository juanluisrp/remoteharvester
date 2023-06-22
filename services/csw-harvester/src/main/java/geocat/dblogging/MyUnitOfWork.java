package geocat.dblogging;

import org.apache.camel.Exchange;
import org.apache.camel.impl.engine.MDCUnitOfWork;
import org.apache.camel.spi.InflightRepository;
import org.slf4j.MDC;


public class MyUnitOfWork extends MDCUnitOfWork {
    public MyUnitOfWork(Exchange exchange, InflightRepository inflightRepository,
                        String pattern, boolean allowUseOriginalMessage, boolean useBreadcrumb) {

        super(exchange, inflightRepository, pattern, allowUseOriginalMessage, useBreadcrumb);

        MDC.clear();
        String correlationId = (String) exchange.getMessage().getHeader("JMSCorrelationID");
        if ((correlationId != null) && (!correlationId.isEmpty()))
            MDC.put("JMSCorrelationID", correlationId);
    }
}