package shuaicj.example.rest.common.err;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Define the error.
 *
 * @author shuaicj 2018/05/11
 */
public class Err {

    private String exception;
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss.SSS")
    private Date timestamp;

    public Err() {
        this.timestamp = new Date();
    }

    public Err(String exception, String message) {
        this();
        this.exception = exception;
        this.message = message;
    }

    public Err(Throwable e) {
        this();
        this.exception = e.getClass().getName();
        this.message = e.getMessage();
    }

    public String getException() {
        return exception;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
