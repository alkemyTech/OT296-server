package com.alkemy.ong.mapper;

import com.alkemy.ong.dto.OrganizationDTO;
import com.alkemy.ong.dto.OrganizationDTOPublic;
import com.alkemy.ong.entity.Organization;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrganizationMapper {
    public OrganizationDTOPublic organizationEntity2DTO (Organization organization) {
        OrganizationDTOPublic dtoPublic = new OrganizationDTOPublic();
        dtoPublic.setName(organization.getName());
        dtoPublic.setImage(organization.getImage());
        dtoPublic.setPhone(organization.getPhone());
        dtoPublic.setAddress(organization.getAddress());
        return dtoPublic;
    }
    public List<OrganizationDTOPublic> organizationListEntity2DTO (List<Organization> organizations){
        List<OrganizationDTOPublic> dtoList = new ArrayList<>();

        for(Organization organization : organizations){
        dtoList.add(this.organizationEntity2DTO(organization));
        }

        return dtoList;
    }
    public OrganizationDTO organizationEntity2DTOCreate (Organization organization) {
        OrganizationDTO dto = new OrganizationDTO();
        dto.setName(organization.getName());
        dto.setImage(organization.getImage());
        dto.setAddress(organization.getAddress());
        dto.setPhone(organization.getPhone());
        dto.setEmail(organization.getEmail());
        dto.setWelcomeText(organization.getWelcomeText());
        dto.setAboutUsText(organization.getAboutUsText());
        dto.setTimestamps(organization.getTimestamps());
        return dto;
    }
    public Organization organizationDto2Entity(OrganizationDTO organizationDTO){
        Organization organization= new Organization();
        organization.setName(organizationDTO.getName());
        organization.setImage(organizationDTO.getImage());
        organization.setAddress(organizationDTO.getAddress());
        organization.setPhone(organizationDTO.getPhone());
        organization.setEmail(organizationDTO.getEmail());
        organization.setWelcomeText(organizationDTO.getWelcomeText());
        organization.setAboutUsText(organizationDTO.getAboutUsText());
        organization.setTimestamps(organizationDTO.getTimestamps());
        return organization;
    }
}
