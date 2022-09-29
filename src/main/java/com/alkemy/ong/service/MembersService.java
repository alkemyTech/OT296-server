package com.alkemy.ong.service;

import com.alkemy.ong.dto.MembersDTO;
import javassist.NotFoundException;

import java.util.List;

public interface MembersService {

    List<MembersDTO> getAllMembers();

    void deleteMembers(String id) throws NotFoundException;
}
