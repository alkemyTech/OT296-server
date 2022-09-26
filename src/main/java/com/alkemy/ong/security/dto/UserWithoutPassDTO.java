package com.alkemy.ong.security.dto;

import com.alkemy.ong.dto.RoleDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserWithoutPassDTO {
    @NotNull
    @NotBlank
    private String firstName;

    @NotNull
    @NotBlank
    private String lastName;

    @NotNull
    @NotBlank
    @Email
    private String email;

    @NotNull
    @NotBlank
    private String photo;

    @NotNull
    @NotBlank
    private LocalDateTime timestamps;

    @NotNull
    @NotBlank
    private RoleDTO roleDTO;
}
