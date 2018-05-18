package shuaicj.example.rest.i18n;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import shuaicj.example.rest.common.err.Err;

/**
 * Test method not allowed.
 *
 * @author shuaicj 2017/08/18
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MethodNotAllowedTest {

    @Autowired
    private TestRestTemplate rest;

    @Test
    public void test() {
        check(null, "method DELETE not allowed");
        check("zh-CN", "不支持DELETE");
        check("fr-FR", "method DELETE not allowed");
    }

    private void check(String locale, String message) {
        HttpHeaders headers = new HttpHeaders();
        if (locale != null) {
            headers.set(HttpHeaders.ACCEPT_LANGUAGE, locale);
        }
        ResponseEntity<Err> e = rest.exchange("/my-country", HttpMethod.DELETE,
                new HttpEntity<>(headers), Err.class);
        assertThat(e.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
        assertThat(e.getBody().getException()).isEqualTo(HttpRequestMethodNotSupportedException.class.getName());
        assertThat(e.getBody().getTimestamp()).isBeforeOrEqualsTo(new Date());
        assertThat(e.getBody().getMessage()).isEqualTo(message);
    }
}