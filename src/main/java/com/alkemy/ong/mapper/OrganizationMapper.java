package com.alkemy.ong.mapper;

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

}
