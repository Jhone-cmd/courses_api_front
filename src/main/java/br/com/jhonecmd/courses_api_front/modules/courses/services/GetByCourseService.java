package br.com.jhonecmd.courses_api_front.modules.courses.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.jhonecmd.courses_api_front.modules.courses.dto.CourseResponseDTO;

@Service
public class GetByCourseService {

    @Value("${api.url}")
    private String apiUrl;

    public CourseResponseDTO execute(String token, String courseId) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(headers);

        var url = apiUrl.concat("/courses/{id}");

        var result = restTemplate.exchange(url, HttpMethod.GET, request,
                CourseResponseDTO.class, courseId);
        return result.getBody();

    }
}
