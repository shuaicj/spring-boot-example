package shuaicj.example.rest.common.err;

/**
 * A simple exception.
 *
 * @author shuaicj 2018/05/11
 */
@SuppressWarnings("serial")
public class NotFoundException extends RuntimeException {

    public NotFoundException() {}

    public NotFoundException(String message) {
        super(message);
    }
}
