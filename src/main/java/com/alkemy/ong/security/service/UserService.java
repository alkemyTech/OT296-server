package com.alkemy.ong.security.service;

import javassist.NotFoundException;

import com.alkemy.ong.security.dto.RegisterDTO;

import java.util.List;

public interface UserService {
    void delete(String id) throws NotFoundException;

    RegisterDTO create (RegisterDTO user);
    List<RegisterDTO> findAllUsers();
}
