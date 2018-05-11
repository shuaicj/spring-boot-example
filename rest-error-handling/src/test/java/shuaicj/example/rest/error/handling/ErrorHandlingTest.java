package shuaicj.example.rest.error.handling;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.HttpRequestMethodNotSupportedException;

/**
 * Test error handling
 *
 * @author shuaicj 2017/08/12
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ErrorHandlingTest {

    @Autowired
    private TestRestTemplate rest;

    @Test
    public void notFound() {
        ResponseEntity<Error> e = rest.postForEntity("/hello", new Hello(101, "abc"), Error.class);
        assertThat(e.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(e.getBody().getException()).isEqualTo(HelloNotFoundException.class.getName());
        assertThat(e.getBody().getMessage()).isEqualTo("id not found");
        assertThat(e.getBody().getTimestamp()).isBeforeOrEqualsTo(new Date());
    }

    @Test
    public void methodNotAllowed() {
        ResponseEntity<Error> e = rest.getForEntity("/hello", Error.class);
        assertThat(e.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
        assertThat(e.getBody().getException()).isEqualTo(HttpRequestMethodNotSupportedException.class.getName());
        assertThat(e.getBody().getMessage()).isEqualTo("unexpected");
        assertThat(e.getBody().getTimestamp()).isBeforeOrEqualsTo(new Date());
    }

    @Test
    public void unexpected() {
        // ResponseEntity<Error> e = rest.postForEntity("/hello", new Hello(-1, "abc"), Error.class);
        // assertThat(e.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        // assertThat(e.getBody().getException()).isEqualTo(RuntimeException.class.getName());
        // assertThat(e.getBody().getMessage()).isEqualTo("unexpected");
        // assertThat(e.getBody().getTimestamp()).isBeforeOrEqualsTo(new Date());
    }
}