package com.alkemy.ong.repository.mapper;

import com.alkemy.ong.awsS3.service.AmazonClient;
import com.alkemy.ong.dto.SlidesDTO;
import com.alkemy.ong.dto.SlidesDTOPublic;
import com.alkemy.ong.entity.Slides;
import com.alkemy.ong.utils.ImageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SlidesMapper {

    @Autowired
    private ImageHelper imageHelper;

    @Autowired
    private AmazonClient amazonClient;
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

    public Slides SlideDTO2Entity (SlidesDTO dto) throws IOException {
        Slides entity = new Slides();
        String fileName = "slide_" + dto.getOrganizationID() + dto.getOrder() + ".png";
        MultipartFile multipart = imageHelper.base64ToImage(dto.getImageURL(), fileName);
        String urlAmazon = amazonClient.uploadFile(multipart);
        entity.setImageURL(urlAmazon);
        entity.setOrder(dto.getOrder());
        entity.setText(dto.getText());
        entity.setOrganizationID(dto.getOrganizationID());
        return entity;
    }

}
