package com.alkemy.ong.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlidesDTO {

    private String imageURL;
    private String text;
    private String order;
    private String organizationID;
}
