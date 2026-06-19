package br.com.jhonecmd.courses_api_front.modules.users.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.jhonecmd.courses_api_front.modules.users.dto.ChangePasswordUserDTO;
import br.com.jhonecmd.courses_api_front.modules.users.dto.CreateUserDTO;
import br.com.jhonecmd.courses_api_front.modules.users.dto.UpdateUserDTO;
import br.com.jhonecmd.courses_api_front.modules.users.services.ChangePasswordUserService;
import br.com.jhonecmd.courses_api_front.modules.users.services.CreateUserService;
import br.com.jhonecmd.courses_api_front.modules.users.services.DeleteUserService;
import br.com.jhonecmd.courses_api_front.modules.users.services.FetchUserService;
import br.com.jhonecmd.courses_api_front.modules.users.services.GetByUserService;
import br.com.jhonecmd.courses_api_front.modules.users.services.LoginUserService;
import br.com.jhonecmd.courses_api_front.modules.users.services.UpdateUserService;
import br.com.jhonecmd.courses_api_front.utils.FormatErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class UserController {

    private final CreateUserService createUserService;

    private final LoginUserService loginUserService;

    private final GetByUserService getByUserService;

    private final FetchUserService fetchUserService;

    private final UpdateUserService updateUserService;

    private final ChangePasswordUserService changePasswordUserService;

    private final DeleteUserService deleteUserService;

    UserController(DeleteUserService deleteUserService, ChangePasswordUserService changePasswordUserService,
            CreateUserService createUserService, LoginUserService loginUserService, GetByUserService getByUserService,
            FetchUserService fetchUserService, UpdateUserService updateUserService) {
        this.deleteUserService = deleteUserService;
        this.changePasswordUserService = changePasswordUserService;
        this.createUserService = createUserService;
        this.loginUserService = loginUserService;
        this.getByUserService = getByUserService;
        this.fetchUserService = fetchUserService;
        this.updateUserService = updateUserService;
    }

    @GetMapping({ "/create", "/create/admin" })
    public String showRegisterForm(Model model, HttpServletRequest request) {
        model.addAttribute("user", new CreateUserDTO());

        boolean isAdminRoute = request.getRequestURI().contains("admin");
        model.addAttribute("isAdminRoute", isAdminRoute);

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
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toString().toUpperCase()))
                    .toList();

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    grants);

            auth.setDetails(token.getAccess_token());

            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(auth);
            SecurityContextHolder.setContext(securityContext);

            session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
            session.setAttribute("token", token);

            return "redirect:/courses";

        } catch (HttpClientErrorException ex) {
            redirectAttributes.addFlashAttribute("error", "Credenciais Inválidas");
            return "redirect:/users/login";
        }
    }

    @GetMapping("/me")
    public String me(Model model) {

        try {

            var result = getByUserService.execute(getToken());
            model.addAttribute("userLogged", result);
            return "modules/users/me";

        } catch (HttpClientErrorException ex) {
            ex.printStackTrace();
            return "redirect:/users/login";
        }

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

    @GetMapping("/editar/{id}")
    public String update(Model model) {

        model.addAttribute("user", getByUserService.execute(getToken()));
        return "modules/users/update";
    }

    @PostMapping("/editar/{id}")
    public String saveUpdate(@PathVariable("id") String userId, @ModelAttribute("user") UpdateUserDTO updateUserDTO) {

        updateUserService.execute(getToken(), userId, updateUserDTO);
        return "redirect:/users/me";
    }

    @GetMapping("/change-password")
    public String changePassword(Model model) {
        model.addAttribute("user", new ChangePasswordUserDTO());
        return "modules/users/changePassword";
    }

    @PostMapping("/change-password")
    public String saveChangePassword(ChangePasswordUserDTO userDTO, Model model) {

        try {
            changePasswordUserService.execute(userDTO);
            return "redirect:/users/login";
        } catch (HttpClientErrorException ex) {
            model.addAttribute("error", FormatErrorMessage.formatErrorMessage(ex.getResponseBodyAsString()));
            model.addAttribute("user", userDTO);
            return "modules/users/changePassword";
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('RECTOR')")
    public String delete(@PathVariable("id") String userId) {

        deleteUserService.execute(getToken(), userId);
        return "redirect:/users/profiles";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletRequest request) {

        SecurityContextHolder.clearContext();

        if (session != null) {
            session.invalidate();
        }

        var cookies = request.getCookies();
        if (cookies != null) {
            for (var cookie : cookies) {
                if ("JSESSIONID".equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                }
            }
        }

        return "redirect:/users/login";
    }

    private String getToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getDetails().toString();
    }

}
