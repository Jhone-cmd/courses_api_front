package br.com.jhonecmd.courses_api_front.modules.categories.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.jhonecmd.courses_api_front.modules.categories.dto.CategoryResponseDTO;

@Service
public class GetByCategoryService {

    @Value("${api.url}")
    private String apiUrl;

    public CategoryResponseDTO execute(String token, String categoryId) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(headers);

        var url = apiUrl.concat("/categories/{id}");

        var result = restTemplate.exchange(url, HttpMethod.GET, request,
                CategoryResponseDTO.class, categoryId);
        return result.getBody();

    }
}
