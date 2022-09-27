package com.alkemy.ong.security.mapper;

import com.alkemy.ong.entity.Users;
import com.alkemy.ong.mapper.RoleMapper;
import com.alkemy.ong.security.dto.UserWithoutPassDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserWithoutPassMapper {
    @Autowired
    private RoleMapper roleMapper;

    public UserWithoutPassDTO userWPEntity2DTO(Users users){
        UserWithoutPassDTO userDTO = new UserWithoutPassDTO();
        userDTO.setFirstName(users.getFirstName());
        userDTO.setLastName(users.getLastName());
        userDTO.setEmail(users.getEmail());
        userDTO.setRoleDTO(roleMapper.roleEntity2DTO(users.getRole()));
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
