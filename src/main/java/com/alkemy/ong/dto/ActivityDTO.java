package com.alkemy.ong.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class ActivityDTO {

    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    private String content;
    private String image;
}
