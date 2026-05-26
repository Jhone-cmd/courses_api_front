package br.com.jhonecmd.courses_api_front.modules.courses.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;

import br.com.jhonecmd.courses_api_front.modules.categories.services.FetchCategoriesService;
import br.com.jhonecmd.courses_api_front.modules.courses.dto.CreateCourseDTO;
import br.com.jhonecmd.courses_api_front.modules.courses.services.CreateCourseService;
import br.com.jhonecmd.courses_api_front.modules.courses.services.FetchCoursesService;
import br.com.jhonecmd.courses_api_front.utils.FormatErrorMessage;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private FetchCategoriesService fetchCategoriesService;

    @Autowired
    private FetchCoursesService fetchCoursesService;

    @Autowired
    private CreateCourseService createCourseService;

    @GetMapping("")
    @PreAuthorize("hasRole('RECTOR') or hasRole('DIRECTOR') or hasRole('COORDINATOR')")
    public String listCourses(Model model) {
        try {

            var result = fetchCoursesService.execute(getToken());
            authenticated();
            model.addAttribute("courses", result);
            return "modules/courses/courses";
        } catch (HttpClientErrorException ex) {
            ex.printStackTrace();
            SecurityContextHolder.clearContext();
            return "redirect:/users/login";
        }
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('RECTOR') or hasRole('DIRECTOR') or hasRole('COORDINATOR')")
    public String create(Model model) {

        var result = fetchCategoriesService.execute(getToken());
        authenticated();
        model.addAttribute("categories", result);
        model.addAttribute("course", new CreateCourseDTO());
        return "modules/courses/create";
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('RECTOR') or hasRole('DIRECTOR') or hasRole('COORDINATOR')")
    public String save(CreateCourseDTO courseDTO, Model model) {

        try {
            createCourseService.execute(getToken(), courseDTO);
            return "redirect:/courses";
        } catch (HttpClientErrorException ex) {
            model.addAttribute("error", FormatErrorMessage.formatErrorMessage(ex.getResponseBodyAsString()));
            model.addAttribute("course", courseDTO);
            return "modules/categories/create";
        }

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
