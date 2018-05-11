package shuaicj.example.rest.error.handling;

import javax.validation.constraints.NotNull;

/**
 * A java bean with validation.
 *
 * @author shuaicj 2018/05/11
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

    public void setId(long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

