package com.alkemy.ong.service;

import com.alkemy.ong.dto.RegisterDTO;

public interface EmailService {
	
	void sendWelcomeEmailTo(RegisterDTO user);

}
