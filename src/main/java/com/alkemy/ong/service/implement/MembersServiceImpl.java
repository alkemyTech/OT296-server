package com.alkemy.ong.service.implement;

import com.alkemy.ong.dto.MembersDTO;
import com.alkemy.ong.dto.MembersDTO2;
import com.alkemy.ong.dto.PagesDTO;
import com.alkemy.ong.entity.Members;
import com.alkemy.ong.mapper.MembersMapper;
import com.alkemy.ong.repository.MembersRepository;
import com.alkemy.ong.service.MembersService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MembersServiceImpl implements MembersService {

    @Autowired
    private MembersRepository membersRepository;

    @Autowired
    private MembersMapper membersMapper;

    private final int PAGE_SIZE = 10;

    @Override
    public List<MembersDTO> getAllMembers() {
        List<Members> members = membersRepository.findAll();
        List<MembersDTO> membersDTOS = membersMapper.membersEntityList2DTO(members);

        return  membersDTOS;
    }

    // ------------- GET Page of Members -------------
    @Override
    public PagesDTO<MembersDTO> getAllMembers(int page) {
        Page<Members> members = membersRepository.findAll(PageRequest.of(page, PAGE_SIZE));
        List<MembersDTO> membersDTOS = membersMapper.membersEntityPageDTOList(members);
        Page<MembersDTO> response = new PageImpl<>(
                membersDTOS,
                PageRequest.of(members.getNumber(), members.getSize()),
                members.getTotalElements()
        );
        return new PagesDTO<>(response, "localhost:8080/members/?page=");
    }

    @Override
    public String deleteMembers(String id) throws NotFoundException {
        Members members= membersRepository.findById(id).orElse(null);
        if (members == null){
            throw new NotFoundException("Members not found");
        }
        membersRepository.deleteById(id);
        return "Member deleted";
    }
    @Override
    public MembersDTO updateMembers(String id, MembersDTO membersDTO) throws NotFoundException {
        Members members= membersRepository.findById(id).orElse(null);
        if (members == null){
            throw new NotFoundException("Members not found");
        }
        members.setName(membersDTO.getName());
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