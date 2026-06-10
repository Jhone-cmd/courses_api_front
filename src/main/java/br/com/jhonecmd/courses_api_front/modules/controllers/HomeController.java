package br.com.jhonecmd.courses_api_front.modules.controllers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.jhonecmd.courses_api_front.modules.courses.dto.CourseResponseDTO;
import br.com.jhonecmd.courses_api_front.modules.courses.services.FetchAllCoursesService;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private FetchAllCoursesService fetchAllCoursesService;

    @GetMapping("")
    public String home(Model model) {
        CourseResponseDTO[] courses = fetchAllCoursesService.execute();

        if (courses == null) {
            courses = new CourseResponseDTO[0];
        }

        int activeCourses = 0;
        Set<String> categories = new HashSet<>();

        for (CourseResponseDTO course : courses) {
            if (Boolean.TRUE.equals(course.getActive())) {
                activeCourses++;
            }

            if (course.getCategoryName() != null && !course.getCategoryName().isBlank()) {
                categories.add(course.getCategoryName());
            }
        }

        model.addAttribute("courses", courses);
        model.addAttribute("totalCourses", courses.length);
        model.addAttribute("activeCourses", activeCourses);
        model.addAttribute("totalCategories", categories.size());

        return "modules/home/index";
    }

    @GetMapping("/courses/v2")
    public String courses(@RequestParam(value = "search", required = false) String search, Model model) {

        CourseResponseDTO[] coursesArray = fetchAllCoursesService.execute();

        if (coursesArray == null) {
            coursesArray = new CourseResponseDTO[0];
        }

        List<CourseResponseDTO> filteredCourses = Arrays.asList(coursesArray);

        if (search != null && !search.isBlank()) {
            String term = search.toLowerCase().trim();
            filteredCourses = filteredCourses.stream()
                    .filter(course -> course.getName() != null && course.getName().toLowerCase().contains(term))
                    .collect(Collectors.toList());

            model.addAttribute("searchTerm", search);
        }

        model.addAttribute("courses", filteredCourses);

        return "modules/home/courses-v2";
    }

}
