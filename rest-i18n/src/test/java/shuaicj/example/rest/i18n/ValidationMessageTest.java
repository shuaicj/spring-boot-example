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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
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
        ResponseEntity<String> e = rest.exchange("/countries/1", HttpMethod.PUT,
                new HttpEntity<>(new Country("cn", "name", "capital")), String.class);
        assertThat(e.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(e.getBody()).isEqualTo("ok");
    }

    @Test
    public void notFound() {
        Country country = new Country("cn", "name", "capital");
        HttpStatus status = HttpStatus.NOT_FOUND;
        String exception = NotFoundException.class.getName();
        check("-1", country, status, exception, null, "id -1 not found");
        check("-1", country, status, exception, "zh-CN", "找不到id -1");
        check("-1", country, status, exception, "fr-FR", "id -1 not found");
    }

    @Test
    public void typeMismatch() {
        Country country = new Country("cn", "name", "capital");
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String exception = MethodArgumentTypeMismatchException.class.getName();
        check("abc", country, status, exception, null, "id type mismatch");
        check("abc", country, status, exception, "zh-CN", "id类型不匹配");
        check("abc", country, status, exception, "fr-FR", "id type mismatch");
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
    public void abbrNotNull() {
        Country country = new Country(null, "name", "capital");
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String exception = MethodArgumentNotValidException.class.getName();
        check(country, status, exception, null, "country abbr is required");
        check(country, status, exception, "zh-CN", "请指定国家代号");
        check(country, status, exception, "fr-FR", "country abbr is required");
    }

    @Test
    public void nameNotNull() {
        Country country = new Country("cn", null, "capital");
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String exception = MethodArgumentNotValidException.class.getName();
        check(country, status, exception, null, "country name is required");
        check(country, status, exception, "zh-CN", "请指定国家名称");
        check(country, status, exception, "fr-FR", "country name is required");
    }

    @Test
    public void capitalNotNull() {
        Country country = new Country("cn", "name", null);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String exception = MethodArgumentNotValidException.class.getName();
        check(country, status, exception, null, "country capital is required");
        check(country, status, exception, "zh-CN", "country capital is required");
        check(country, status, exception, "fr-FR", "country capital is required");
    }

    @Test
    public void abbrBadSize() {
        Country country = new Country("i", "name", "capital");
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String exception = MethodArgumentNotValidException.class.getName();
        check(country, status, exception, null, "the length of country abbr is between 2 and 3");
        check(country, status, exception, "zh-CN", "国家代号长度应在2到3之间");
        check(country, status, exception, "fr-FR", "the length of country abbr is between 2 and 3");
    }

    @Test
    public void nameBadSize() {
        Country country = new Country("cn", "n", "capital");
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String exception = MethodArgumentNotValidException.class.getName();
        check(country, status, exception, null, "the length of country name is between 2 and 10");
        check(country, status, exception, "zh-CN", "国家名称长度应在2到10之间");
        check(country, status, exception, "fr-FR", "the length of country name is between 2 and 10");
    }

    @Test
    public void capitalBadSize() {
        Country country = new Country("cn", "name", "c");
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
        check(country, status, exception, null, "the length of country abbr is between 2 and 3",
                                                "country name is required",
                                                "country capital is required");
        check(country, status, exception, "zh-CN", "国家代号长度应在2到3之间",
                                                   "请指定国家名称",
                                                   "country capital is required");
        check(country, status, exception, "fr-FR", "the length of country abbr is between 2 and 3",
                                                   "country name is required",
                                                   "country capital is required");
    }

    private void check(Country country, HttpStatus status, String exception,
                       String locale, String... messages) {
        check("1", country, status, exception, locale, messages);
    }

    private void check(String id, Country country, HttpStatus status, String exception,
                       String locale, String... messages) {
        HttpHeaders headers = new HttpHeaders();
        if (locale != null) {
            headers.set(HttpHeaders.ACCEPT_LANGUAGE, locale);
        }
        ResponseEntity<Err> e = rest.exchange("/countries/" + id, HttpMethod.PUT,
                new HttpEntity<>(country, headers), Err.class);
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