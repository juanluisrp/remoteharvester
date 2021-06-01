package geocat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication()
public class MySpringApp {

    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(MySpringApp.class);
        ApplicationContext ctx = app.run(args);
    }


}