package com.alkemy.ong.mapper;

import com.alkemy.ong.dto.ContactDTO;
import com.alkemy.ong.entity.Contact;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ContactMapper {
    public Contact contactDTO2Entity(ContactDTO dto) {
        Contact entity = new Contact();
        entity.setName(dto.getName());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        entity.setMessage(dto.getMessage());
        return entity;
    }

    public ContactDTO contactEntity2DTO(Contact entity) {
        ContactDTO dto = new ContactDTO();
        dto.setName(entity.getName());
        dto.setPhone(entity.getPhone());
        dto.setEmail(entity.getEmail());
        dto.setMessage(entity.getMessage());
        return dto;
    }

    public List<ContactDTO> contactEntityList2DTOList(List<Contact> contacts){
        List<ContactDTO> contactDTOS = new ArrayList<ContactDTO>();
        for(Contact contact : contacts){
            contactDTOS.add(this.contactEntity2DTO(contact));
        }
        return contactDTOS;
    }

}
