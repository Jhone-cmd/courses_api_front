package br.com.jhonecmd.courses_api_front.modules.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping("")
    public String redirectToLogin() {
        return "redirect:/users/login";
    }

}
