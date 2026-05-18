package br.com.jhonecmd.courses_api_front.modules.users.dto;

import lombok.Data;

@Data
public class Token {

    private String access_token;
    private Long expiresAt;
    private String position;

}
