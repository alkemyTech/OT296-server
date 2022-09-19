package com.alkemy.ong.security.service.implement;

import com.alkemy.ong.entity.Users;
import com.alkemy.ong.security.service.UserService;
import com.alkemy.ong.security.dto.RegisterDTO;
import com.alkemy.ong.security.mapper.UserMapper;
import com.alkemy.ong.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceimpl implements UserService{
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public RegisterDTO create(RegisterDTO user) {
        Users newUsers = userMapper.userDTO2Entity(user);
        Users usersSave = usersRepository.save(newUsers);
        RegisterDTO registerDTO = userMapper.userEntity2DTO(usersSave);
        return registerDTO;
    }


}
