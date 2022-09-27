package com.alkemy.ong.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsDTO {

    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    private String content;
    @NotNull
    @NotBlank
    private String image;
    private LocalDateTime updateDateTime;

}