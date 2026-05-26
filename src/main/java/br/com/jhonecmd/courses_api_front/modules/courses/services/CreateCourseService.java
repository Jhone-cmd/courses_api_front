package br.com.jhonecmd.courses_api_front.modules.courses.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.jhonecmd.courses_api_front.modules.courses.dto.CreateCourseDTO;

@Service
public class CreateCourseService {

    @Value("${api.url}")
    private String apiUrl;

    public String execute(String token, CreateCourseDTO courseDTO) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<CreateCourseDTO> request = new HttpEntity<>(courseDTO, headers);

        String url = String.format("%s/categories/%s/courses", apiUrl, courseDTO.getCategoryId());

        var result = restTemplate.postForObject(url, request, String.class);
        return result;
    }
}
