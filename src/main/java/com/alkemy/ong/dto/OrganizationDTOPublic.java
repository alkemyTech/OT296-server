package com.alkemy.ong.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDTOPublic {

    private String name;
    private String image;
    private String address;
    private Integer phone;
}
