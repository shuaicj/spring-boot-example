package shuaicj.example.rest.common;

import javax.validation.constraints.NotNull;

/**
 * A java bean representing a greeting.
 *
 * @author shuaicj 2017/08/12
 */
public class Hello {

    private long id;

    @NotNull
    private String content;

    public Hello() {}

    public Hello(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}

