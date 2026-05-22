package br.com.jhonecmd.courses_api_front.modules.categories.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;

import br.com.jhonecmd.courses_api_front.modules.categories.services.FetchCategoriesService;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private FetchCategoriesService fetchCategoriesService;

    @GetMapping()
    @PreAuthorize("hasRole('RECTOR') or hasRole('DIRECTOR')")
    public String listCategories(Model model) {

        try {
            var result = fetchCategoriesService.execute(getToken());
            System.out.println(result);
            model.addAttribute("categories", result);
            return "modules/categories/categories";
        } catch (HttpClientErrorException ex) {
            ex.printStackTrace();
            return "modules/users/login";
        }

    }

    private String getToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getDetails().toString();
    }
}
