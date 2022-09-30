package com.alkemy.ong.dto;

import com.alkemy.ong.entity.Slides;
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

    public SlidesDTO(Slides slides1) {
        this.imageURL = slides1.getImageURL();
        this.text = slides1.getText();
        this.order = slides1.getOrder();
        this.organizationID = slides1.getOrganizationID();
    }
}
