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
        ResponseEntity<String> e = rest.postForEntity("/countries",
                new Country("id", "name", "capital"), String.class);
        assertThat(e.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(e.getBody()).isEqualTo("ok");
    }

    @Test
    public void notFound() {
        Country country = new Country("yyy", "name", "capital");
        HttpStatus status = HttpStatus.NOT_FOUND;
        String exception = NotFoundException.class.getName();
        check(country, status, exception, null, "yyy not found");
        check(country, status, exception, "zh-CN", "找不到yyy");
        check(country, status, exception, "fr-FR", "yyy not found");
    }

    @Test
    public void unexpected() {
        Country country = new Country("xxx", "name", "capital");
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String exception = RuntimeException.class.getName();
        check(country, status, exception, null, "unexpected exception");
        check(country, status, exception, "zh-CN", "意外错误");
        check(country, status, exception, "fr-FR", "unexpected exception");
    }

    @Test
    public void idNotNull() {
        Country country = new Country(null, "name", "capital");
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String exception = MethodArgumentNotValidException.class.getName();
        check(country, status, exception, null, "country id is required");
        check(country, status, exception, "zh-CN", "请指定国家代号");
        check(country, status, exception, "fr-FR", "country id is required");
    }

    @Test
    public void nameNotNull() {
        Country country = new Country("id", null, "capital");
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String exception = MethodArgumentNotValidException.class.getName();
        check(country, status, exception, null, "country name is required");
        check(country, status, exception, "zh-CN", "请指定国家名称");
        check(country, status, exception, "fr-FR", "country name is required");
    }

    @Test
    public void capitalNotNull() {
        Country country = new Country("id", "name", null);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String exception = MethodArgumentNotValidException.class.getName();
        check(country, status, exception, null, "country capital is required");
        check(country, status, exception, "zh-CN", "country capital is required");
        check(country, status, exception, "fr-FR", "country capital is required");
    }

    @Test
    public void idBadSize() {
        Country country = new Country("i", "name", "capital");
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String exception = MethodArgumentNotValidException.class.getName();
        check(country, status, exception, null, "the length of country id is between 2 and 5");
        check(country, status, exception, "zh-CN", "国家代号长度应在2到5之间");
        check(country, status, exception, "fr-FR", "the length of country id is between 2 and 5");
    }

    @Test
    public void nameBadSize() {
        Country country = new Country("id", "n", "capital");
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String exception = MethodArgumentNotValidException.class.getName();
        check(country, status, exception, null, "the length of country name is between 2 and 10");
        check(country, status, exception, "zh-CN", "国家名称长度应在2到10之间");
        check(country, status, exception, "fr-FR", "the length of country name is between 2 and 10");
    }

    @Test
    public void capitalBadSize() {
        Country country = new Country("id", "name", "c");
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String exception = MethodArgumentNotValidException.class.getName();
        check(country, status, exception, null, "the length of country capital is between 2 and 20");
        check(country, status, exception, "zh-CN", "the length of country capital is between 2 and 20");
        check(country, status, exception, "fr-FR", "the length of country capital is between 2 and 20");
    }

    @Test
    public void notNullAndBadSize() {
        Country country = new Country("zhzhzh", null, null);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String exception = MethodArgumentNotValidException.class.getName();
        check(country, status, exception, null, "the length of country id is between 2 and 5",
                                                "country name is required",
                                                "country capital is required");
        check(country, status, exception, "zh-CN", "国家代号长度应在2到5之间",
                                                   "请指定国家名称",
                                                   "country capital is required");
        check(country, status, exception, "fr-FR", "the length of country id is between 2 and 5",
                                                   "country name is required",
                                                   "country capital is required");
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