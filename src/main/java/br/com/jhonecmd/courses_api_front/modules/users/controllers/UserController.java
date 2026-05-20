package br.com.jhonecmd.courses_api_front.modules.users.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.jhonecmd.courses_api_front.modules.users.dto.CreateUserDTO;
import br.com.jhonecmd.courses_api_front.modules.users.services.CreateUserService;
import br.com.jhonecmd.courses_api_front.modules.users.services.FetchUserService;
import br.com.jhonecmd.courses_api_front.modules.users.services.LoginUserService;
import br.com.jhonecmd.courses_api_front.utils.FormatErrorMessage;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("users")
public class UserController {

    @Autowired
    private CreateUserService createUserService;

    @Autowired
    private LoginUserService loginUserService;

    @Autowired
    private FetchUserService fetchUserService;

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("user", new CreateUserDTO());
        return "modules/users/create";
    }

    @PostMapping("/create")
    public String save(CreateUserDTO userDTO, Model model) {

        try {
            createUserService.execute(userDTO);
            return "redirect:/users/create";
        } catch (HttpClientErrorException ex) {
            model.addAttribute("error", FormatErrorMessage.formatErrorMessage(ex.getResponseBodyAsString()));
            model.addAttribute("user", userDTO);
            return "modules/users/create";
        }

    }

    @GetMapping("/login")
    public String login() {
        return "modules/users/login";
    }

    @PostMapping("/signIn")
    public String singIn(RedirectAttributes redirectAttributes, HttpSession session, String email, String password) {
        try {

            var token = loginUserService.execute(email, password);
            var grants = token.getPosition().stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" +
                            role.toString().toUpperCase()))
                    .toList();

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(null, null, grants);

            auth.setDetails(token.getAccess_token());

            SecurityContextHolder.getContext().setAuthentication(auth);
            SecurityContext securityContext = SecurityContextHolder.getContext();
            session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
            session.setAttribute("token", token);

            return "redirect:/users/me";

        } catch (HttpClientErrorException ex) {

            redirectAttributes.addFlashAttribute("error", "Credenciais Inválidas");
            return "redirect:/users/login";
        }

    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('RECTOR')")
    public String me() {

        return "modules/users/me";

    }

    @GetMapping("/profiles")
    @PreAuthorize("hasRole('RECTOR')")
    public String profile(Model model) {

        try {

            var result = fetchUserService.execute(getToken());
            model.addAttribute("users", result);
            return "modules/users/profiles";

        } catch (HttpClientErrorException ex) {
            ex.printStackTrace();
            return "redirect:/users/login";
        }

    }

    private String getToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getDetails().toString();
    }

}
