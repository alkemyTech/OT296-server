package com.alkemy.ong.controller;

import com.alkemy.ong.dto.OrganizationDTO;
import com.alkemy.ong.dto.OrganizationDTOPublic;
import com.alkemy.ong.dto.SlidesDTO;
import com.alkemy.ong.security.service.impl.UserServiceImpl;
import com.alkemy.ong.security.util.JwTUtil;
import com.alkemy.ong.service.OrganizationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrganizationController.class)
class OrganizationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    OrganizationService organizationService;

    @MockBean
    UserServiceImpl userService;

    @MockBean
    JwTUtil jwTUtil;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setting(){
        this.objectMapper = new ObjectMapper();
    }

    static OrganizationDTO generateOrganizationDTO(){
        OrganizationDTO orgDTO = new OrganizationDTO();
        orgDTO.setName("org1");
        orgDTO.setImage("image1");
        orgDTO.setAddress("address1");
        orgDTO.setPhone(1234);
        orgDTO.setEmail("email1@email.com");
        orgDTO.setUrlFacebook("facebook1");
        orgDTO.setUrlLinkedin("linkedin1");
        orgDTO.setUrlInstagram("instagram1");
        orgDTO.setWelcomeText("welcome1");
        orgDTO.setAboutUsText("about1");
        return orgDTO;
    }
    static List<SlidesDTO> generateSlidesDTO(){
        SlidesDTO slidesDTO = new SlidesDTO();
        slidesDTO.setImageURL("image1");
        slidesDTO.setText("text1");
        slidesDTO.setOrder(1);
        slidesDTO.setOrganizationID("1");
        return Collections.singletonList(slidesDTO);
    }
    static List<OrganizationDTOPublic> generateOrgDTOPublic(){
        OrganizationDTOPublic organizationDTOPublic = new OrganizationDTOPublic();
        organizationDTOPublic.setName("org1");
        organizationDTOPublic.setImage("image1");
        organizationDTOPublic.setAddress("address1");
        organizationDTOPublic.setPhone(1234);
        organizationDTOPublic.setUrlFacebook("face1");
        organizationDTOPublic.setUrlInstagram("instagram1");
        organizationDTOPublic.setUrlLinkedin("linkedin1");
        organizationDTOPublic.setSlides(OrganizationControllerTest.generateSlidesDTO());
        return Collections.singletonList(organizationDTOPublic);
    }

    /*============================================================*/
    /*                           POST-TEST                        */
    /*============================================================*/

    @Test
    @DisplayName("Test Post Ong status .OK")
    @WithMockUser(username = "flaambroggio@admin.com", authorities = "ROLE_ADMIN")
    void createIsOk() throws Exception {

        OrganizationDTO dto = OrganizationControllerTest.generateOrganizationDTO();

        when(organizationService.create(Mockito.any(OrganizationDTO.class))).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.post("/organization/public")
                    .content(objectMapper.writeValueAsString(dto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(dto)))
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(organizationService).create(Mockito.any());
    }

    @Test
    @DisplayName("Test Post Ong status .FORBIDDEN invalid user")
    void createIsForbidden() throws Exception {
        when(jwTUtil.isBearer(Mockito.any())).thenReturn(false);
        OrganizationDTO dto = OrganizationControllerTest.generateOrganizationDTO();

        when(organizationService.create(Mockito.any(OrganizationDTO.class))).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.post("/organization/public")
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());
        Mockito.verify(organizationService, Mockito.never()).create(Mockito.any());
    }

    @Test
    @DisplayName("Test Post Ong status .FORBIDDEN invalid token")
    void createIsForbiddenToken() throws Exception {
        when(jwTUtil.isBearer(Mockito.any())).thenReturn(true);
        when(jwTUtil.validateToken(Mockito.any(),Mockito.any())).thenReturn(false);
        OrganizationDTO dto = OrganizationControllerTest.generateOrganizationDTO();

        when(organizationService.create(Mockito.any(OrganizationDTO.class))).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.post("/organization/public")
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
        Mockito.verify(organizationService, Mockito.never()).create(Mockito.any());
    }

    @Test
    @DisplayName("Test Post Ong status .INTERNAL_SERVER_ERROR")
    @WithMockUser(username = "flaambroggio@admin.com", authorities = "ROLE_ADMIN")
    void createIsServerError() throws Exception {
        when(organizationService.create(Mockito.any())).thenThrow(new InternalError());

        mockMvc.perform(MockMvcRequestBuilders.post("/organization/public")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
        Mockito.verify(organizationService, Mockito.never()).create(Mockito.any());
    }

    /*============================================================*/
    /*                           PATCH-TEST                       */
    /*============================================================*/

    @Test
    @DisplayName("Test Patch Ong status .OK")
    @WithMockUser(username = "flaambroggio@admin.com", authorities = "ROLE_ADMIN")
    void patchOrganizationIsOk() throws Exception {
        String id = "123";
        Map<Object, Object> objectMap = new HashMap<>();
        objectMap.put("name", "nameUpdated");

        mockMvc.perform(MockMvcRequestBuilders.patch("/organization/public" + "/{id}", id)
                        .content(objectMapper.writeValueAsString(objectMap))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        Mockito.verify(organizationService,Mockito.times(1)).patchOrganization(id, objectMap);
    }

    @Test
    @DisplayName("Test Patch Ong status .FORBIDDEN invalid user")
    void patchOrganizationIsForbidden() throws Exception {
        when(jwTUtil.isBearer(Mockito.any())).thenReturn(false);
        String id = "123";
        Map<Object, Object> objectMap = new HashMap<>();
        objectMap.put("name", "nameUpdated");

        mockMvc.perform(MockMvcRequestBuilders.patch("/organization/public" + "/{id}", id)
                        .content(objectMapper.writeValueAsString(objectMap))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        Mockito.verify(organizationService,Mockito.never()).patchOrganization(id, objectMap);
    }

    @Test
    @DisplayName("Test Patch Ong status .FORBIDDEN invalid token")
    void patchOrganizationIsForbiddenToken() throws Exception {
        when(jwTUtil.isBearer(Mockito.any())).thenReturn(true);
        when(jwTUtil.validateToken(Mockito.any(),Mockito.any())).thenReturn(false);
        String id = "123";
        Map<Object, Object> objectMap = new HashMap<>();
        objectMap.put("name", "nameUpdated");

        mockMvc.perform(MockMvcRequestBuilders.patch("/organization/public" + "/{id}", id)
                        .content(objectMapper.writeValueAsString(objectMap))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        Mockito.verify(organizationService,Mockito.never()).patchOrganization(id, objectMap);
    }

    @Test
    @DisplayName("Test Patch Ong status .NOT_FOUND")
    @WithMockUser(username = "flaambroggio@admin.com", authorities = "ROLE_ADMIN")
    void patchOrganizationIsNotFound() throws Exception {
        String id = "123";
        Map<Object, Object> objectMap = new HashMap<>();
        objectMap.put("name", "nameUpdated");

        mockMvc.perform(MockMvcRequestBuilders.patch("/organization/public" + "/{id}", id)
                        .content(objectMapper.writeValueAsString(objectMap))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        Mockito.verify(organizationService,Mockito.never()).patchOrganization(Mockito.any(),Mockito.any());
    }

    @Test
    @DisplayName("Test Patch Ong status .INTERNAL_SERVER_ERROR")
    @WithMockUser(username = "flaambroggio@admin.com", authorities = "ROLE_ADMIN")
    void patchOrganizationIsInternalServerError() throws Exception {
        String id = "123";
        Map<Object, Object> objectMap = new HashMap<>();
        objectMap.put("name", "nameUpdated");

        mockMvc.perform(MockMvcRequestBuilders.patch("/organization/public" + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        Mockito.verify(organizationService,Mockito.never()).patchOrganization(id, objectMap);
    }

    /*============================================================*/
    /*                           GET-TEST                         */
    /*============================================================*/

    @Test
    @DisplayName("Test Get Ong status .OK")
    @WithMockUser(username = "flaambroggio@admin.com", authorities = "ROLE_USER")
    void getOrganizationsIsOk() throws Exception {
        List<OrganizationDTOPublic> OrganizationDTOPublicList = OrganizationControllerTest.generateOrgDTOPublic();
        when(organizationService.getOrganizationsDTO()).thenReturn(OrganizationDTOPublicList);

        mockMvc.perform(MockMvcRequestBuilders.get("/organization/public"))
                .andExpect(status().isOk());
        Mockito.verify(organizationService).getOrganizationsDTO();
    }

    @Test
    @DisplayName("Test Get Ong status .FORBIDDEN invalid user")
    void getOrganizationsIsForbidden() throws Exception {
        when(jwTUtil.isBearer(Mockito.any())).thenReturn(true);
        when(jwTUtil.validateToken(Mockito.any(),Mockito.any())).thenReturn(false);
        List<OrganizationDTOPublic> OrganizationDTOPublicList = OrganizationControllerTest.generateOrgDTOPublic();
        when(organizationService.getOrganizationsDTO()).thenReturn(OrganizationDTOPublicList);

        mockMvc.perform(MockMvcRequestBuilders.get("/organization/public"))
                .andExpect(status().isForbidden());
        Mockito.verify(organizationService, Mockito.never()).getOrganizationsDTO();
    }

    @Test
    @DisplayName("Test Get Ong status .FORBIDDEN invalid token")
    void getOrganizationsIsForbiddenToken() throws Exception {
        when(jwTUtil.isBearer(Mockito.any())).thenReturn(false);
        List<OrganizationDTOPublic> OrganizationDTOPublicList = OrganizationControllerTest.generateOrgDTOPublic();
        when(organizationService.getOrganizationsDTO()).thenReturn(OrganizationDTOPublicList);

        mockMvc.perform(MockMvcRequestBuilders.get("/organization/public"))
                .andExpect(status().isForbidden());
        Mockito.verify(organizationService, Mockito.never()).getOrganizationsDTO();
    }

    @Test
    @DisplayName("Test Get Ong by Id status .OK")
    @WithMockUser(username = "flaambroggio@admin.com", authorities = "ROLE_USER")
    void getByIdIsOk() throws Exception {
        String id = "123";
        OrganizationDTOPublic OrganizationDTOPublic = OrganizationControllerTest.generateOrgDTOPublic().get(0);
        when(organizationService.getSlidesByIdOngOrder(Mockito.any())).thenReturn(OrganizationDTOPublic);

        mockMvc.perform(MockMvcRequestBuilders.get("/organization/public" + "/{id}", id))
                .andExpect(status().isAccepted());
        Mockito.verify(organizationService).getSlidesByIdOngOrder(Mockito.any());
    }

    @Test
    @DisplayName("Test Get Ong by Id status .NOT_FOUND")
    @WithMockUser(username = "flaambroggio@admin.com", authorities = "ROLE_USER")
    void getByIdNotFound() throws Exception {
        String id = "123";
        when(organizationService.getSlidesByIdOngOrder(Mockito.any())).thenThrow(new NotFoundException("Organization not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/organization/public" + "/{id}", id))
                .andExpect(status().isNotFound());
        Mockito.verify(organizationService).getSlidesByIdOngOrder(Mockito.any());
    }

    @Test
    @DisplayName("Test Get Ong by Id status .FORBIDDEN invalid user")
    void getByIdIsForbidden() throws Exception {
        when(jwTUtil.isBearer(Mockito.any())).thenReturn(true);
        when(jwTUtil.validateToken(Mockito.any(),Mockito.any())).thenReturn(false);
        OrganizationDTOPublic OrganizationDTOPublic = OrganizationControllerTest.generateOrgDTOPublic().get(0);
        when(organizationService.getSlidesByIdOngOrder(Mockito.any())).thenReturn(OrganizationDTOPublic);

        mockMvc.perform(MockMvcRequestBuilders.get("/organization/public".concat("/123")))
                .andExpect(status().isForbidden());
        Mockito.verify(organizationService, Mockito.never()).getSlidesByIdOngOrder(Mockito.any());
    }

    @Test
    @DisplayName("Test Get Ong by Id status .FORBIDDEN invalid token")
    void getByIdIsForbiddenToken() throws Exception {
        when(jwTUtil.isBearer(Mockito.any())).thenReturn(false);
        OrganizationDTOPublic OrganizationDTOPublic = OrganizationControllerTest.generateOrgDTOPublic().get(0);
        when(organizationService.getSlidesByIdOngOrder(Mockito.any())).thenReturn(OrganizationDTOPublic);

        mockMvc.perform(MockMvcRequestBuilders.get("/organization/public".concat("/123")))
                .andExpect(status().isForbidden());
        Mockito.verify(organizationService, Mockito.never()).getSlidesByIdOngOrder(Mockito.any());
    }
}