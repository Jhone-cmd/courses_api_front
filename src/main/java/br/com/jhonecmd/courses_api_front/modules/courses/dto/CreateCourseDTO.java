package br.com.jhonecmd.courses_api_front.modules.courses.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class CreateCourseDTO {

    private String name;
    private String description;
    private Boolean active = false;
    private String teacherName;
    private UUID categoryId;

}
