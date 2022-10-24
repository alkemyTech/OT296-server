package com.alkemy.ong.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class OrganizationDTO {

    private String name;
    private String image;
    private String address;
    private Integer phone;
    private String email;
    private String urlFacebook;
    private String urlLinkedin;
    private String urlInstagram;
    private String welcomeText;
    private String aboutUsText;
    private LocalDateTime timestamps;

}
