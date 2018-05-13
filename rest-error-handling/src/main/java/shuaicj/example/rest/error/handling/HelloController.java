package shuaicj.example.rest.error.handling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * A rest controller with error handling.
 *
 * @author shuaicj 2018/05/11
 */
@RestController
public class HelloController {

    @PostMapping("/hello")
    public String post(@Valid @RequestBody Hello h) {
        long id = h.getId();
        if (id < 0) {
            throw new RuntimeException("unexpected error");
        } else if (id > 100) {
            throw new HelloNotFoundException("id not found");
        }
        return "ok";
    }

    /**
     * This controller-scoped handler is prior to the global one
     * {@link GlobalErrorHandler#globalScopeErrorHandler(HelloNotFoundException)}.
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error controllerScopeErrorHandler(HelloNotFoundException e) {
        Error err = new Error(e);
        err.setMessage(err.getMessage() + " [controller scope]");
        return err;
    }
}

