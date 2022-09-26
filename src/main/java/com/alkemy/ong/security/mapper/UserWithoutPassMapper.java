package com.alkemy.ong.security.mapper;

import com.alkemy.ong.entity.Users;
import com.alkemy.ong.security.dto.RegisterDTO;
import com.alkemy.ong.security.dto.UserWithoutPassDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserWithoutPassMapper {

    public UserWithoutPassDTO userWPEntity2DTO(Users users){
        UserWithoutPassDTO userDTO = new UserWithoutPassDTO();
        userDTO.setFirstName(users.getFirstName());
        userDTO.setLastName(users.getLastName());
        userDTO.setEmail(users.getEmail());
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
