package shuaicj.example.rest.i18n;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/countries")
    public String post(@Valid @RequestBody Country country) {
        String id = country.getId();
        if (id.equals("xxx")) {
            throw new RuntimeException();
        } else if (id.equals("yyy")) {
            throw new NotFoundException("yyy");
        }
        return "ok";
    }
}

