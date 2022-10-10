package com.alkemy.ong.dto;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestimonialDTO {

	@NotBlank(message = "Name cannot be empty or null")
	private String name;
	private String image;

	@NotBlank(message = "Content cannot be empty or null")
	private String content;
}