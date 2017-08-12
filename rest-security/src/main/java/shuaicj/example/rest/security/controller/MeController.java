package shuaicj.example.rest.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * A controller mapping "/me".
 *
 * @author shuaicj 2017/08/12
 */
@RestController
public class MeController {

    @GetMapping("/me")
    public String me(Principal user) {
        return user.getName();
    }
}

