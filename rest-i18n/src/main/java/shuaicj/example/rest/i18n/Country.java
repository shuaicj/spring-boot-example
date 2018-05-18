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
    @Size(min = 2, max = 3)
    private String abbr;

    @NotNull(message = "{shuaicj.country.name.not.null}")
    @Size(min = 2, max = 10, message = "{shuaicj.country.name.size}")
    private String name;

    @NotNull(message = "country capital is required")
    @Size(min = 2, max = 20, message = "the length of country capital is between 2 and 20")
    private String capital;

    public Country() {}

    public Country(String abbr, String name, String capital) {
        this.abbr = abbr;
        this.name = name;
        this.capital = capital;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }
}

