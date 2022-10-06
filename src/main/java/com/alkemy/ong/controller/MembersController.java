package com.alkemy.ong.controller;

import com.alkemy.ong.dto.MembersDTO;
import com.alkemy.ong.dto.MembersDTO2;
import com.alkemy.ong.dto.PagesDTO;
import com.alkemy.ong.service.MembersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/members")
@Tag(name = "Members Controller", description = "Show, create, delete and update Members")
public class MembersController {

    @Autowired
    private MembersService membersService;

    @Operation(summary = "GET a list of members")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Found members",
                    content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = MembersDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request",
            content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden for no authenticated user",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<MembersDTO>> getAllMembers() {
        List<MembersDTO> membersDTOS = membersService.getAllMembers();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(membersDTOS);
    }

    // -------------- GET Page of Members -------------
    @Operation(summary = "GET Page of Members")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Found members",
                    content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = MembersDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden for no authenticated user",
                    content = @Content)
    })
    @GetMapping(params = "page")
    public ResponseEntity<?> getPageableMembers(@RequestParam(required = false, defaultValue = "-1") int page) {
        PagesDTO<MembersDTO> membersDTOS = membersService.getAllMembers(page);
        return ResponseEntity.ok().body(membersDTOS);
    }

    @Operation(summary = "POST members")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Return created status code",
                    content = {@Content(mediaType = "application/json",
                        schema = @Schema(implementation = MembersDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden for no authenticated user",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<MembersDTO> createMembers(@Valid @RequestBody MembersDTO2 membersDTO2) throws Exception {
        membersService.createMembers(membersDTO2);
        return new ResponseEntity("Create members", HttpStatus.CREATED);
    }

    @Operation(summary = "DELETE members")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return ok status code",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Members not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden for no authenticated user",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMembers(@PathVariable @Parameter(description = "Id of Member to delete", required = true) String id){
        try {
            membersService.deleteMembers(id);
        }catch (NotFoundException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "UPDATE members")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Update members",
                    content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = MembersDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Members not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden for no authenticated user",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMembers(@Parameter(description = "Id of Member to update", required = true) @PathVariable String id,
                                                @RequestBody MembersDTO membersDTO) {
        try {
            membersService.updateMembers(id, membersDTO);
        } catch (
                NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}