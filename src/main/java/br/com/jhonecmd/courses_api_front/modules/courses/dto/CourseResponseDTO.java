package br.com.jhonecmd.courses_api_front.modules.courses.dto;

import lombok.Data;

@Data
public class CourseResponseDTO {

    private String id;
    private String name;
    private String description;
    private String categoryName;
    private Boolean active;

}
