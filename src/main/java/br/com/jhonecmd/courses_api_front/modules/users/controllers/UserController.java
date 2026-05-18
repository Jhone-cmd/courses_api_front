package br.com.jhonecmd.courses_api_front.modules.users.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("users")
public class UserController {

    @GetMapping("/login")
    public String login() {
        return "modules/users/login";
    }

}
