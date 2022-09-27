package com.alkemy.ong.controller;

import com.alkemy.ong.dto.ActivityDTO;
import com.alkemy.ong.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @PostMapping()
    public ResponseEntity<ActivityDTO> create(@Valid @RequestBody ActivityDTO activityDTO) {
        activityService.createActivity(activityDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("{id}")
    public ResponseEntity<ActivityDTO> update(@PathVariable String id,@RequestBody ActivityDTO activityDTO) {
        try{
            ActivityDTO activityDTO1 = activityService.updateActivity(activityDTO,id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(activityDTO1);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }
}
