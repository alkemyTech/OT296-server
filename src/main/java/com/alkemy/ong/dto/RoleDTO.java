package com.alkemy.ong.dto;

import lombok.Setter;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Setter
public class RoleDTO {
    @NotNull
    private String name;
    private String description;
    private Date timestamps;
}
