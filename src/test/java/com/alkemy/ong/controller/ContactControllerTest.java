package com.alkemy.ong.controller;

import com.alkemy.ong.dto.ContactDTO;
import com.alkemy.ong.security.service.impl.UserServiceImpl;
import com.alkemy.ong.security.util.JwTUtil;
import com.alkemy.ong.service.ContactService;
import com.alkemy.ong.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@WebMvcTest(ContactController.class)
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactService contactService;

    @MockBean
    private EmailService emailService;

    @MockBean
    JwTUtil jwTUtil;

    @MockBean
    private UserServiceImpl userService;

    @Autowired
    ObjectMapper jsonMapper;

    @BeforeEach
    void setUp() {
        this.jsonMapper = new ObjectMapper();
    }

    @AfterEach
    void tearDown() {

    }

        @DisplayName("contact as admin added succesfull")
        @WithMockUser(username = "mock@admin.com", roles = "ADMIN")
        @Test
        void test1() throws Exception {
            ContactDTO contactDTO = generateContactDTO();
            doNothing().when(contactService).addContact(any());

            mockMvc.perform(post("/contact")
                            .contentType(APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(jsonMapper.writeValueAsString(contactDTO)))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andDo(MockMvcResultHandlers.print());

            Mockito.verify(contactService).addContact(Mockito.any());
        }

    @DisplayName("Contact already exist")
    @Test
    void test2() throws Exception{
        ContactDTO contactDTO = generateContactDTO();
        doNothing().when(contactService).addContact(any());

        mockMvc.perform(post("/contact")
                .contentType(APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(contactDTO)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andDo(MockMvcResultHandlers.print());
        Mockito.verify(contactService, Mockito.never()).addContact(Mockito.any());

    }

    @Test
    void getAllContacts() {

    }

    private static ContactDTO generateContactDTO(){
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setName("NN");
        contactDTO.setEmail("mock@admin.com");
        contactDTO.setPhone("2263355");
        contactDTO.setMessage("message");
        return contactDTO;
    }
}