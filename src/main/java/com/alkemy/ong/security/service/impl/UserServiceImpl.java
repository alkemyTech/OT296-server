package com.alkemy.ong.security.service.impl;

import com.alkemy.ong.dto.UserDTO;
import com.alkemy.ong.entity.Role;
import com.alkemy.ong.entity.Users;
import com.alkemy.ong.repository.RoleRepository;
import com.alkemy.ong.repository.UsersRepository;
import com.alkemy.ong.security.dto.RegisterDTO;
import com.alkemy.ong.security.dto.UserWithoutPassDTO;
import com.alkemy.ong.security.mapper.UserMapper;
import com.alkemy.ong.security.mapper.UserWithJWTMapper;
import com.alkemy.ong.security.mapper.UserWithoutPassMapper;
import com.alkemy.ong.security.service.UserService;
import com.alkemy.ong.security.util.JwTUtil;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserWithoutPassMapper userWithoutPassMapper;

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwTUtil jwTUtil;
    
    @Autowired
    private UserWithJWTMapper userWithJWTMapper;

    @Override
    public UserDetails loadUserByUsername(String emailOrPassword) throws UsernameNotFoundException {
        Users user = usersRepository.findByEmailOrPassword(emailOrPassword, emailOrPassword);
        if(user == null){
            throw new UsernameNotFoundException("ok: false");
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));

        return new org.springframework.security.core.userdetails
                .User(user.getEmail(), user.getPassword(), authorities);
    }

    @Override
    public RegisterDTO create(RegisterDTO user) {
        Users newUsers = userMapper.userDTO2Entity(user);
        if (user.getEmail().contains("admin")) {
            Role roles = roleRepository.findByName("ROLE_ADMIN").get();
            newUsers.setRole(roles);
        }else {
            Role roles = roleRepository.findByName("ROLE_USER").get();
            newUsers.setRole(roles);
        }
        Users usersSave = usersRepository.save(newUsers);
        Authentication auth;
        try {
        	auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        } catch (BadCredentialsException e) {
        	throw e;
        }
        final String jwt = jwTUtil.generateToken(auth);
        RegisterDTO registerDTO = userWithJWTMapper.userEntity2DTO(usersSave, jwt);
        return registerDTO;
    }

    public void delete(String id) throws NotFoundException {
        Optional<Users> user = usersRepository.findById(id);
        if(user.isPresent()){
            usersRepository.deleteById(id);
        }else{
            throw new NotFoundException("User not found");
        }
    }

    public void patchUser(String id, Map<Object, Object> objectMap) throws NotFoundException{
        Optional<Users> user = usersRepository.findById(id);

        if(user.isPresent()){
            Users replace = user.get();
            objectMap.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Users.class, (String) key);
                field.setAccessible(true);
                ReflectionUtils.setField(field, replace, value);
            });
            usersRepository.save(replace);
        }else{
            throw  new NotFoundException("User not found");
        }
    }

    @Override
    public UserDTO meData(String userMail) {

        Optional<Users> user = usersRepository.findByEmail(userMail);

        return UserDTO.builder()
                .firstName(user.get().getFirstName())
                .lastName(user.get().getLastName())
                .email(user.get().getEmail())
                .photo(user.get().getPhoto())
                .timestamps(user.get().getTimestamps())
                .role(user.get().getRole())
                .build();
    }

    // Get all users without password
    public List<UserWithoutPassDTO> findAllUsers(){
        List<Users> usersEntities = usersRepository.findAll();
        List<UserWithoutPassDTO> usersDTO = userWithoutPassMapper.userWPEntityList2DTOList(usersEntities);
        return usersDTO;
    }
}
