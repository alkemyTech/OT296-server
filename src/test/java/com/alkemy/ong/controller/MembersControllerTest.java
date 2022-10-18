package com.alkemy.ong.controller;

import com.alkemy.ong.dto.MembersDTO;
import com.alkemy.ong.dto.MembersDTO2;
import com.alkemy.ong.dto.PagesDTO;
import com.alkemy.ong.security.configuration.SecurityConfiguration;
import com.alkemy.ong.security.service.impl.UserServiceImpl;
import com.alkemy.ong.security.util.JwTUtil;
import com.alkemy.ong.service.MembersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import javassist.NotFoundException;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(MembersController.class)
@Import({SecurityConfiguration.class, BCryptPasswordEncoder.class})
class MembersControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    MembersService membersService;

    @MockBean
    UserServiceImpl userServiceImpl;

    @MockBean
    JwTUtil jwTUtil;

    @Autowired
    ObjectMapper jsonMapper;

    @BeforeEach
    public void settings() {
        this.jsonMapper = new ObjectMapper();
    }

    static List<MembersDTO> generateListDTO(){
        MembersDTO membersDTO = new MembersDTO();
        membersDTO.setDescription("description");
        membersDTO.setImage("image");
        membersDTO.setName("name");
        membersDTO.setFacebookUrl("facebookUrl");
        membersDTO.setInstagramUrl("instagramUrl");
        membersDTO.setLinkedinUrl("linkedinUrl");
        return Collections.singletonList(membersDTO);
    }

    static MembersDTO2  generateMemberDTO2(){
        MembersDTO2 membersDTO2 = new MembersDTO2();
        membersDTO2.setName("name");
        return membersDTO2;
    }

    static MembersDTO2  generateMemberDTO2NameInvalid(){
        MembersDTO2 membersDTO2 = new MembersDTO2();
        membersDTO2.setName("name123");
        return membersDTO2;
    }

    static List<MembersDTO2>  generateMemberDTO2ServerError(){
        List<MembersDTO2> membersDTO2 = new ArrayList<>();
        MembersDTO2 membersDTO;
        membersDTO = generateMemberDTO2();
        membersDTO.setName(null);
        return membersDTO2;
    }

    static List<MembersDTO>  generateMemberDTOServerError(){
        List<MembersDTO> membersDTO = new ArrayList<>();
        MembersDTO membersDTO1;
        membersDTO1 = generateListDTO().get(0);
        membersDTO1.setName(null);
        return membersDTO;
    }


    /*============================================================*/
    /*                           GET-TEST                        */
    /*============================================================*/

    @Test
    @DisplayName("Valid Case getPage")
    @WithMockUser(username = "gabriel@gmail.com",authorities = "ROLE_ADMIN")
    public void getPageableMembersTestOk() throws Exception {
        Pageable pageable = PageRequest.of(0,10, Sort.unsorted());
        List<MembersDTO> membersDTOS = MembersControllerTest.generateListDTO();
        Page<MembersDTO> page = new PageImpl<>(membersDTOS,pageable,membersDTOS.size());
        PagesDTO<MembersDTO> dtoPage = new PagesDTO<>(page,"previousPage","nextPage");
        Mockito.when(membersService.getAllMembers(0)).thenReturn(dtoPage);
        mvc.perform(MockMvcRequestBuilders.get("/members").param("page","0").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());
        Mockito.verify(membersService).getAllMembers(0);
    }

    @Test
    @DisplayName("Forbidden Case getPage")
    public void getPageableMembersTestForbidden() throws Exception {
        Mockito.when(jwTUtil.isBearer(Mockito.any())).thenReturn(false);
        Pageable pageable = PageRequest.of(0,10, Sort.unsorted());
        List<MembersDTO> membersDTOS = MembersControllerTest.generateListDTO();
        Page<MembersDTO> page = new PageImpl<>(membersDTOS,pageable,membersDTOS.size());
        PagesDTO<MembersDTO> dtoPage = new PagesDTO<>(page,"previousPage","nextPage");
        Mockito.when(membersService.getAllMembers(0)).thenReturn(dtoPage);
        mvc.perform(MockMvcRequestBuilders.get("/members").param("page","0").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(membersService,Mockito.never()).getAllMembers(0);
    }

    @Test
    @DisplayName("Invalid Token Case getPage")
    public void getPageableMembersTestTokenInvalid() throws Exception {
        Mockito.when(jwTUtil.isBearer(Mockito.any())).thenReturn(true);
        Mockito.when(jwTUtil.validateToken(Mockito.any(),Mockito.any())).thenReturn(false);
        Pageable pageable = PageRequest.of(0,10, Sort.unsorted());
        List<MembersDTO> membersDTOS = MembersControllerTest.generateListDTO();
        Page<MembersDTO> page = new PageImpl<>(membersDTOS,pageable,membersDTOS.size());
        PagesDTO<MembersDTO> dtoPage = new PagesDTO<>(page,"previousPage","nextPage");
        Mockito.when(membersService.getAllMembers(0)).thenReturn(dtoPage);
        mvc.perform(MockMvcRequestBuilders.get("/members").param("page","0").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(membersService,Mockito.never()).getAllMembers(0);
    }


    @Test
    @DisplayName("Valid Case getAll")
    @WithMockUser(username = "gabriel@gmail.com",authorities = "ROLE_ADMIN")
    public void getPageableMembersAllTestOk() throws Exception {
        List<MembersDTO> membersDTOS = MembersControllerTest.generateListDTO();
        Mockito.when(membersService.getAllMembers()).thenReturn(membersDTOS);
        mvc.perform(MockMvcRequestBuilders.get("/members"))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(membersService).getAllMembers();
    }

    @Test
    @DisplayName("Forbidden getAll")
    public void getPageableMembersAllTestForbidden() throws Exception {
        Mockito.when(jwTUtil.isBearer(Mockito.any())).thenReturn(false);
        List<MembersDTO> membersDTOS = MembersControllerTest.generateListDTO();
        Mockito.when(membersService.getAllMembers()).thenReturn(membersDTOS);
        mvc.perform(MockMvcRequestBuilders.get("/members"))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(membersService,Mockito.never()).getAllMembers();
    }

    @Test
    @DisplayName("Invalid Token getAll")
    public void getPageableMembersAllTestInvalidToken() throws Exception {
        Mockito.when(jwTUtil.isBearer(Mockito.any())).thenReturn(true);
        Mockito.when(jwTUtil.validateToken(Mockito.any(),Mockito.any())).thenReturn(false);
        List<MembersDTO> membersDTOS = MembersControllerTest.generateListDTO();
        Mockito.when(membersService.getAllMembers()).thenReturn(membersDTOS);
        mvc.perform(MockMvcRequestBuilders.get("/members"))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(membersService,Mockito.never()).getAllMembers();
    }


    /*============================================================*/
    /*                           POST-TEST                        */
    /*============================================================*/

        @Test
        @DisplayName("Valid Case post")
        @WithMockUser(username = "gabriel@gmail.com", authorities = "ROLE_ADMIN")
        public void createMembersTestCreated() throws Exception {
            MembersDTO2 membersDTO2 = MembersControllerTest.generateMemberDTO2();
            Mockito.when(membersService.createMembers(Mockito.any(MembersDTO2.class))).thenReturn(membersDTO2);
            mvc.perform(MockMvcRequestBuilders.post("/members")
                            .content(jsonMapper.writeValueAsString(membersDTO2))
                            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.content().json(jsonMapper.writeValueAsString(membersDTO2)))
                    .andDo(MockMvcResultHandlers.print());

            Mockito.verify(membersService).createMembers(Mockito.any());
        }

    @Test
    @DisplayName("Server Error post")
    @WithMockUser(username = "gabriel@gmail.com", authorities = "ROLE_ADMIN")
    public void createMembersTestServerError() throws Exception {
        MembersDTO2 membersDTO2expected = MembersControllerTest.generateMemberDTO2();
        Mockito.when(membersService.createMembers(Mockito.any())).thenReturn(membersDTO2expected);
        List<MembersDTO2> membersDTO2request = MembersControllerTest.generateMemberDTO2ServerError();
        mvc.perform(MockMvcRequestBuilders.post("/members")
                        .content(jsonMapper.writeValueAsString(membersDTO2request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(membersService,Mockito.never()).createMembers(Mockito.any());
    }

    @Test
    @DisplayName("Role Invalid post")
    @WithMockUser(username = "gabriel@gmail.com", authorities = "ROLE_USER")
    public void createMembersTestRoleInvalid() throws Exception {
        MembersDTO2 membersDTO2 = MembersControllerTest.generateMemberDTO2();
        Mockito.when(membersService.createMembers(Mockito.any())).thenReturn(membersDTO2);
        mvc.perform(MockMvcRequestBuilders.post("/members")
                        .content(jsonMapper.writeValueAsString(membersDTO2))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(membersService,Mockito.never()).createMembers(Mockito.any());
    }

    @Test
    @DisplayName("BadRequest Case post")
    @WithMockUser(username = "gabriel@gmail.com", authorities = "ROLE_ADMIN")
    public void createMembersTestBadRequest() throws Exception {
        MembersDTO2 membersDTO2 = MembersControllerTest.generateMemberDTO2NameInvalid();
        Mockito.when(membersService.createMembers(Mockito.any())).thenReturn(membersDTO2);
        mvc.perform(MockMvcRequestBuilders.post("/members")
                        .content(jsonMapper.writeValueAsString(membersDTO2))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(membersService,Mockito.never()).createMembers(Mockito.any());
    }

    @Test
    @DisplayName("Forbidden Case post")
    public void createMembersTestForbidden() throws Exception {
        Mockito.when(jwTUtil.isBearer(Mockito.any())).thenReturn(false);
        MembersDTO2 membersDTO2 = MembersControllerTest.generateMemberDTO2();
        Mockito.when(membersService.createMembers(Mockito.any())).thenReturn(membersDTO2);
        mvc.perform(MockMvcRequestBuilders.post("/members")
                        .content(jsonMapper.writeValueAsString(membersDTO2))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(membersService,Mockito.never()).createMembers(Mockito.any());
    }

    @Test
    @DisplayName("Invalid Token Case post")
    public void createMembersTestTokenInvalid() throws Exception {
        Mockito.when(jwTUtil.isBearer(Mockito.any())).thenReturn(true);
        Mockito.when(jwTUtil.validateToken(Mockito.any(),Mockito.any())).thenReturn(false);
        MembersDTO2 membersDTO2 = MembersControllerTest.generateMemberDTO2();
        Mockito.when(membersService.createMembers(Mockito.any())).thenReturn(membersDTO2);
        mvc.perform(MockMvcRequestBuilders.post("/members")
                        .content(jsonMapper.writeValueAsString(membersDTO2))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(membersService,Mockito.never()).createMembers(Mockito.any());
    }


    /*============================================================*/
    /*                           PUT-TEST                        */
    /*============================================================*/

    @Test
    @DisplayName("Valid Case put")
    @WithMockUser(username = "gabriel@gmail.com", authorities = "ROLE_ADMIN")
    public void updateMembersTestOk() throws Exception {
        String id = "id123";
        MembersDTO membersDTO = MembersControllerTest.generateListDTO().get(0);
        Mockito.when(membersService.updateMembers(Mockito.any(),Mockito.any())).thenReturn(membersDTO);
        mvc.perform(MockMvcRequestBuilders.put("/members/{id}",id)
                .content(jsonMapper.writeValueAsString(membersDTO))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(jsonMapper.writeValueAsString(membersDTO)))
                .andExpect(status().isAccepted())
                .andDo(MockMvcResultHandlers.print());
        Mockito.verify(membersService).updateMembers(Mockito.any(),Mockito.any());
    }

    @Test
    @DisplayName("Id Not Found Case put")
    @WithMockUser(username = "gabriel@gmail.com", authorities = "ROLE_ADMIN")
    public void updateMembersTestRoleNotFound() throws Exception {
        String id = "id123";
        MembersDTO membersDTO = MembersControllerTest.generateListDTO().get(0);
        Mockito.when(membersService.updateMembers(Mockito.any(),Mockito.any())).thenThrow(new NotFoundException("Member Not found"));
        mvc.perform(MockMvcRequestBuilders.put("/members/{id}",id)
                        .content(jsonMapper.writeValueAsString(membersDTO))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(membersService).updateMembers(Mockito.any(),Mockito.any());
    }

    @Test
    @DisplayName("Invalid Role Case put")
    @WithMockUser(username = "gabriel@gmail.com", authorities = "ROLE_USER")
    public void updateMembersTestRoleInvalid() throws Exception {
        String id = "id123";
        MembersDTO membersDTO = MembersControllerTest.generateListDTO().get(0);
        Mockito.when(membersService.updateMembers(Mockito.any(),Mockito.any())).thenReturn(membersDTO);
        mvc.perform(MockMvcRequestBuilders.put("/members/{id}",id)
                        .content(jsonMapper.writeValueAsString(membersDTO))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(membersService,Mockito.never()).updateMembers(Mockito.any(),Mockito.any());
    }

    @Test
    @DisplayName("Forbidden Case put")
    public void updateMembersTestForbidden() throws Exception {
        Mockito.when(jwTUtil.isBearer(Mockito.any())).thenReturn(false);
        String id = "id123";
        MembersDTO membersDTO = MembersControllerTest.generateListDTO().get(0);
        Mockito.when(membersService.updateMembers(Mockito.any(),Mockito.any())).thenReturn(membersDTO);
        mvc.perform(MockMvcRequestBuilders.put("/members/{id}",id)
                        .content(jsonMapper.writeValueAsString(membersDTO))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(membersService,Mockito.never()).updateMembers(Mockito.any(),Mockito.any());
    }

    @Test
    @DisplayName("Invalid Token Case put")
    public void updateMembersTestTokenInvalid() throws Exception {
        Mockito.when(jwTUtil.isBearer(Mockito.any())).thenReturn(true);
        Mockito.when(jwTUtil.validateToken(Mockito.any(),Mockito.any())).thenReturn(false);
        String id = "id123";
        MembersDTO membersDTO = MembersControllerTest.generateListDTO().get(0);
        Mockito.when(membersService.updateMembers(Mockito.any(),Mockito.any())).thenReturn(membersDTO);
        mvc.perform(MockMvcRequestBuilders.put("/members/{id}",id)
                        .content(jsonMapper.writeValueAsString(membersDTO))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(membersService,Mockito.never()).updateMembers(Mockito.any(),Mockito.any());
    }

    @Test
    @DisplayName("Server Error put")
    @WithMockUser(username = "gabriel@gmail.com", authorities = "ROLE_ADMIN")
    public void updateMembersTestServerError() throws Exception {
        String id = "id123";
        MembersDTO membersDTOExpected = MembersControllerTest.generateListDTO().get(0);
        Mockito.when(membersService.updateMembers(Mockito.any(), Mockito.any())).thenReturn(membersDTOExpected);
        List<MembersDTO> membersDTORequest = MembersControllerTest.generateMemberDTOServerError();
        mvc.perform(MockMvcRequestBuilders.put("/members/{id}",id)
                        .content(jsonMapper.writeValueAsString(membersDTORequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(membersService, Mockito.never()).updateMembers(Mockito.any(), Mockito.any());
    }

    /*============================================================*/
    /*                           DELETE-TEST                        */
    /*============================================================*/

    @Test
    @DisplayName("Valid Case delete")
    @WithMockUser(username = "gabriel@gmail.com", authorities = "ROLE_ADMIN")
    public void deleteMembersTestOk() throws Exception {
        String id = "id123";
        String Response = "Member deleted";
        Mockito.when(membersService.deleteMembers(Mockito.any())).thenReturn(Response);
        mvc.perform(MockMvcRequestBuilders.delete("/members/{id}",id))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Response))
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(membersService).deleteMembers(Mockito.any());
    }

    @Test
    @DisplayName("Id Not Found Case delete")
    @WithMockUser(username = "gabriel@gmail.com", authorities = "ROLE_ADMIN")
    public void deleteMembersTestNotFound() throws Exception {
        String id = "id123";
        String Response = "Member not found";
        Mockito.when(membersService.deleteMembers(Mockito.any())).thenThrow(new NotFoundException("Member not found"));
        mvc.perform(MockMvcRequestBuilders.delete("/members/{id}",id))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(Response))
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(membersService).deleteMembers(Mockito.any());
    }

    @Test
    @DisplayName("Role Invalid Case delete")
    @WithMockUser(username = "gabriel@gmail.com", authorities = "ROLE_USER")
    public void deleteMembersTestInvalidRole() throws Exception {
        String id = "id123";
        String Response = "Member deleted";
        Mockito.when(membersService.deleteMembers(Mockito.any())).thenReturn(Response);
        mvc.perform(MockMvcRequestBuilders.delete("/members/{id}",id))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(membersService,Mockito.never()).deleteMembers(Mockito.any());
    }

    @Test
    @DisplayName("Forbidden Case delete")
    public void deleteMembersTestForbidden() throws Exception {
        Mockito.when(jwTUtil.isBearer(Mockito.any())).thenReturn(false);
        String id = "id123";
        String Response = "Member deleted";
        Mockito.when(membersService.deleteMembers(Mockito.any())).thenReturn(Response);
        mvc.perform(MockMvcRequestBuilders.delete("/members/{id}",id))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(membersService,Mockito.never()).deleteMembers(Mockito.any());
    }

    @Test
    @DisplayName("Token Invalid Case delete")
    public void deleteMembersTestInvalidToken() throws Exception {
        Mockito.when(jwTUtil.isBearer(Mockito.any())).thenReturn(true);
        Mockito.when(jwTUtil.validateToken(Mockito.any(),Mockito.any())).thenReturn(false);
        String id = "id123";
        String Response = "Member deleted";
        Mockito.when(membersService.deleteMembers(Mockito.any())).thenReturn(Response);
        mvc.perform(MockMvcRequestBuilders.delete("/members/{id}",id))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(membersService,Mockito.never()).deleteMembers(Mockito.any());
    }

}