package com.alkemy.ong.security.service;

import com.alkemy.ong.security.dto.UserWithoutPassDTO;
import javassist.NotFoundException;

import com.alkemy.ong.security.dto.RegisterDTO;

import java.util.List;

public interface UserService {
    void delete(String id) throws NotFoundException;

    RegisterDTO create (RegisterDTO user);
    List<UserWithoutPassDTO> findAllUsers();
}
