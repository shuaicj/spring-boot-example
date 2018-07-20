package shuaicj.example.rest.error.handling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shuaicj.example.rest.common.err.Err;
import shuaicj.example.rest.common.err.NotFoundException;

/**
 * Global error handler.
 *
 * @author shuaicj 2018/05/11
 */
@RestControllerAdvice
public class GlobalErrorHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalErrorHandler.class);

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public Err globalScopeErrorHandler(NotFoundException e) {
        return new Err(e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public Err globalScopeErrorHandler(MethodArgumentNotValidException e) {
        return new Err(e);
    }

    // a general exception handler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public Err globalScopeErrorHandler(Exception e) throws Exception {
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }
        logger.warn("unexpected exception", e);
        return new Err(e);
    }
}
