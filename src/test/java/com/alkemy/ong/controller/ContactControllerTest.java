package com.alkemy.ong.controller;

import com.alkemy.ong.dto.ContactDTO;
import com.alkemy.ong.security.service.impl.UserServiceImpl;
import com.alkemy.ong.security.util.JwTUtil;
import com.alkemy.ong.service.EmailService;
import com.alkemy.ong.service.implement.ContactServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import javassist.NotFoundException;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ContactController.class)
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactServiceImpl contactService;

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

    @Nested
    class createContactTest {

        @DisplayName("Contact as admin added successful")
        @WithMockUser(username = "mock@admin.com", roles = "ADMIN")
        @Test
        void test1() throws Exception {
            ContactDTO contactDTO = generateContactDTO();
            doNothing().when(contactService).addContact(any());

            mockMvc.perform(post("/contact")
                            .contentType(APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(jsonMapper.writeValueAsString(contactDTO)))
                    .andExpect(status().isCreated())
                    .andDo(MockMvcResultHandlers.print());

            Mockito.verify(contactService).addContact(Mockito.any());
        }

        @DisplayName("Contact as user added successful")
        @WithMockUser(username = "mock@user.com", roles = "USER")
        @Test
        void test2() throws Exception {
            ContactDTO contactDTO = generateContactDTO();
            doNothing().when(contactService).addContact(any());

            mockMvc.perform(post("/contact")
                            .contentType(APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(jsonMapper.writeValueAsString(contactDTO)))
                    .andExpect(status().isCreated())
                    .andDo(MockMvcResultHandlers.print());

            Mockito.verify(contactService).addContact(Mockito.any());
        }

        @DisplayName("validate token")
        @Test
        void test3() throws Exception {
            Mockito.when(jwTUtil.isBearer(Mockito.any())).thenReturn(false);
            ContactDTO contactDTO = generateContactDTO();
            doNothing().when(contactService).addContact(any());

            mockMvc.perform(post("/contact")
                            .contentType(APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(jsonMapper.writeValueAsString(contactDTO)))
                    .andExpect(status().isForbidden())
                    .andDo(MockMvcResultHandlers.print());
            Mockito.verify(contactService, Mockito.never()).addContact(Mockito.any());

        }

        @DisplayName("Invalid token case")
        @Test
        void test4() throws Exception {
            Mockito.when(jwTUtil.isBearer(Mockito.any())).thenReturn(true);
            Mockito.when(jwTUtil.validateToken(Mockito.any(), Mockito.any())).thenReturn(false);

            ContactDTO contactDTO = generateContactDTO();
            doNothing().when(contactService).addContact(any());

            mockMvc.perform(post("/contact")
                            .contentType(APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(jsonMapper.writeValueAsString(contactDTO)))
                    .andExpect(status().isForbidden())
                    .andDo(MockMvcResultHandlers.print());
            Mockito.verify(contactService, Mockito.never()).addContact(Mockito.any());
        }

        @DisplayName("contact already exist")
        @WithMockUser(username = "mock@admin.com", roles = "ADMIN")
        @Test
        void test5() throws Exception{
            ContactDTO contactDTO = generateContactDTO();
            doNothing().when(contactService).addContact(contactDTO);
            doThrow(NotFoundException.class).when(contactService).addContact(contactDTO);

            mockMvc.perform(post("/contact")
                            .contentType(APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(jsonMapper.writeValueAsString(contactDTO)))
                    .andExpect(status().isForbidden())
                    .andDo(MockMvcResultHandlers.print());

            Mockito.verify(contactService).addContact(Mockito.any());
        }
    }

    @Nested
    class getAllContactsTest {

        @DisplayName("get all contacts as admin is valid")
        @WithMockUser(username = "mock@admin.com", roles = "ADMIN")
        @Test
        void test1() throws Exception{
            List<ContactDTO> contactDTOList = generateListContactDTO();
            Mockito.when(contactService.getAllContacts()).thenReturn(contactDTOList);

            mockMvc.perform(MockMvcRequestBuilders.get("/contact/contacts"))
                            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk())
                            .andDo(MockMvcResultHandlers.print());
            Mockito.verify(contactService).getAllContacts();
        }

        @DisplayName("get all contacts as user is invalid")
        @WithMockUser(username = "mock@asd.com", roles = "USER")
        @Test
        void test2() throws Exception{
            List<ContactDTO> contactDTOList = generateListContactDTO();
            Mockito.when(contactService.getAllContacts()).thenReturn(contactDTOList);

            mockMvc.perform(MockMvcRequestBuilders.get("/contact/contacts")
                            .content(jsonMapper.writeValueAsString(contactDTOList))
                            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden())
                    .andDo(MockMvcResultHandlers.print());
            Mockito.verify(contactService, Mockito.never()).getAllContacts();
        }

        @DisplayName("forbidden")
        @Test
        void test3() throws Exception {
            Mockito.when(jwTUtil.isBearer(Mockito.any())).thenReturn(false);
            List<ContactDTO> contactDTOList = generateListContactDTO();
            Mockito.when(contactService.getAllContacts()).thenReturn(contactDTOList);

            mockMvc.perform(MockMvcRequestBuilders.get("/contact/contacts")
                            .contentType(APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden())
                    .andDo(MockMvcResultHandlers.print());
            Mockito.verify(contactService, Mockito.never()).getAllContacts();
        }

        @DisplayName("Invalid token case")
        @Test
        void test4() throws Exception {
            Mockito.when(jwTUtil.isBearer(Mockito.any())).thenReturn(true);
            Mockito.when(jwTUtil.validateToken(Mockito.any(), Mockito.any())).thenReturn(false);

            List<ContactDTO> contactDTOList = generateListContactDTO();
            Mockito.when(contactService.getAllContacts()).thenReturn(contactDTOList);

            mockMvc.perform(MockMvcRequestBuilders.get("/contact/contacts")
                            .contentType(APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden())
                    .andDo(MockMvcResultHandlers.print());
            Mockito.verify(contactService, Mockito.never()).getAllContacts();
        }

    }

    private static ContactDTO generateContactDTO() {
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setName("NN");
        contactDTO.setEmail("test@admin.com");
        contactDTO.setPhone("2263355");
        contactDTO.setMessage("message");
        return contactDTO;
    }

    private static List<ContactDTO> generateListContactDTO() {
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setName("Contact dto tests");
        contactDTO.setEmail("test@admin.com");
        contactDTO.setPhone("2263355");
        contactDTO.setMessage("message");
        return Collections.singletonList(contactDTO);
    }
}