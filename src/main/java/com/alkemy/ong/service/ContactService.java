package com.alkemy.ong.service;

import com.alkemy.ong.dto.ContactDTO;

public interface ContactService {
    public void saveContact(ContactDTO dto);
    public void addContact(ContactDTO dto) throws Exception;

}
