package br.com.jhonecmd.courses_api_front.modules.users.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.jhonecmd.courses_api_front.modules.users.services.LoginUserService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("users")
public class UserController {

    @Autowired
    private LoginUserService loginUserService;

    @GetMapping("/login")
    public String login() {
        return "modules/users/login";
    }

    @PostMapping("/signIn")
    public String singIn(RedirectAttributes redirectAttributes, HttpSession session, String email, String password) {
        try {

            var token = loginUserService.login(email, password);
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

            return "redirect:/users/profile";

        } catch (HttpClientErrorException ex) {

            redirectAttributes.addFlashAttribute("error", "Credenciais Inválidas");
            return "redirect:/users/login";
        }

    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('RECTOR') or hasRole('DIRECTOR')")
    public String profile() {
        return "modules/users/profile";
    }

}
