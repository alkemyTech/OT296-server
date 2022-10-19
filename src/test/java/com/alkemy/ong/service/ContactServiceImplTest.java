package com.alkemy.ong.service;

import com.alkemy.ong.dto.ContactDTO;
import com.alkemy.ong.entity.Contact;
import com.alkemy.ong.mapper.ContactMapper;
import com.alkemy.ong.repository.ContactRepository;
import com.alkemy.ong.service.implement.ContactServiceImpl;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ContactServiceImpl.class)
class ContactServiceImplTest {

    @MockBean
    ContactRepository contactRepository;

    @SpyBean
    ContactMapper contactMapper;

    private ContactServiceImpl contactService;

    Contact contact;

    @BeforeEach
    void setUp() {
        contact = Contact.builder()
                .name("NN")
                .email("test@admin.com")
                .phone("2263355")
                .message("message")
                .build();

        contactService = new ContactServiceImpl(contactMapper,contactRepository);

    }

    @DisplayName("add contact successful")
    @Test
    void addContact() throws NotFoundException {
        ContactDTO contactDTO = generateContactDTO();

        given(contactRepository.save(contact)).willReturn(contact);
        given(contactMapper.contactDTO2Entity(contactDTO)).willReturn(contact);
        given(contactMapper.contactEntity2DTO(contact)).willReturn(contactDTO);
        contactService.addContact(contactDTO);

        verify(contactRepository,times(1)).save(any());
    }

    @DisplayName("cannot add a contact already added")
    @Test
    void cannotAddContact() throws NotFoundException {

        ContactDTO contactDTO = generateContactDTO();

        contactService.addContact(contactDTO);
        given(contactRepository.existsByEmail(contactDTO.getEmail())).willReturn(true);

        assertThrows(NotFoundException.class,() -> contactService.addContact(contactDTO));
        verify(contactRepository,times(1)).save(any());
    }

    @DisplayName("get all contacts")
    @Test
    void getAllContacts() {
        ContactDTO contactDTO = generateContactDTO();
        given(contactRepository.findAll()).willReturn(List.of(contact));
        given(contactMapper.contactEntityList2DTOList(List.of(contact))).willReturn(List.of(contactDTO));
        List<ContactDTO> contactDTOS = contactService.getAllContacts();
        assertThat(contactDTOS).isNotNull();
        assertThat(contactDTOS.size()).isEqualTo(1);
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