package com.alkemy.ong.service;

import com.alkemy.ong.dto.ContactDTO;

import java.util.List;

public interface ContactService {

    public void addContact(ContactDTO dto) throws Exception;

    public List<ContactDTO> getAllContacts();

}