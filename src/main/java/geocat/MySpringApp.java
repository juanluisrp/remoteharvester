package geocat;

import org.apache.camel.spring.SpringCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;

@SpringBootApplication()
public class MySpringApp {

    @Autowired
    DataSource dataSource;


    public static void main(String[] args) throws Exception {

        Logger logger = LoggerFactory.getLogger(MySpringApp.class);
//        logger.debug("hi");
//        logger.debug("hi2");
        SpringApplication app = new SpringApplication(MySpringApp.class);
        ApplicationContext ctx = app.run(args);
//        SpringCamelContext camel = (SpringCamelContext) ctx.getBean(SpringCamelContext.class);
//        camel.start();
//        logger.debug("hi");
//        logger.debug("hi");
    }


}