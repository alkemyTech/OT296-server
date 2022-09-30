package com.alkemy.ong.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
public class MembersDTO2 {

    @Pattern(regexp = "[a-zA-Z\\s]*", message = "The name cannot contains numbers or characters specials")
    private String name;
}