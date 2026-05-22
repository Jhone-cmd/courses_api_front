package br.com.jhonecmd.courses_api_front.modules.categories.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @GetMapping()
    public String listCategories() {

        return "modules/categories/categories";
    }
}
