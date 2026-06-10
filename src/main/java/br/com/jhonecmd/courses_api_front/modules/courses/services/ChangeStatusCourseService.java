package br.com.jhonecmd.courses_api_front.modules.courses.services;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChangeStatusCourseService {

    @Value("${api.url}")
    private String apiUrl;

    public void execute(String token, String courseId) {

        var factory = new JdkClientHttpRequestFactory();
        RestTemplate restTemplate = new RestTemplate(factory);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        Map<String, Object> body = new HashMap<>();
        body.put("active", true);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        var url = apiUrl.concat("/courses/{id}/active");

        restTemplate.exchange(
                url,
                HttpMethod.PATCH,
                request,
                Void.class,
                courseId);
    }
}