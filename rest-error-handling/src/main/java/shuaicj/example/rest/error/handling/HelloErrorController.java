package shuaicj.example.rest.error.handling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shuaicj.example.rest.common.Hello;
import shuaicj.example.rest.common.err.Err;
import shuaicj.example.rest.common.err.NotFoundException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * A rest controller with error handling.
 *
 * @author shuaicj 2018/05/11
 */
@RestController
public class HelloErrorController {

    @PostMapping("/hello-err")
    public String post(@Valid @RequestBody Hello h) {
        long id = h.getId();
        if (id < 0) {
            throw new RuntimeException("unexpected error");
        } else if (id == 0) {
            throw new IllegalArgumentException("illegal 0");
        } else if (id == 1) {
            throw new HelloException("hello 1");
        } else if (id > 100) {
            throw new NotFoundException("id not found");
        }
        return "ok";
    }

    /**
     * Reuse spring-boot error response body, but changing http status only.
     * This is the first way to do it.
     * See also {@link GlobalErrorHandler#globalScopeErrorHandler(IllegalArgumentException, HttpServletResponse)}.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @SuppressWarnings("serial")
    public static class HelloException extends RuntimeException {

        public HelloException(String message) {
            super(message);
        }
    }

    /**
     * Custom error response body.
     * This controller-scoped handler is prior to the global one
     * {@link GlobalErrorHandler#globalScopeErrorHandler(NotFoundException)}.
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Err controllerScopeErrorHandler(NotFoundException e) {
        Err err = new Err(e);
        err.setMessage(err.getMessage() + " [controller scope]");
        return err;
    }
}

