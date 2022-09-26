package com.alkemy.ong.controller;

import com.alkemy.ong.dto.OrganizationDTOPublic;
import com.alkemy.ong.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/organization")
public class OrganizationController {
    @Autowired
    private OrganizationService organizationService;

    @GetMapping("/public")
    public ResponseEntity<List<OrganizationDTOPublic>> getOrganizations() {
        return ResponseEntity.ok(organizationService.getOrganizationsDTO());
    }

}