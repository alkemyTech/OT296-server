package com.alkemy.ong.security.service.impl;

import com.alkemy.ong.entity.Users;
import com.alkemy.ong.repository.UsersRepository;
import com.alkemy.ong.security.dto.RegisterDTO;
import com.alkemy.ong.security.mapper.UserMapper;
import com.alkemy.ong.security.service.UserService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

    public List<RegisterDTO> findAllUsers(){
        List<Users> usersEntities = usersRepository.findAll();
        List<RegisterDTO> usersDTO = userMapper.userEntityList2DTOList(usersEntities);
        return usersDTO;
    }
}
