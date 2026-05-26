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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;

import br.com.jhonecmd.courses_api_front.modules.courses.services.FetchCoursesService;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private FetchCoursesService fetchCoursesService;

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
