package com.alkemy.ong.controller;

import com.alkemy.ong.dto.ActivityDTO;
import com.alkemy.ong.security.configuration.SecurityConfiguration;
import com.alkemy.ong.security.util.JwTUtil;
import com.alkemy.ong.security.service.impl.UserServiceImpl;
import com.alkemy.ong.service.ActivityService;
import com.alkemy.ong.utils.OpenAPISecurityConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static com.alkemy.ong.util.ActivityMocksUtil.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ActivityController.class)
@Import({SecurityConfiguration.class, BCryptPasswordEncoder.class, OpenAPISecurityConfiguration.class})
class ActivityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private JwTUtil jwTUtil;

    @MockBean
    private ActivityService activityService;

    @Autowired
    private ObjectMapper jsonMapper;


    @BeforeEach
    public void setting(){
        this.jsonMapper = new ObjectMapper();
    }

    @Nested
    class saveActivityTest{
        @Test
        @DisplayName("Valid case")
        @WithMockUser(username = "adminmock@gmail.com", roles = "ADMIN")
        void test1() throws Exception{
            ActivityDTO expectedResponse = generateActivityDTO();

            Mockito.when(activityService.createActivity(Mockito.any(ActivityDTO.class))).thenReturn(expectedResponse);

            mockMvc.perform(MockMvcRequestBuilders.post("/activities")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(jsonMapper.writeValueAsString(expectedResponse)))
                    .andExpect(status().isCreated())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.content().json(jsonMapper.writeValueAsString(expectedResponse)))
                    .andDo(MockMvcResultHandlers.print());

            Mockito.verify(activityService).createActivity(Mockito.any());
        }

        @Test
        @DisplayName("Invalid role")
        @WithMockUser(username = "adminmock@gmail.com", authorities = "ADMIN")
        void test3() throws  Exception{
            ActivityDTO expectedResponse = generateActivityDTO();

            Mockito.when(activityService.createActivity(Mockito.any())).thenReturn(expectedResponse);

            mockMvc.perform(MockMvcRequestBuilders.post("/activities")
                            .content(jsonMapper.writeValueAsString(expectedResponse))
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isForbidden())
                    .andDo(MockMvcResultHandlers.print());

            Mockito.verify(activityService, Mockito.never()).createActivity(Mockito.any());
        }

        @Test
        @DisplayName("Token not valid")
        void test4() throws Exception{
            Mockito.when(jwTUtil.isBearer(Mockito.any())).thenReturn(true);
            Mockito.when(jwTUtil.verify(Mockito.any())).thenThrow(new Exception());

            ActivityDTO expectedResponse = generateActivityDTO();

            Mockito.when(activityService.createActivity(Mockito.any())).thenReturn(expectedResponse);

            mockMvc.perform(MockMvcRequestBuilders.post("/activities")
                            .content(jsonMapper.writeValueAsString(expectedResponse))
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isForbidden())
                    .andDo(MockMvcResultHandlers.print());

            Mockito.verify(activityService, Mockito.never()).createActivity(Mockito.any());
        }

        @Test
        @DisplayName("Token not provided")
        void test5() throws Exception{
            Mockito.when(jwTUtil.isBearer(Mockito.any())).thenReturn(false);

            ActivityDTO expectedResponse = generateActivityDTO();

            Mockito.when(activityService.createActivity(Mockito.any())).thenReturn(expectedResponse);

            mockMvc.perform(MockMvcRequestBuilders.post("/activities")
                            .content(jsonMapper.writeValueAsString(expectedResponse))
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isForbidden())
                    .andDo(MockMvcResultHandlers.print());

            Mockito.verify(activityService, Mockito.never()).createActivity(Mockito.any());
        }

        @Test
        @DisplayName("Mandatory atributes missing")
        @WithMockUser(username = "adminmock@gmail.com", roles = "ADMIN")
        void test6() throws Exception {
            ActivityDTO expectedResponse = generateActivityDTO();
            Mockito.when(activityService.createActivity(Mockito.any())).thenReturn(expectedResponse);

            List<ActivityDTO> requestWithMissingAttribute = generateRequestMissingMandatoryAttributes();
            mockMvc.perform(MockMvcRequestBuilders.post("/activities")
                            .content(jsonMapper.writeValueAsString(requestWithMissingAttribute))
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isInternalServerError())
                    .andDo(MockMvcResultHandlers.print());

            Mockito.verify(activityService, Mockito.never()).createActivity(Mockito.any());
        }

        @Test
        @DisplayName("Mandatory atributes missing")
        @WithMockUser(username = "adminmock@gmail.com", roles = "ADMIN")
        void test7() throws Exception{
            ActivityDTO expectedResponse = generateActivityDTO();
            Mockito.when(activityService.createActivity(Mockito.any())).thenReturn(expectedResponse);

            List<ActivityDTO> requestWithBrokenAttribute = generateRequestWithBrokenAttributes();
            mockMvc.perform(MockMvcRequestBuilders.post("/activities")
                            .content(jsonMapper.writeValueAsString(requestWithBrokenAttribute))
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isInternalServerError())
                    .andDo(MockMvcResultHandlers.print());

            Mockito.verify(activityService, Mockito.never()).createActivity(Mockito.any());
        }
    }

    @Nested
    class updateActivityTest{
        @Test
        @DisplayName("Valid case")
        @WithMockUser(username = "adminmock@gmail.com", roles = "ADMIN")
        void test1() throws Exception {
            String id = "id123";
            ActivityDTO expectedResponse = generateActivityDTO();

            Mockito.when(activityService.updateActivity(Mockito.any(), Mockito.any())).thenReturn(expectedResponse);

            mockMvc.perform(MockMvcRequestBuilders.put("/activities" + "/{id}", id)
                            .content(jsonMapper.writeValueAsString(expectedResponse))
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isAccepted())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.content().json(jsonMapper.writeValueAsString(expectedResponse)))
                    .andDo(MockMvcResultHandlers.print());

            Mockito.verify(activityService).updateActivity(Mockito.any(), Mockito.any());
        }

        @Test
        @DisplayName("Non-existing ID")
        @WithMockUser(username = "adminmock@gmail.com", roles = "ADMIN")
        void test2() throws Exception {
            String id = "id123";
            ActivityDTO request = generateActivityDTO();

            Mockito.when(activityService.updateActivity(Mockito.any(), Mockito.any())).thenThrow();

            mockMvc.perform(MockMvcRequestBuilders.put("/activities" + "/{id}", id)
                            .content(jsonMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isNotFound())
                    .andDo(MockMvcResultHandlers.print());

            Mockito.verify(activityService).updateActivity(Mockito.any(), Mockito.any());
        }

        @Test
        @DisplayName("Name not exists")
        @WithMockUser(username = "adminmock@gmail.com", roles = "ADMIN")
        void test3() throws Exception{
            String id = "id123";
            ActivityDTO request = generateActivityDTO();

            Mockito.when(activityService.updateActivity(Mockito.any(), Mockito.any())).thenThrow();

            mockMvc.perform(MockMvcRequestBuilders.put("/activities" + "/{id}", id)
                            .content(jsonMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isNotFound())
                    .andDo(MockMvcResultHandlers.print());

            Mockito.verify(activityService).updateActivity(Mockito.any(), Mockito.any());
        }

        @Test
        @DisplayName("Invalid role")
        @WithMockUser(username = "adminmock@gmail.com", authorities = "ADMIN")
        void test4() throws  Exception{
            String id = "id123";
            ActivityDTO expectedResponse = generateActivityDTO();

            Mockito.when(activityService.updateActivity(Mockito.any(), Mockito.any())).thenReturn(expectedResponse);

            mockMvc.perform(MockMvcRequestBuilders.put("/activities" + "/{id}", id)
                            .content(jsonMapper.writeValueAsString(expectedResponse))
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isForbidden())
                    .andExpect(MockMvcResultMatchers.content().string(Matchers.not(Matchers.containsString("Activity Test DTO"))))
                    .andDo(MockMvcResultHandlers.print());

            Mockito.verify(activityService, Mockito.never()).updateActivity(Mockito.any(), Mockito.any());
        }

        @Test
        @DisplayName("Token not valid")
        void test5() throws Exception{
            Mockito.when(jwTUtil.isBearer(Mockito.any())).thenReturn(true);
            Mockito.when(jwTUtil.verify(Mockito.any())).thenThrow(new Exception());

            ActivityDTO expectedResponse = generateActivityDTO();
            String id = "id123";

            Mockito.when(activityService.updateActivity(Mockito.any(), Mockito.any())).thenReturn(expectedResponse);

            mockMvc.perform(MockMvcRequestBuilders.put("/activities" + "/{id}", id)
                            .content(jsonMapper.writeValueAsString(expectedResponse))
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isForbidden())
                    .andDo(MockMvcResultHandlers.print());

            Mockito.verify(activityService, Mockito.never()).updateActivity(Mockito.any(), Mockito.any());
        }

        @Test
        @DisplayName("Token not provided")
        void test6() throws Exception{
            Mockito.when(jwTUtil.isBearer(Mockito.any())).thenReturn(false);

            ActivityDTO expectedResponse = generateActivityDTO();
            String id = "id123";

            Mockito.when(activityService.updateActivity(Mockito.any(), Mockito.any())).thenReturn(expectedResponse);

            mockMvc.perform(MockMvcRequestBuilders.put("/activities" + "/{id}", id)
                            .content(jsonMapper.writeValueAsString(expectedResponse))
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isForbidden())
                    .andDo(MockMvcResultHandlers.print());

            Mockito.verify(activityService, Mockito.never()).updateActivity(Mockito.any(), Mockito.any());
        }

        @Test
        @DisplayName("Mandatory atributes missing")
        @WithMockUser(username = "adminmock@gmail.com", roles = "ADMIN")
        void test7() throws Exception {
            ActivityDTO expectedResponse = generateActivityDTO();
            Mockito.when(activityService.updateActivity(Mockito.any(), Mockito.any())).thenReturn(expectedResponse);
            String id = "id123";
            List<ActivityDTO> requestWithMissingAttribute = generateRequestMissingMandatoryAttributes();
            mockMvc.perform(MockMvcRequestBuilders.put("/activities" + "/{id}", id)
                            .content(jsonMapper.writeValueAsString(requestWithMissingAttribute))
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isInternalServerError())
                    .andDo(MockMvcResultHandlers.print());

            Mockito.verify(activityService, Mockito.never()).updateActivity(Mockito.any(), Mockito.any());
        }

        @Test
        @DisplayName("Mandatory atributes missing")
        @WithMockUser(username = "adminmock@gmail.com", roles = "ADMIN")
        void test8() throws Exception{
            ActivityDTO expectedResponse = generateActivityDTO();
            Mockito.when(activityService.updateActivity(Mockito.any(), Mockito.any())).thenReturn(expectedResponse);

            String id = "id123";
            List<ActivityDTO> requestWithBrokenAttribute = generateRequestWithBrokenAttributes();
            mockMvc.perform(MockMvcRequestBuilders.put("/activities" + "/{id}", id)
                            .content(jsonMapper.writeValueAsString(requestWithBrokenAttribute))
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isInternalServerError())
                    .andDo(MockMvcResultHandlers.print());

            Mockito.verify(activityService, Mockito.never()).updateActivity(Mockito.any(), Mockito.any());
        }
    }
}