package br.com.jhonecmd.courses_api_front.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/", "/courses/v2", "/users/login", "/users/signIn", "/users/create",
                    "/users/change-password")
                    .permitAll();
            auth.anyRequest().authenticated();
        }).formLogin(form -> form.loginPage("/users/login"));
        return http.build();
    }
}
