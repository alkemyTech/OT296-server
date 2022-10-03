package com.alkemy.ong.controller;

import com.alkemy.ong.dto.OrganizationDTO;
import com.alkemy.ong.dto.OrganizationDTOPublic;
import com.alkemy.ong.dto.SlidesDTO;
import com.alkemy.ong.entity.Organization;
import com.alkemy.ong.service.OrganizationService;
import com.alkemy.ong.service.SlidesService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/organization")
public class OrganizationController {
    @Autowired
    private OrganizationService organizationService;


    @GetMapping("/public")
    public ResponseEntity<List<OrganizationDTOPublic>> getOrganizations() {
        return ResponseEntity.ok(organizationService.getOrganizationsDTO());
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<Object> getById(@PathVariable String id) throws NotFoundException {
        try {
            OrganizationDTOPublic dtos = organizationService.getSlidesByIdOngOrder(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(dtos);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/public")
    public ResponseEntity<OrganizationDTO>create(@RequestBody OrganizationDTO organization){
        OrganizationDTO organizationCreated = organizationService.create(organization);
        return ResponseEntity.status(HttpStatus.CREATED).body(organizationCreated);
    }
    @PatchMapping("/public/{id}")
    public ResponseEntity<Organization> patchOrganization(@PathVariable String id, @RequestBody Map<Object, Object> objectMap){
        try{
            organizationService.patchOrganization(id, objectMap);
        }catch(NotFoundException e){
            return new ResponseEntity("Organization Not Found",HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

}