package com.alkemy.ong.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {

    private String email;
    private String password;
}