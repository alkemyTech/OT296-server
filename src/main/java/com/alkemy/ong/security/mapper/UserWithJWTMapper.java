package com.alkemy.ong.security.mapper;

import org.springframework.stereotype.Component;

import com.alkemy.ong.entity.Users;
import com.alkemy.ong.security.dto.RegisterDTO;

@Component
public class UserWithJWTMapper {

	private String password;

	public RegisterDTO userEntity2DTO(Users users, String jwt){
		RegisterDTO registerDTO = new RegisterDTO();
		registerDTO.setFirstName(users.getFirstName());
		registerDTO.setLastName(users.getLastName());
		registerDTO.setEmail(users.getEmail());
		registerDTO.setPassword(password);
		registerDTO.setJwt(jwt);
		return registerDTO;
	}		
}