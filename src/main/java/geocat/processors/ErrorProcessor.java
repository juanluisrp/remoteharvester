package geocat.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ErrorProcessor implements Processor {

    int maxCamelInternalRedirects;

    public ErrorProcessor(int maxCamelInternalRedirects) {
        this.maxCamelInternalRedirects = maxCamelInternalRedirects;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Exception ex = exchange.getException();
        exchange.getMessage().setHeader("exception",ex.getMessage());
        exchange.getMessage().setHeader("sendingBackToJMS",willBeRedirectedBackToJMS(exchange));
    }

    //${header.CamelRedeliveryCounter} > ${header.CamelRedeliveryMaxCounter}"
    public boolean willBeRedirectedBackToJMS(Exchange exchange){
        int camelRedeliveryCounter =  (Integer)exchange.getMessage().getHeaders().get("CamelRedeliveryCounter") ;
        return camelRedeliveryCounter > maxCamelInternalRedirects;
    }
}
