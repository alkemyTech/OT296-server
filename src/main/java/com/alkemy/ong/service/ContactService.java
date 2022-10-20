package com.alkemy.ong.service;

import com.alkemy.ong.dto.ContactDTO;
import javassist.NotFoundException;

import java.util.List;

public interface ContactService {

    public void addContact(ContactDTO dto) throws NotFoundException;

    public List<ContactDTO> getAllContacts();
}
