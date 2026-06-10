package br.com.jhonecmd.courses_api_front.modules.users.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.jhonecmd.courses_api_front.modules.users.dto.ChangePasswordUserDTO;

@Service
public class ChangePasswordUserService {

    @Value("${api.url}")
    private String apiUrl;

    public void execute(ChangePasswordUserDTO userDTO) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ChangePasswordUserDTO> request = new HttpEntity<>(userDTO);

        var url = apiUrl.concat("/users/change-password");

        restTemplate.exchange(url, HttpMethod.PATCH, request, Void.class);

    }
}
