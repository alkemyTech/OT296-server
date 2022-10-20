package com.alkemy.ong.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
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
