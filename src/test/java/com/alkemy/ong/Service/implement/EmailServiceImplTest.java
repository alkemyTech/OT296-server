package com.alkemy.ong.service.implement;

import com.alkemy.ong.security.dto.RegisterDTO;
import com.sendgrid.SendGrid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = EmailServiceImpl.class)
class EmailServiceImplTest {

    @Mock
    private SendGrid sendGrid;

    private String emailSender = System.getenv("EMAIL_SENDER");

    private EmailServiceImpl emailService;

    private RegisterDTO registerDTO;

    @BeforeEach
    void setup(){
        emailService = new EmailServiceImpl();

        registerDTO = new RegisterDTO();
        registerDTO.setEmail("eze@gmail.com");
        registerDTO.setPassword("1234");
        registerDTO.setFirstName("Ezequiel");
        registerDTO.setLastName("Perez");
    }

    @Test
    @DisplayName("Send Welcome Email")
    void sendWelcomeEmailTo(){

        emailService.sendWelcomeEmailTo(registerDTO);

    }

    @Test
    @DisplayName("sendEmailTo")
    void sendEmailTo(){
        emailService.sendEmailTo("User", "message");
    }
}