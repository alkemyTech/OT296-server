package com.alkemy.ong.service.implement;

import com.alkemy.ong.dto.MembersDTO;
import com.alkemy.ong.dto.MembersDTO2;
import com.alkemy.ong.entity.Members;
import com.alkemy.ong.mapper.MembersMapper;
import com.alkemy.ong.repository.MembersRepository;
import com.alkemy.ong.service.MembersService;
import javassist.NotFoundException;
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

    @Override
    public void deleteMembers(String id) throws NotFoundException {
        Members members= membersRepository.findById(id).orElse(null);
        if (members == null){
            throw new NotFoundException("Members not found");
        }
        membersRepository.deleteById(id);
    }
    @Override
    public MembersDTO updateMembers(String id, MembersDTO membersDTO) throws NotFoundException {
        Members members= membersRepository.findById(id).orElse(null);
        if (members == null){
            throw new NotFoundException("Members not found");
        }
        members.setFacebookUrl(membersDTO.getFacebookUrl());
        members.setInstagramUrl(membersDTO.getInstagramUrl());
        members.setLinkedinUrl(membersDTO.getLinkedinUrl());
        members.setImage(membersDTO.getImage());
        members.setDescription(membersDTO.getDescription());
        members.setTimestamps(membersDTO.getTimestamps());
        Members saveMembers=membersRepository.save(members);
        return membersMapper.menbersEntity2DTO(saveMembers);
    }
    
    @Override
    public MembersDTO2 createMembers(MembersDTO2 membersDTO2) {
        Members members = membersMapper.membersEntity2DTO2(membersDTO2);
        Members membersSave = membersRepository.save(members);

        return membersMapper.membersDTO2Entity(membersSave);
    }
}