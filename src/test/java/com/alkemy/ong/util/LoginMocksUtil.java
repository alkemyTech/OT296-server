package com.alkemy.ong.util;

import com.alkemy.ong.security.dto.LoginDTO;

public class LoginMocksUtil {
	
    public static LoginDTO generateExistentLoginDTO(){
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("felimune@dev.com");
        loginDTO.setPassword("pass1234");

        return loginDTO;
    }
    
    public static LoginDTO generateFakeLoginDTO(){
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("pepe@botellas.com");
        loginDTO.setPassword("A123");

        return loginDTO;
    }
    
    public static LoginDTO generateRequestMissingMandatoryAttributes(){
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setPassword("userMock123");

        return loginDTO;
    }

}
