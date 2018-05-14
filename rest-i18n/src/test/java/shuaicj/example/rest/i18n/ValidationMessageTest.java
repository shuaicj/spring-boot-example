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
import org.springframework.web.bind.MethodArgumentNotValidException;
import shuaicj.example.rest.common.err.Err;
import shuaicj.example.rest.common.err.NotFoundException;

/**
 * Test error messages of validation.
 *
 * @author shuaicj 2017/08/14
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ValidationMessageTest {

    @Autowired
    private TestRestTemplate rest;

    @Test
    public void ok() {
        ResponseEntity<String> e = rest.postForEntity("/countries", new Country("en-US", "abc"), String.class);
        assertThat(e.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(e.getBody()).isEqualTo("ok");
    }

    @Test
    public void notFound() {
        Country country = new Country("yyy", "abc");
        HttpStatus status = HttpStatus.NOT_FOUND;
        String exception = NotFoundException.class.getName();
        check(country, status, exception, null, "country code yyy not found");
        check(country, status, exception, "zh-CN", "找不到国家代号yyy");
        check(country, status, exception, "fr-FR", "country code yyy not found");
    }

    @Test
    public void unexpected() {
        Country country = new Country("xxx", "abc");
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String exception = RuntimeException.class.getName();
        check(country, status, exception, null, "unexpected exception");
        check(country, status, exception, "zh-CN", "意外错误");
        check(country, status, exception, "fr-FR", "unexpected exception");
    }

    @Test
    public void notNull() {
        Country country = new Country(null, "abc");
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String exception = MethodArgumentNotValidException.class.getName();
        check(country, status, exception, null, "country code is required");
        check(country, status, exception, "zh-CN", "请指定国家代号");
        check(country, status, exception, "fr-FR", "country code is required");
    }

    @Test
    public void badSize() {
        Country country = new Country("zhzhzh", "abc");
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String exception = MethodArgumentNotValidException.class.getName();
        check(country, status, exception, null, "the length of country code is between 2 and 5");
        check(country, status, exception, "zh-CN", "国家代号长度应在2到5之间");
        check(country, status, exception, "fr-FR", "the length of country code is between 2 and 5");
    }

    @Test
    public void notNullAndBadSize() {
        Country country = new Country("zhzhzh", null);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String exception = MethodArgumentNotValidException.class.getName();
        check(country, status, exception, null, "the length of country code is between 2 and 5", "country name is required");
        check(country, status, exception, "zh-CN", "国家代号长度应在2到5之间", "请指定国家名称");
        check(country, status, exception, "fr-FR", "the length of country code is between 2 and 5", "country name is required");
    }

    private void check(Country country, HttpStatus status, String exception, String locale, String... messages) {
        ResponseEntity<Err> e;
        if (locale == null) {
            e = rest.postForEntity("/countries", country, Err.class);
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.ACCEPT_LANGUAGE, locale);
            e = rest.exchange("/countries", HttpMethod.POST, new HttpEntity<>(country, headers), Err.class);
        }
        assertThat(e.getStatusCode()).isEqualTo(status);
        assertThat(e.getBody().getException()).isEqualTo(exception);
        assertThat(e.getBody().getTimestamp()).isBeforeOrEqualsTo(new Date());
        if (messages.length == 1) {
            assertThat(e.getBody().getMessage()).isEqualTo(messages[0]);
        } else {
            for (String m : messages) {
                assertThat(e.getBody().getMessage()).contains(m);
            }
        }
    }
}