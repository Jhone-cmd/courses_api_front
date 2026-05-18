package br.com.jhonecmd.courses_api_front.modules.users.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.jhonecmd.courses_api_front.modules.users.dto.CreateUserDTO;

@Service
public class CreateUserService {

    @Value("${api.url}")
    private String apiUrl;

    public String execute(CreateUserDTO userDTO) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateUserDTO> request = new HttpEntity<>(userDTO);

        var url = apiUrl.concat("/users");

        var result = restTemplate.postForObject(url, request, String.class);
        return result;
    }
}
