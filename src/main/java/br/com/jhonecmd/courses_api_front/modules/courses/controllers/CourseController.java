package br.com.jhonecmd.courses_api_front.modules.courses.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;

import br.com.jhonecmd.courses_api_front.modules.categories.services.FetchCategoriesService;
import br.com.jhonecmd.courses_api_front.modules.courses.dto.CreateCourseDTO;
import br.com.jhonecmd.courses_api_front.modules.courses.services.ChangeStatusCourseService;
import br.com.jhonecmd.courses_api_front.modules.courses.services.CreateCourseService;
import br.com.jhonecmd.courses_api_front.modules.courses.services.DeleteCourseService;
import br.com.jhonecmd.courses_api_front.modules.courses.services.FetchCoursesService;
import br.com.jhonecmd.courses_api_front.modules.courses.services.GetByCourseService;
import br.com.jhonecmd.courses_api_front.utils.FormatErrorMessage;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CreateCourseService createCourseService;

    private final FetchCategoriesService fetchCategoriesService;

    private final FetchCoursesService fetchCoursesService;

    private final GetByCourseService getByCourseService;

    private final ChangeStatusCourseService changeStatusCourseService;

    private final DeleteCourseService deleteCourseService;

    CourseController(CreateCourseService createCourseService, FetchCategoriesService fetchCategoriesService,
            FetchCoursesService fetchCoursesService, GetByCourseService getByCourseService,
            ChangeStatusCourseService changeStatusCourseService, DeleteCourseService deleteCourseService) {
        this.createCourseService = createCourseService;
        this.fetchCategoriesService = fetchCategoriesService;
        this.fetchCoursesService = fetchCoursesService;
        this.getByCourseService = getByCourseService;
        this.changeStatusCourseService = changeStatusCourseService;
        this.deleteCourseService = deleteCourseService;
    }

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

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasRole('RECTOR') or hasRole('DIRECTOR') or hasRole('COORDINATOR')")
    public String update(Model model, @PathVariable("id") String courseId) {

        model.addAttribute("course", getByCourseService.execute(getToken(), courseId));
        var result = fetchCategoriesService.execute(getToken());
        model.addAttribute("categories", result);
        return "modules/courses/update";
    }

    @PatchMapping("/{id}/active")
    @PreAuthorize("hasRole('RECTOR') or hasRole('DIRECTOR') or hasRole('COORDINATOR')")
    public String changeStatusCourse(@PathVariable("id") String courseId) {

        changeStatusCourseService.execute(getToken(), courseId);
        return "redirect:/courses";
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('RECTOR') or hasRole('DIRECTOR') or hasRole('COORDINATOR')")
    public String delete(@PathVariable("id") String courseId) {

        deleteCourseService.execute(getToken(), courseId);
        return "redirect:/courses";
    }

    private String getToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getDetails().toString();
    }

    private void authenticated() {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                "user",
                null,
                AuthorityUtils.createAuthorityList("ROLE_RECTOR", "ROLE_DIRECTOR", "ROLE_COORDINATOR"));

        auth.setDetails(getToken());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
