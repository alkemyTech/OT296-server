package com.alkemy.ong.security.mapper;

import com.alkemy.ong.dto.RoleDTO;
import com.alkemy.ong.entity.Users;
import com.alkemy.ong.security.dto.UserWithoutPassDTO;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserWithoutPassMapper {

    public UserWithoutPassDTO userWPEntity2DTO(Users users){
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName(users.getRole().getName());
        roleDTO.setDescription(users.getRole().getDescription());
        roleDTO.setTimestamps(users.getRole().getTimestamps());

        UserWithoutPassDTO userDTO = new UserWithoutPassDTO();
        userDTO.setFirstName(users.getFirstName());
        userDTO.setLastName(users.getLastName());
        userDTO.setEmail(users.getEmail());
        userDTO.setRoleDTO(roleDTO);
        return userDTO;
    }

    public List<UserWithoutPassDTO> userWPEntityList2DTOList(List<Users> users){
        List<UserWithoutPassDTO> usersDTO = new ArrayList<UserWithoutPassDTO>();
        for(Users user : users){
            usersDTO.add(this.userWPEntity2DTO(user));
        }
        return usersDTO;
    }
}
