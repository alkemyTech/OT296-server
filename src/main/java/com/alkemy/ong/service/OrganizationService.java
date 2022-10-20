package com.alkemy.ong.service;

import com.alkemy.ong.dto.OrganizationDTO;
import com.alkemy.ong.dto.OrganizationDTOPublic;
import javassist.NotFoundException;

import java.util.List;
import java.util.Map;

public interface OrganizationService {
    public List<OrganizationDTOPublic> getOrganizationsDTO();
    public OrganizationDTO create(OrganizationDTO dto);
    public OrganizationDTOPublic patchOrganization(String id, Map<Object, Object> objectMap) throws NotFoundException;

    OrganizationDTOPublic getSlidesByIdOngOrder(String idOng) throws NotFoundException;
}
