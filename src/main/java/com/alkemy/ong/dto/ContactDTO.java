package com.alkemy.ong.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactDTO {

    @NotNull @NotEmpty @NotBlank
    private String name;

    private String phone;

    @NotNull @NotEmpty @NotBlank
    private String email;

    private String message;
}
