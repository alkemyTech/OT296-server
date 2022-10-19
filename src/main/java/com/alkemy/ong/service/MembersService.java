package com.alkemy.ong.service;

import com.alkemy.ong.dto.MembersDTO;
import com.alkemy.ong.dto.PagesDTO;
import javassist.NotFoundException;
import com.alkemy.ong.dto.MembersDTO2;

import java.util.List;

public interface MembersService {

    List<MembersDTO> getAllMembers();
    PagesDTO<MembersDTO> getAllMembers(int page);
    MembersDTO updateMembers(String id, MembersDTO membersDTO) throws NotFoundException;
    String deleteMembers(String id) throws NotFoundException;

    MembersDTO2 createMembers(MembersDTO2 membersDTO2);

}