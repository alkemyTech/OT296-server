package com.alkemy.ong.security.controller;

import com.alkemy.ong.entity.Users;
import com.alkemy.ong.security.dto.LoginDTO;
import com.alkemy.ong.security.dto.RegisterDTO;
import com.alkemy.ong.security.dto.UserWithoutPassDTO;
import com.alkemy.ong.security.service.UserService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class UserRestController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginDTO> login(@RequestBody @Valid LoginDTO loginDTO) throws Exception {
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (BadCredentialsException e) {
            return new ResponseEntity(("User or Password incorrect ok: false"), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(loginDTO);
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

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserWithoutPassDTO>> findAll(){
        List<UserWithoutPassDTO> usuarios = userService.findAllUsers();
        return ResponseEntity.ok(usuarios);
    }
}
