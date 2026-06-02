package br.com.jhonecmd.courses_api_front.modules.categories.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;

import br.com.jhonecmd.courses_api_front.modules.categories.dto.CreateCategoryDTO;
import br.com.jhonecmd.courses_api_front.modules.categories.services.CreateCategoryService;
import br.com.jhonecmd.courses_api_front.modules.categories.services.DeleteCategoryService;
import br.com.jhonecmd.courses_api_front.modules.categories.services.FetchCategoriesService;
import br.com.jhonecmd.courses_api_front.utils.FormatErrorMessage;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private FetchCategoriesService fetchCategoriesService;

    @Autowired
    private CreateCategoryService createCategoryService;

    @Autowired
    private DeleteCategoryService deleteCategoryService;

    @GetMapping("")
    @PreAuthorize("hasRole('RECTOR') or hasRole('DIRECTOR')")
    public String listCategories(Model model) {

        try {

            var result = fetchCategoriesService.execute(getToken());
            authenticated();
            model.addAttribute("categories", result);
            return "modules/categories/categories";
        } catch (HttpClientErrorException ex) {
            ex.printStackTrace();
            SecurityContextHolder.clearContext();
            return "redirect:/users/login";
        }

    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('RECTOR') or hasRole('DIRECTOR')")
    public String create(Model model) {
        model.addAttribute("category", new CreateCategoryDTO());
        return "modules/categories/create";
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('RECTOR') or hasRole('DIRECTOR')")
    public String save(CreateCategoryDTO categoryDTO, Model model) {

        try {
            createCategoryService.execute(getToken(), categoryDTO);
            return "redirect:/categories";
        } catch (HttpClientErrorException ex) {
            model.addAttribute("error", FormatErrorMessage.formatErrorMessage(ex.getResponseBodyAsString()));
            model.addAttribute("category", categoryDTO);
            return "modules/categories/create";
        }

    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('RECTOR') or hasRole('DIRECTOR')")
    public String delete(@PathVariable("id") String categoryId) {

        deleteCategoryService.execute(getToken(), categoryId);
        return "redirect:/categories";
    }

    private String getToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getDetails().toString();
    }

    private void authenticated() {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                "user",
                null,
                AuthorityUtils.createAuthorityList("ROLE_RECTOR", "ROLE_DIRECTOR"));

        auth.setDetails(getToken());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

}
