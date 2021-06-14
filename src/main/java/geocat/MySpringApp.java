package geocat;

import geocat.csw.csw.CSWGetCapHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@SpringBootApplication()
public class MySpringApp {

    @Autowired
    DataSource dataSource;



    public  static void main(String[] args) throws Exception {

        Logger logger = LoggerFactory.getLogger(MySpringApp.class);
//        logger.debug("hi");
//        logger.debug("hi2");
        SpringApplication app = new SpringApplication(MySpringApp.class);
        ApplicationContext ctx = app.run(args);
//        logger.debug("hi");
//        logger.debug("hi");
    }


}