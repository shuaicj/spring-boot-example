package shuaicj.example.rest.i18n;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A java bean with validation.
 *
 * @author shuaicj 2018/05/14
 */
public class Country {

    @NotNull
    @Size(min = 2, max = 5)
    private String code;

    @NotNull
    private String name;

    public Country() {}

    public Country(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

