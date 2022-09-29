package com.alkemy.ong.mapper;

import com.alkemy.ong.dto.MembersDTO;
import com.alkemy.ong.entity.Members;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MembersMapper {

    public MembersDTO menbersEntity2DTO(Members members) {

        MembersDTO membersDTO = new MembersDTO();
        members.setName(membersDTO.getName());
        members.setFacebookUrl(membersDTO.getFacebookUrl());
        members.setInstagramUrl(membersDTO.getInstagramUrl());
        members.setLinkedinUrl(membersDTO.getLinkedinUrl());
        members.setImage(membersDTO.getImage());
        members.setDescription(membersDTO.getDescription());
        members.setTimestamps(membersDTO.getTimestamps());

        return membersDTO;
    }

    public List<MembersDTO> membersEntityList2DTO(List<Members> membersList) {
        List<MembersDTO> membersDTOList = new ArrayList<>();
        for (Members members : membersList) {
            membersDTOList.add(this.menbersEntity2DTO(members));
        }

        return membersDTOList;
    }
}
