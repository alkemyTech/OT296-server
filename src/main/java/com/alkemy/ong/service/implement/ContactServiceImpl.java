package com.alkemy.ong.service.implement;

import com.alkemy.ong.dto.ContactDTO;
import com.alkemy.ong.entity.Contact;
import com.alkemy.ong.mapper.ContactMapper;
import com.alkemy.ong.repository.ContactRepository;
import com.alkemy.ong.service.ContactService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {
    @Autowired
    private ContactMapper contactMapper;
    @Autowired
    private ContactRepository contactRepository;

    @Override
    public void addContact(ContactDTO dto) throws NotFoundException {
        if (contactRepository.existsByEmail(dto.getEmail())) {
            throw new NotFoundException("Contact already exist");
        }
        Contact entity = contactMapper.contactDTO2Entity(dto);
        contactRepository.save(entity);
    }

    @Override
    public List<ContactDTO> getAllContacts(){
        List<Contact> contactEntities = contactRepository.findAll();
        List<ContactDTO> contactDTOS = contactMapper.contactEntityList2DTOList(contactEntities);
        return contactDTOS;
    }

}