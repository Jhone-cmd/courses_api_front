package br.com.jhonecmd.courses_api_front.modules.users.dto;

import lombok.Data;

@Data
public class ChangePasswordUserDTO {

    private String email;
    private String password;
}
