package com.alkemy.ong.dto;

import com.alkemy.ong.entity.Organization;
import com.alkemy.ong.entity.Slides;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDTOPublic {

    private String name;
    private String image;
    private String address;
    private Integer phone;

    private List<SlidesDTO> slides;

}
