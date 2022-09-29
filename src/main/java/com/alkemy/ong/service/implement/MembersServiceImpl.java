package com.alkemy.ong.service.implement;

import com.alkemy.ong.dto.MembersDTO;
import com.alkemy.ong.entity.Members;
import com.alkemy.ong.mapper.MembersMapper;
import com.alkemy.ong.repository.MembersRepository;
import com.alkemy.ong.service.MembersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MembersServiceImpl implements MembersService {

    @Autowired
    private MembersRepository membersRepository;

    @Autowired
    private MembersMapper membersMapper;

    @Override
    public List<MembersDTO> getAllMembers() {
        List<Members> members = membersRepository.findAll();
        List<MembersDTO> membersDTOS = membersMapper.membersEntityList2DTO(members);

        return  membersDTOS;
    }
}