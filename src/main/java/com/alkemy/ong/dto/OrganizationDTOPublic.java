package com.alkemy.ong.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDTOPublic {

    private String name;
    private String image;
    private String address;
    private Integer phone;
    private String urlFacebook;
    private String urlLinkedin;
    private String urlInstagram;

    private List<SlidesDTO> slides;

}
