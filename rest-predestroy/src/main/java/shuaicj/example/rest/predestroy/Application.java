package shuaicj.example.rest.predestroy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring boot app.
 *
 * @author shuaicj 2017/08/12
 */
@SpringBootApplication(scanBasePackages = {"shuaicj.example.rest.predestroy", "shuaicj.example.rest.common"})
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }
}
