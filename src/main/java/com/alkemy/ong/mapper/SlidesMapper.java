package com.alkemy.ong.mapper;

import com.alkemy.ong.dto.SlidesDTOPublic;
import com.alkemy.ong.entity.Slides;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SlidesMapper {
   public SlidesDTOPublic slidesEntity2DTO (Slides entity) {
       SlidesDTOPublic dtoPublic = new SlidesDTOPublic();
       dtoPublic.setImageURL(entity.getImageURL());
       dtoPublic.setOrder(entity.getOrder());
       return dtoPublic;
   }

   public List<SlidesDTOPublic> slidesEntityList2DTO (List<Slides> entities) {
       List<SlidesDTOPublic> listDTOPublic = new ArrayList<>();

       for(Slides entity : entities) {
           listDTOPublic.add(this.slidesEntity2DTO(entity));
       }
       return listDTOPublic;
   }

}
