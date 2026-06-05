package br.com.jhonecmd.courses_api_front.modules.categories.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.jhonecmd.courses_api_front.modules.categories.dto.UpdateCategoryDTO;

@Service
public class UpdateCategoryService {

    @Value("${api.url}")
    private String apiUrl;

    public void execute(String token, String categoryId, UpdateCategoryDTO updateCategoryDTO) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<UpdateCategoryDTO> request = new HttpEntity<>(updateCategoryDTO, headers);

        var url = apiUrl.concat("/categories/{id}");

        restTemplate.exchange(
                url,
                HttpMethod.PUT,
                request,
                UpdateCategoryDTO.class,
                categoryId);
    }
}
