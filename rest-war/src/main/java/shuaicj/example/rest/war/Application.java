package shuaicj.example.rest.war;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Spring boot app.
 *
 * @author shuaicj 2017/08/12
 */
@SpringBootApplication(scanBasePackages = {"shuaicj.example.rest.war", "shuaicj.example.rest.common"})
public class Application extends SpringBootServletInitializer {

    // for spring boot packaging: war
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }
}
