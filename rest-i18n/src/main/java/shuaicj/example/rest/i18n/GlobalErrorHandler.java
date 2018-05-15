package shuaicj.example.rest.i18n;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import shuaicj.example.rest.common.err.Err;
import shuaicj.example.rest.common.err.NotFoundException;

/**
 * Global error handler.
 *
 * @author shuaicj 2018/05/14
 */
@ControllerAdvice
public class GlobalErrorHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalErrorHandler.class);
    @Autowired I18nHelper i18n;

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    @ResponseBody
    public Err globalScopeErrorHandler(NotFoundException e) {
        return new Err(e.getClass().getName(), i18n.get("Exception.notFound", new Object[]{e.getMessage()}));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    @ResponseBody
    public Err globalScopeErrorHandler(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
                          .getAllErrors()
                          .stream()
                          .map(objectError -> {
                              String msg = objectError.getDefaultMessage();
                              if (msg != null && msg.startsWith("{") && msg.endsWith("}")) {
                                  String code = msg.substring(1, msg.length() - 1);
                                  return i18n.get(code, objectError.getArguments());
                              }
                              return i18n.get(objectError);
                          })
                          .collect(Collectors.joining("; "));
        return new Err(e.getClass().getName(), message);
    }

    // a general exception handler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    @ResponseBody
    public Err globalScopeErrorHandler(Exception e) throws Exception {
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }
        logger.warn("unexpected exception", e);
        return new Err(e.getClass().getName(), i18n.get("Exception.unexpected"));
    }
}
