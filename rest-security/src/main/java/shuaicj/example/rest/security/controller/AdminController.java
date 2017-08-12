package shuaicj.example.rest.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * A controller mapping "/admin".
 *
 * @author shuaicj 2017/08/12
 */
@RestController
public class AdminController {

    @GetMapping("/admin")
    public String admin(Principal user) {
        return user.getName();
    }
}

