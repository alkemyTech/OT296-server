package com.alkemy.ong.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CommentDTO {
    @NotNull
    @NotBlank
    private String userId;
    private String body;
    @NotNull
    @NotBlank
    private String newsId;
}