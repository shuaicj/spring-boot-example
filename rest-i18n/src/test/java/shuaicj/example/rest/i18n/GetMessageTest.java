package shuaicj.example.rest.i18n;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Get localed messages.
 *
 * @author shuaicj 2017/08/14
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetMessageTest {

    @Autowired
    private TestRestTemplate rest;

    @Test
    public void byDefault() {
        assertThat(restGet(null)).isEqualTo("China");
    }

    @Test
    public void specified() {
        assertThat(restGet("zh-CN")).isEqualTo("中国");
    }

    @Test
    public void notExists() {
        assertThat(restGet("fr-FR")).isEqualTo("China");
    }

    private String restGet(String locale) {
        if (locale == null) {
            return rest.getForObject("/my-country", String.class);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT_LANGUAGE, locale);
        return rest.exchange("/my-country", HttpMethod.GET, new HttpEntity<>(headers), String.class).getBody();
    }
}