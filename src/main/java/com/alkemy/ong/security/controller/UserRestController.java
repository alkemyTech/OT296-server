package com.alkemy.ong.security.controller;

import com.alkemy.ong.entity.Users;
import com.alkemy.ong.security.dto.LoginDTO;
import com.alkemy.ong.security.dto.RegisterDTO;
import com.alkemy.ong.security.response.AuthenticationResponse;
import com.alkemy.ong.security.service.UserService;
import com.alkemy.ong.security.service.impl.UserServiceImpl;
import com.alkemy.ong.security.util.JwTUtil;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @PostMapping("/login")
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

    @PostMapping("/register")
    public ResponseEntity<RegisterDTO> register(@RequestBody @Valid RegisterDTO user){
        RegisterDTO registerDTO = userService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerDTO);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Users> delete(@PathVariable String id){
        try{
            userService.delete(id);
        }catch(NotFoundException e){
            e.getMessage();
            return new ResponseEntity(("User Not Found"),HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<Users> patchUser(@PathVariable String id, @RequestBody Map<Object, Object> objectMap){
        try{
            userService.patchUser(id, objectMap);
        }catch(NotFoundException e){
            return new ResponseEntity("User Not Found",HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}