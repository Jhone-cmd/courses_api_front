package br.com.jhonecmd.courses_api_front.modules.courses.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.client.RestTemplate;

import br.com.jhonecmd.courses_api_front.modules.courses.dto.CourseResponseDTO;

@Service
public class FetchAllCoursesService {

    @Value("${api.url}")
    private String apiUrl;

    public CourseResponseDTO[] execute() {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(headers);

        var url = apiUrl.concat("/courses/v2");

        try {
            var result = restTemplate.exchange(url, HttpMethod.GET, request,
                    CourseResponseDTO[].class);
            System.out.println(result);
            return result.getBody();
        } catch (Unauthorized ex) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
    }
}
