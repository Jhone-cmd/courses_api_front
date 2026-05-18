package br.com.jhonecmd.courses_api_front.modules.users.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("users")
public class UserController {

    @GetMapping("/login")
    public String login() {
        return "modules/users/login";
    }

}
