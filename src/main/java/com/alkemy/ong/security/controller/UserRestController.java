package com.alkemy.ong.security.controller;

import com.alkemy.ong.dto.UserDTO;
import com.alkemy.ong.entity.Users;
import com.alkemy.ong.security.dto.LoginDTO;
import com.alkemy.ong.security.dto.RegisterDTO;
import com.alkemy.ong.security.dto.UserWithoutPassDTO;
import com.alkemy.ong.security.response.AuthenticationResponse;
import com.alkemy.ong.security.service.UserService;
import com.alkemy.ong.UserServiceTest.UserServiceImpl;
import com.alkemy.ong.security.util.JwTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UserRestController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwTUtil jwTUtil;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private UserService userService;

    @Tag(name = "Users")
    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get data user login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Action carried out successfully"),
    })
    public ResponseEntity<UserDTO> meData(@CurrentSecurityContext(expression = "authentication") Authentication authentication) {
        return ResponseEntity.ok(userService.meData(authentication.getName()));
    }

    @Tag(name = "Authentication")
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "User input")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Action carried out successfully"),
            @ApiResponse(responseCode = "403", description = "Action forbidden"),
            @ApiResponse(responseCode = "404", description = "Bad credentials")
    })
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginDTO loginDTO) throws Exception {
        Authentication auth;
        try {
            auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

        } catch (BadCredentialsException e) {
            return new ResponseEntity(("ok: false"), HttpStatus.BAD_REQUEST);
        }

        final String jwt = jwTUtil.generateToken(auth);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @Tag(name = "Authentication")
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "New user register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Action carried out successfully"),
    })
    public ResponseEntity<RegisterDTO> register(@RequestBody @Valid RegisterDTO user){
        RegisterDTO registerDTO = userService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerDTO);
    }

    @Tag(name = "Users")
    @DeleteMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Action carried out successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Users> delete(@PathVariable String id){
        try{
            userService.delete(id);
        }catch(NotFoundException e){
            e.getMessage();
            return new ResponseEntity(("User Not Found"),HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Tag(name = "Users")
    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Action carried out successfully"),
    })
    public ResponseEntity<List<UserWithoutPassDTO>> findAll(){
        List<UserWithoutPassDTO> usuarios = userService.findAllUsers();
        return ResponseEntity.ok(usuarios);
    }

    @Tag(name = "Users")
    @PatchMapping(value = "/users/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Patch user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Action carried out successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Users> patchUser(@PathVariable String id, @RequestBody Map<Object, Object> objectMap){
        try{
            userService.patchUser(id, objectMap);
        }catch(NotFoundException e){
            return new ResponseEntity("User Not Found",HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}