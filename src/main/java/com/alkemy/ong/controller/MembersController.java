package com.alkemy.ong.controller;

import com.alkemy.ong.dto.MembersDTO;
import com.alkemy.ong.service.MembersService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MembersController {

    @Autowired
    private MembersService membersService;

    @GetMapping
    public ResponseEntity<List<MembersDTO>> getAllMembers() {
        List<MembersDTO> membersDTOS = membersService.getAllMembers();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(membersDTOS);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMembers(@PathVariable String id, @RequestBody MembersDTO membersDTO){
        try {
            membersService.updateMembers(id, membersDTO);
        }catch (
                NotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
