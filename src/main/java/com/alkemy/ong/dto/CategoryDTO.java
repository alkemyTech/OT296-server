package com.alkemy.ong.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class CategoryDTO {

    @NotNull
    @NotBlank
    private  String name;

    private String description;

    private String image;

    private LocalDate timestamps;
}
