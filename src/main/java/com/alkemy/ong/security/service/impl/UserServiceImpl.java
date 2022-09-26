package com.alkemy.ong.security.service.impl;

import com.alkemy.ong.entity.Users;
import com.alkemy.ong.repository.UsersRepository;
import com.alkemy.ong.security.dto.RegisterDTO;
import com.alkemy.ong.security.mapper.UserMapper;
import com.alkemy.ong.security.service.UserService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UserMapper userMapper;

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
        Users usersSave = usersRepository.save(newUsers);
        RegisterDTO registerDTO = userMapper.userEntity2DTO(usersSave);
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
}
