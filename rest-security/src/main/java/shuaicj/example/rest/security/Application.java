package shuaicj.example.rest.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring boot app.
 *
 * @author shuaicj 2017/08/12
 */
@SpringBootApplication(scanBasePackages = {"shuaicj.example.rest.security", "shuaicj.example.rest.common"})
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }
}
