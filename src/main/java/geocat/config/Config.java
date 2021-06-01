package geocat.config;

import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.activemq.ActiveMQComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.jms.ConnectionFactory;


@Configuration
public class Config {


    String brokerUrl = "tcp://localhost:61616";

    int maxConnections = 11;

    @Autowired
    CamelContext camelContext;

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL(brokerUrl);
        org.apache.activemq.RedeliveryPolicy redeliveryPolicy = factory.getRedeliveryPolicy();
        redeliveryPolicy.setMaximumRedeliveries(3);
        redeliveryPolicy.setRedeliveryDelay(3000);
        redeliveryPolicy.setInitialRedeliveryDelay(3000);
        redeliveryPolicy.setMaximumRedeliveryDelay(5000);
        return factory;
    }

    //    @Bean
//    public JmsTransactionManager jmsTransactionManager(ActiveMQConnectionFactory connectionFactory) {
//        JmsTransactionManager jmsTransactionManager = new JmsTransactionManager();
//        jmsTransactionManager.setConnectionFactory(connectionFactory);
//        return jmsTransactionManager;
//    }
    @Bean
    //@Primary
    public ActiveMQComponent activemq(ConnectionFactory connectionFactory) {
        JmsTransactionManager jmsTransactionManager = new JmsTransactionManager();
        jmsTransactionManager.setConnectionFactory(connectionFactory);

        ActiveMQComponent activeMQComponent = new ActiveMQComponent();
        activeMQComponent.setConnectionFactory(connectionFactory);
        activeMQComponent.setTransacted(true);
        activeMQComponent.setTransactedInOut(true);
        activeMQComponent.setTransactionManager(jmsTransactionManager);
        //  activeMQComponent.setLazyCreateTransactionManager (false);

        activeMQComponent.setCacheLevelName("CACHE_CONSUMER");
        // activeMQComponent.setAcknowledgementModeName("SESSION_TRANSACTED");


        return activeMQComponent;
    }

//    @Bean
//    public CamelContext camelContext(){
//        CamelContext ctx = new DefaultCamelContext();
//       // ctx.setMessageHistory(true);
//        return ctx;
//    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager dbTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();

        return transactionManager;
    }

}
