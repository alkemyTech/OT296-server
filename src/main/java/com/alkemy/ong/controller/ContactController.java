package com.alkemy.ong.controller;

import com.alkemy.ong.dto.ContactDTO;
import com.alkemy.ong.service.ContactService;
import com.alkemy.ong.service.EmailService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<Object> createContact(@RequestBody @Valid ContactDTO dto) {
        try {
            contactService.addContact(dto);
            emailService.sendEmailTo(dto.getEmail(), "Muchas gracias por contactarte con nosotros, en breve nos volveremos a comunicar contigo.");

        }catch (NotFoundException e) {
            return new ResponseEntity<>("Contact already exist", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>("Created", HttpStatus.CREATED);
    }

    @GetMapping("/contacts")
    public ResponseEntity<List<ContactDTO>> getAllContacts(){
        List<ContactDTO> contacts = contactService.getAllContacts();
        return ResponseEntity.status(HttpStatus.OK).body(contacts);
    }

}