package br.com.jhonecmd.courses_api_front.modules.users.dto;

import lombok.Data;

@Data
public class UpdateUserDTO {

    private String name;
    private String email;
    private String position;
}
