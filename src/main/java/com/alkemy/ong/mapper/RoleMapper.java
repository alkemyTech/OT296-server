package com.alkemy.ong.mapper;

import com.alkemy.ong.dto.RoleDTO;
import com.alkemy.ong.entity.Role;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class RoleMapper {
    public RoleDTO roleEntity2DTO(Role role){
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName(role.getName());
        roleDTO.setDescription(role.getDescription());
        roleDTO.setTimestamps(role.getTimestamps());
        return roleDTO;
    }

    public List<RoleDTO> roleEntityList2DTOList(List<Role> roles){
        List<RoleDTO> rolesDTO = new ArrayList<RoleDTO>();
        for(Role role : roles){
            rolesDTO.add(this.roleEntity2DTO(role));
        }
        return rolesDTO;
    }
}
