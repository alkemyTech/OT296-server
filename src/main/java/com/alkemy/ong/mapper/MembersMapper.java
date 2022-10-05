package com.alkemy.ong.mapper;

import com.alkemy.ong.dto.MembersDTO;
import com.alkemy.ong.dto.MembersDTO2;
import com.alkemy.ong.entity.Members;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MembersMapper {

    public MembersDTO menbersEntity2DTO(Members members) {

        MembersDTO membersDTO = new MembersDTO();
        membersDTO.setName(members.getName());
        membersDTO.setFacebookUrl(members.getFacebookUrl());
        membersDTO.setInstagramUrl(members.getInstagramUrl());
        membersDTO.setLinkedinUrl(members.getLinkedinUrl());
        membersDTO.setImage(members.getImage());
        membersDTO.setDescription(members.getDescription());
        membersDTO.setTimestamps(members.getTimestamps());

        return membersDTO;
    }

    public List<MembersDTO> membersEntityList2DTO(List<Members> membersList) {
        List<MembersDTO> membersDTOList = new ArrayList<>();
        for (Members members : membersList) {
            membersDTOList.add(this.menbersEntity2DTO(members));
        }

        return membersDTOList;
    }
    public Members membersDto2Entity(MembersDTO membersDTO){
        Members members= new Members();
        members.setName(membersDTO.getName());
        members.setFacebookUrl(membersDTO.getFacebookUrl());
        members.setInstagramUrl(membersDTO.getInstagramUrl());
        members.setLinkedinUrl(membersDTO.getLinkedinUrl());
        members.setImage(membersDTO.getImage());
        members.setDescription(membersDTO.getDescription());
        members.setTimestamps(membersDTO.getTimestamps());
        return members;
    }

    public Members membersEntity2DTO2(MembersDTO2 membersDTO2) {

        Members members = new Members();
        members.setName(membersDTO2.getName());

        return members;
    }

    public MembersDTO2 membersDTO2Entity(Members members) {

        MembersDTO2 membersDTO2 = new MembersDTO2();
        membersDTO2.setName(members.getName());

        return membersDTO2;
    }

    // ---------- Entity Page to DTO LIST ------------
    public List<MembersDTO> membersEntityPageDTOList(Page<Members> members){
        List<MembersDTO> membersDTO = new ArrayList<>();
        for(Members member : members){
            membersDTO.add(this.menbersEntity2DTO(member));
        }
        return membersDTO;
    }
}