package com.alkemy.ong.service;

import com.alkemy.ong.security.dto.RegisterDTO;

public interface EmailService {
	
	void sendWelcomeEmailTo(RegisterDTO user);
	
	void sendEmailTo(String to, String message);

}
