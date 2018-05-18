package shuaicj.example.rest.i18n;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shuaicj.example.rest.common.err.NotFoundException;

/**
 * A simple rest controller.
 *
 * @author shuaicj 2018/05/14
 */
@RestController
public class CountryController {

    @Autowired I18nHelper i18n;

    @GetMapping("/my-country")
    public String get() {
        return i18n.get("shuaicj.country");
    }

    @GetMapping("/countries/cn/cities/{cityId}")
    public String getCity() {
        return null;
    }

    @PutMapping("/countries/{id}")
    public String put(@PathVariable long id, @Valid @RequestBody Country country) {
        if (id < 0) {
            throw new NotFoundException("id " + id);
        }
        if (country.getAbbr().equals("xxx")) {
            throw new RuntimeException();
        }
        return "ok";
    }
}

