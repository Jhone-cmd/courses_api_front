package br.com.jhonecmd.courses_api_front.modules.users.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DeleteUserService {

    @Value("${api.url}")
    private String apiUrl;

    public void execute(String token, String userId) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        var url = apiUrl.concat("/users/{id}");

        restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                request,
                Void.class,
                userId);
    }
}
