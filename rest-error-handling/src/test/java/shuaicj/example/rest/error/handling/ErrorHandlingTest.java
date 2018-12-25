package shuaicj.example.rest.error.handling;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.MethodArgumentNotValidException;
import shuaicj.example.rest.common.Hello;
import shuaicj.example.rest.common.err.Err;
import shuaicj.example.rest.common.err.NotFoundException;

import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

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
    public void ok() {
        ResponseEntity<String> e = rest.postForEntity("/hello-err", new Hello(50, "abc"), String.class);
        assertThat(e.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(e.getBody()).isEqualTo("ok");
    }

    @Test
    public void illegalArgument() {
        ResponseEntity<Map> e = rest.postForEntity("/hello-err", new Hello(0, "abc"), Map.class);
        assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(e.getBody().get("exception")).isEqualTo(IllegalArgumentException.class.getName());
        assertThat(e.getBody().get("message")).isEqualTo("illegal 0");
    }

    @Test
    public void helloException() {
        ResponseEntity<Map> e = rest.postForEntity("/hello-err", new Hello(1, "abc"), Map.class);
        assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(e.getBody().get("exception")).isEqualTo(HelloErrorController.HelloException.class.getName());
        assertThat(e.getBody().get("message")).isEqualTo("hello 1");
    }

    @Test
    public void notFound() {
        ResponseEntity<Err> e = rest.postForEntity("/hello-err", new Hello(101, "abc"), Err.class);
        assertThat(e.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(e.getBody().getException()).isEqualTo(NotFoundException.class.getName());
        assertThat(e.getBody().getMessage()).isEqualTo("id not found [controller scope]");
        assertThat(e.getBody().getTimestamp()).isBeforeOrEqualsTo(new Date());
    }

    @Test
    public void jsonValidationFailed() {
        ResponseEntity<Err> e = rest.postForEntity("/hello-err", new Hello(50, null), Err.class);
        assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(e.getBody().getException()).isEqualTo(MethodArgumentNotValidException.class.getName());
        assertThat(e.getBody().getMessage()).contains("NotNull");
        assertThat(e.getBody().getTimestamp()).isBeforeOrEqualsTo(new Date());
    }

    @Test
    public void unexpected() {
        ResponseEntity<Err> e = rest.postForEntity("/hello-err", new Hello(-1, "abc"), Err.class);
        assertThat(e.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(e.getBody().getException()).isEqualTo(RuntimeException.class.getName());
        assertThat(e.getBody().getMessage()).isEqualTo("unexpected error");
        assertThat(e.getBody().getTimestamp()).isBeforeOrEqualsTo(new Date());
    }
}