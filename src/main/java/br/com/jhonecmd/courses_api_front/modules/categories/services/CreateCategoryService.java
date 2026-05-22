package br.com.jhonecmd.courses_api_front.modules.categories.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.jhonecmd.courses_api_front.modules.categories.dto.CreateCategoryDTO;

@Service
public class CreateCategoryService {

    @Value("${api.url}")
    private String apiUrl;

    public String execute(String token, CreateCategoryDTO categoryDTO) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<CreateCategoryDTO> request = new HttpEntity<>(categoryDTO, headers);

        var url = apiUrl.concat("/categories");

        var result = restTemplate.postForObject(url, request, String.class);
        return result;
    }
}
