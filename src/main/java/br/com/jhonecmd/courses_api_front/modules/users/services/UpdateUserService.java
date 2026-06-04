package br.com.jhonecmd.courses_api_front.modules.users.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.jhonecmd.courses_api_front.modules.users.dto.UpdateUserDTO;

@Service
public class UpdateUserService {

    @Value("${api.url}")
    private String apiUrl;

    // 1. Adicione o UpdateUserDTO nos parâmetros
    public void execute(String token, String userId, UpdateUserDTO updateUserDTO) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<UpdateUserDTO> request = new HttpEntity<>(updateUserDTO, headers);

        var url = apiUrl.concat("/users/{id}");

        restTemplate.exchange(
                url,
                HttpMethod.PUT,
                request,
                UpdateUserDTO.class,
                userId);
    }
}
