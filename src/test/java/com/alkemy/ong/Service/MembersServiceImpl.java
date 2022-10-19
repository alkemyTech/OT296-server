package com.alkemy.ong.Service;

import com.alkemy.ong.dto.MembersDTO;
import com.alkemy.ong.dto.MembersDTO2;
import com.alkemy.ong.dto.PagesDTO;
import com.alkemy.ong.entity.Members;
import com.alkemy.ong.mapper.MembersMapper;
import com.alkemy.ong.repository.MembersRepository;
import com.alkemy.ong.service.implement.MembersServiceImpl;
import javassist.NotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {MembersServiceImpl.class})
@ExtendWith(MockitoExtension.class)
class MembersServiceImplTest {

    @Mock
    MembersRepository membersRepository;

    @InjectMocks
    private MembersServiceImpl membersServiceImpl;

    @Mock
    private MembersMapper membersMapper;

    private Members member;

    private MembersDTO2 membersDto2;



    @BeforeEach
    void SetUp(){


        member = Members.builder()
                .id("1")
                .name("name")
                .description("description")
                .facebookUrl("facebookUrl")
                .instagramUrl("instagramUrl")
                .linkedinUrl("linkedinUrl")
                .build();

    }


    @Test
    @DisplayName("get members")
    void getAllMembers() {
        MembersDTO membersDTO = new MembersDTO();
        membersDTO.setName("name1");
        membersDTO.setDescription("description1");
        membersDTO.setLinkedinUrl("linkedinUrl1");
        membersDTO.setInstagramUrl("instagramUrl1");
        membersDTO.setLinkedinUrl("linkedinUrl1");
        membersDTO.setFacebookUrl("facebookUrl");
        given(membersRepository.findAll()).willReturn(List.of(member));
        given(membersMapper.membersEntityList2DTO(List.of(member))).willReturn(List.of(membersDTO));
        List<MembersDTO> membersDTOS = membersServiceImpl.getAllMembers();
        assertThat(membersDTOS).isNotNull();
        assertThat(membersDTOS.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("get members page")
    void getAllMembersPage() {
        ArrayList<Members> members = new ArrayList<>();
        members.add(new Members());
        members.add(new Members());
        members.add(new Members());
        List<MembersDTO> membersDto = new ArrayList<>();

        Pageable pageable = PageRequest.of(0,10, Sort.unsorted());
        Page<Members> page = new PageImpl<>(members,pageable,members.size());

        when(membersRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(membersMapper.membersEntityPageDTOList(page)).thenReturn(membersDto);
        PagesDTO<MembersDTO> membersPagesDTO2 = membersServiceImpl.getAllMembers(0);

        assertThat(membersPagesDTO2).isNotNull();
        assertThat(members.size()).isEqualTo(3);
        verify(membersMapper,times(1)).membersEntityPageDTOList(any());
    }

    @Test
    @DisplayName("Valid Case delete")
    void deleteMembers() throws NotFoundException {
        given(membersRepository.findById(member.getId())).willReturn(Optional.of(member));
        willDoNothing().given(membersRepository).deleteById(member.getId());
        membersServiceImpl.deleteMembers(member.getId());
        verify(membersRepository,times(1)).deleteById(member.getId());
    }

    @Test
    @DisplayName("Member NotFound delete")
    void deleteMembersNotFound()  {
        given(membersRepository.findById(member.getId())).willReturn(Optional.empty());
        assertThatThrownBy(()-> membersServiceImpl.deleteMembers(member.getId()))
                .isInstanceOf(NotFoundException.class);
        verify(membersRepository,never()).deleteById(member.getId());
    }

    @Test
    @DisplayName("Valid Case update")
    void updateMembers() throws NotFoundException {
        MembersDTO membersDTO = new MembersDTO();
        membersDTO.setName("name1");
        membersDTO.setDescription("description1");
        membersDTO.setFacebookUrl("facebookUrl1");
        membersDTO.setLinkedinUrl("linkedinUrl1");
        membersDTO.setInstagramUrl("instagramUrl1");
        given(membersRepository.findById("1")).willReturn(Optional.of(member));
        membersServiceImpl.updateMembers("1",membersDTO);
        assertThat(member.getName()).isEqualTo("name1");
        verify(membersRepository,times(1)).findById("1");
    }

    @Test
    @DisplayName("Member NotFound update")
    void updateMembersNotFound() throws NotFoundException {
        MembersDTO membersDTO = new MembersDTO();
        membersDTO.setName("name1");
        membersDTO.setDescription("description1");
        membersDTO.setFacebookUrl("facebookUrl1");
        membersDTO.setLinkedinUrl("linkedinUrl1");
        membersDTO.setInstagramUrl("instagramUrl1");
        given(membersRepository.findById("1")).willReturn(Optional.empty());
        assertThatThrownBy(()-> membersServiceImpl.updateMembers("1",membersDTO))
                .isInstanceOf(NotFoundException.class);
        verify(membersRepository,never()).save(any());
    }


    @Test
    void createMembers() {
        membersDto2 = new MembersDTO2();
        membersDto2.setName(member.getName());
        given(membersMapper.membersEntity2DTO2(membersDto2)).willReturn(member);
        given(membersMapper.membersDTO2Entity(member)).willReturn(membersDto2);
        given(membersRepository.save(member)).willReturn(member);
        MembersDTO2 membersDTO2 = membersServiceImpl.createMembers(membersDto2);
        assertEquals(membersDTO2.getName(), member.getName());
        verify(membersMapper,times(1)).membersEntity2DTO2(membersDto2);
        verify(membersMapper,times(1)).membersDTO2Entity(member);

    }

}