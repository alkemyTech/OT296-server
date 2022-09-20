package com.alkemy.ong.mapper;

import com.alkemy.ong.dto.OrganizationDTO;
import com.alkemy.ong.entity.Organization;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrganizationMapper {
    public OrganizationDTO organizationEntity2DTO (Organization organization) {
        OrganizationDTO dto = new OrganizationDTO();
        dto.setName(organization.getName());
        dto.setImage(organization.getImage());
        dto.setPhone(organization.getPhone());
        dto.setAddress(organization.getAddress());
        return dto;
    }
    public List<OrganizationDTO> organizationListEntity2DTO (List<Organization> organizations){
        List<OrganizationDTO> dtoList = new ArrayList<>();

        for(Organization organization : organizations){
        dtoList.add(this.organizationEntity2DTO(organization));
        }

        return dtoList;
        }

}
