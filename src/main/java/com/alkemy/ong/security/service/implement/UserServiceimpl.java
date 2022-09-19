package com.alkemy.ong.security.service.implement;

import com.alkemy.ong.security.service.UserService;
import com.alkemy.ong.security.dto.RegisterDTO;
import com.alkemy.ong.entity.Users;
import com.alkemy.ong.security.mapper.UserMapper;
import com.alkemy.ong.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserServiceimpl implements UserService{
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public RegisterDTO create(RegisterDTO user) {
        Users newUser = userMapper.userDTO2Entity(user);
        Users userSave = usersRepository.save(newUser);
        RegisterDTO registerDTO = userMapper.userEntity2DTO(userSave);
        return registerDTO;
    }


}
