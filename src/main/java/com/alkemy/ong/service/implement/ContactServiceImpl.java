package com.alkemy.ong.service.implement;

import com.alkemy.ong.dto.ContactDTO;
import com.alkemy.ong.entity.Contact;
import com.alkemy.ong.mapper.ContactMapper;
import com.alkemy.ong.repository.ContactRepository;
import com.alkemy.ong.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactService {
    @Autowired
    private ContactMapper contactMapper;
    @Autowired
    private ContactRepository contactRepository;

    @Override
    public void saveContact(ContactDTO dto) {
        Contact entity = contactMapper.contactDTO2Entity(dto);
        contactRepository.save(entity);
    }
    @Override
    public void addContact(ContactDTO dto) throws Exception {
        if (contactRepository.existsByEmail(dto.getEmail())) {
            throw new Exception("Contact already exist");
        }
        Contact entity = contactMapper.contactDTO2Entity(dto);
        contactRepository.save(entity);
    }

}
