package com.alkemy.ong.util;

import com.alkemy.ong.security.dto.RegisterDTO;

public class RegisterMocksUtil {

    public static RegisterDTO generateRegisterDTO(){
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setFirstName("Feli");
        registerDTO.setLastName("Mune");
        registerDTO.setEmail("felimune@dev.com");
        registerDTO.setPassword("pass1234");

        return registerDTO;
    }
}