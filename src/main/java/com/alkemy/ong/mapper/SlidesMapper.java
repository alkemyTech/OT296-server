package com.alkemy.ong.mapper;

import com.alkemy.ong.dto.OrganizationDTOPublic;
import com.alkemy.ong.dto.SlidesDTO;
import com.alkemy.ong.dto.SlidesDTOPublic;
import com.alkemy.ong.entity.Slides;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SlidesMapper {
   public SlidesDTOPublic slidesEntity2DTOPublic (Slides entity) {
       SlidesDTOPublic dtoPublic = new SlidesDTOPublic();
       dtoPublic.setImageURL(entity.getImageURL());
       dtoPublic.setOrder(entity.getOrder());
       return dtoPublic;
   }

   public List<SlidesDTOPublic> slidesEntityList2DTO (List<Slides> entities) {
       List<SlidesDTOPublic> listDTOPublic = new ArrayList<>();

       for(Slides entity : entities) {
           listDTOPublic.add(this.slidesEntity2DTOPublic(entity));
       }
       return listDTOPublic;
   }

   public SlidesDTO SlidesEntity2DTO (Slides entity) {
       SlidesDTO dto = new SlidesDTO();
       dto.setImageURL(entity.getImageURL());
       dto.setOrder(entity.getOrder());
       dto.setText(entity.getText());
       dto.setOrganizationID(entity.getOrganizationID());
       return dto;
   }

    /*public SlidesDTO Slides2DTO (Slides entity) {
        SlidesDTO dto = new SlidesDTO();
        OrganizationDTOPublic orgDTO = new OrganizationDTOPublic();
        String idOrg = String.valueOf(entity.getOrganizationID());
        dto.setImageURL(entity.getImageURL());
        dto.setOrder(entity.getOrder());
        dto.setText(entity.getText());
        dto.setOrganizationID(entity.getOrganizationID());
        return dto;
    }*/

}
