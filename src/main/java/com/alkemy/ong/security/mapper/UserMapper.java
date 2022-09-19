package com.alkemy.ong.security.mapper;

import com.alkemy.ong.security.dto.RegisterDTO;
import com.alkemy.ong.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    @Autowired
    private PasswordEncoder encoder;

    private String password;

    public Users userDTO2Entity(RegisterDTO user){
        Users users = new Users();
        users.setFirstName(user.getFirstName());
        users.setLastName(user.getLastName());
        users.setEmail(user.getEmail());
        users.setPassword(encoder.encode(user.getPassword()));
        password = user.getPassword();
        return users;
    }

    public RegisterDTO userEntity2DTO(Users users){
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setFirstName(users.getFirstName());
        registerDTO.setLastName(users.getLastName());
        registerDTO.setEmail(users.getEmail());
        registerDTO.setPassword(password);
        return registerDTO;
    }
}
