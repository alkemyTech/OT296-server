package com.alkemy.ong.controller;

import com.alkemy.ong.dto.MembersDTO;
import com.alkemy.ong.dto.MembersDTO2;
import com.alkemy.ong.service.MembersService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    // -------------- GET Page of Members -------------
    @GetMapping(params = "page")
    public ResponseEntity<List<MembersDTO>> getPageableMembers(@RequestParam(required = false, defaultValue = "-1") int page) {
        List<MembersDTO> membersDTOS = membersService.getAllMembers(page);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(membersDTOS);
    }

    @PostMapping
    public ResponseEntity<MembersDTO> createMembers(@Valid @RequestBody MembersDTO2 membersDTO2) throws Exception {
        membersService.createMembers(membersDTO2);
        return new ResponseEntity("Create members", HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMembers(@PathVariable String id){
        try {
            membersService.deleteMembers(id);
        }catch (NotFoundException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMembers(@PathVariable String id, @RequestBody MembersDTO membersDTO) {
        try {
            membersService.updateMembers(id, membersDTO);
        } catch (
                NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}