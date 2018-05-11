package shuaicj.example.rest.error.handling;

/**
 * A simple exception.
 *
 * @author shuaicj 2018/05/11
 */
@SuppressWarnings("serial")
public class HelloNotFoundException extends RuntimeException {

    public HelloNotFoundException(String message) {
        super(message);
    }
}
