package com.alkemy.ong.security.controller;

import static com.alkemy.ong.util.LoginMocksUtil.generateExistentLoginDTO;
import static com.alkemy.ong.util.LoginMocksUtil.generateFakeLoginDTO;
import static com.alkemy.ong.util.RegisterMocksUtil.generateRegisterDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alkemy.ong.security.dto.UserWithoutPassDTO;
import com.alkemy.ong.security.service.impl.UserServiceImpl;
import com.alkemy.ong.security.util.JwTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.alkemy.ong.security.dto.LoginDTO;
import com.alkemy.ong.security.dto.RegisterDTO;

@WebMvcTest(UserRestController.class)
class UserRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AuthenticationManager authenticationManager;

	@MockBean
	private JwTUtil jwTUtil;

    @MockBean
    private UserServiceImpl userService;

    @Autowired
    private ObjectMapper jsonMapper;

    private List<UserWithoutPassDTO> userDTOList;

	@BeforeEach
	public void setting(){
		this.jsonMapper = new ObjectMapper();
	}

	@Nested
	class meTest{
		@Test
		@DisplayName("Valid case")
		void test1() throws Exception {
			ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/auth/me"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print());

			assertEquals(200, result.andReturn().getResponse().getStatus());
		}
	}

	@Nested
	class loginTest{
		@Test
		@DisplayName("Valid case")
		void test1() throws Exception {
			LoginDTO requestBody = generateExistentLoginDTO();

			ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.content(jsonMapper.writeValueAsString(requestBody)))
					.andExpect(status().isOk())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
					.andDo(MockMvcResultHandlers.print());

			assertEquals(200, result.andReturn().getResponse().getStatus());
		}

		@Test
		@DisplayName("Bad request")
		void test2() throws Exception {
			LoginDTO requestBody = generateFakeLoginDTO();

			when(authenticationManager.authenticate(Mockito.any())).thenThrow(new BadCredentialsException("ok false"));

			ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.content(jsonMapper.writeValueAsString(requestBody)))
					.andDo(MockMvcResultHandlers.print());

			assertEquals(400, result.andReturn().getResponse().getStatus());
		}

		@Test
		@DisplayName("Invalid Request")
		void test3() throws Exception {

			ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.content(" "))
					.andDo(MockMvcResultHandlers.print());

			assertEquals(500, result.andReturn().getResponse().getStatus());
		}
	}

	@Nested
	class registerTest{
		@Test
		@DisplayName("Valid case")
		void test1() throws Exception {
			RegisterDTO requestBody = generateRegisterDTO();

			ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.content(jsonMapper.writeValueAsString(requestBody)))
					.andExpect(status().isCreated())
					.andDo(MockMvcResultHandlers.print());

			assertEquals(201, result.andReturn().getResponse().getStatus());
		}
	}

    @Nested
    class FindAllUsersTest { //status: 200

        @Test
        @DisplayName("findAll - Admin - status.OK")
        @WithMockUser(username = "adminmock@gmail.com", roles = "ADMIN")
        void test1() throws Exception {
            UserWithoutPassDTO userWithoutPassDTO = new UserWithoutPassDTO();
            userWithoutPassDTO.setFirstName("Ezequiel");
            userWithoutPassDTO.setLastName("Perez");
            userWithoutPassDTO.setEmail("ezequiel@gmail.com");
            userWithoutPassDTO.setPhoto("ezequielPhoto");
            userDTOList = new ArrayList<UserWithoutPassDTO>();
            userDTOList.add(userWithoutPassDTO);

            when(userService.findAllUsers()).thenReturn(userDTOList);

            ResultActions response = mockMvc.perform(get("/auth/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonMapper.writeValueAsString(userDTOList)));

            response.andExpect(status().isOk())
                    .andDo(print())
                    .andExpect(jsonPath("$.size()", is(userDTOList.size())));
            verify(userService).findAllUsers();
        }

        @Test
        @DisplayName("findAll - User - status.OK")
        @WithMockUser(username = "usermock@gmail.com", roles = "USER")
        void test2() throws Exception {
            UserWithoutPassDTO userWithoutPassDTO = new UserWithoutPassDTO();
            userWithoutPassDTO.setFirstName("Ezequiel");
            userWithoutPassDTO.setLastName("Perez");
            userWithoutPassDTO.setEmail("ezequiel@gmail.com");
            userWithoutPassDTO.setPhoto("ezequielPhoto");
            userDTOList = new ArrayList<UserWithoutPassDTO>();
            userDTOList.add(userWithoutPassDTO);

            when(userService.findAllUsers()).thenReturn(userDTOList);

            ResultActions response = mockMvc.perform(get("/auth/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonMapper.writeValueAsString(userDTOList)));

            response.andExpect(status().isOk())
                    .andDo(print())
                    .andExpect(jsonPath("$.size()", is(userDTOList.size())));
            verify(userService).findAllUsers();
        }
    }

    @Nested
    class Delete{ //status: 200, 404 and 403

        @Test
        @DisplayName("Delete status.OK")
        @WithMockUser(username = "adminmock@gmail.com", roles = "ADMIN")
        void test1() throws Exception {
            willDoNothing().given(userService).delete(any());

            ResultActions response = mockMvc.perform(delete("/auth/users/{id}", "1")
                    .contentType(MediaType.APPLICATION_JSON));

            response.andExpect(status().isOk())
                            .andDo(print());
            verify(userService).delete(any());
        }

        @Test
        @DisplayName("Delete status.NOTFOUND")
        @WithMockUser(username = "adminmock@gmail.com", roles = "ADMIN")
        void test2() throws Exception {
            String exceptionResponse = "User Not Found";
            willDoNothing().given(userService).delete(any());
            doThrow(NotFoundException.class).when(userService).delete(any());

            ResultActions response = mockMvc.perform(delete("/auth/users/{id}", "1")
                    .contentType(MediaType.APPLICATION_JSON));

            response.andExpect(status().isNotFound())
                    .andExpect(content().string(exceptionResponse))
                    .andDo(print());
        }

        @Test
        @DisplayName("Delete status.FORBIDDEN")
        @WithMockUser(username = "usermock@gmail.com", roles = "USER")
        void test3() throws Exception {
            willDoNothing().given(userService).delete(any());
            doThrow(NotFoundException.class).when(userService).delete(any());

            ResultActions response = mockMvc.perform(delete("/auth/users/{id}", "1")
                    .contentType(MediaType.APPLICATION_JSON));

            response.andExpect(status().isForbidden())
                    .andDo(print());
        }
    }

    @Nested // status: 202, 404 and 403
    class PatchUser{

        @Test
        @DisplayName("patchUser status.OK")
        @WithMockUser(username = "adminmock@gmail.com", roles = "ADMIN")
        void test1() throws Exception {
            Map<Object, Object> objectMap = new HashMap<Object, Object>();
            objectMap.put("firstName", "Ezequiel");
            objectMap.put("lastName", "Perez");
            objectMap.put("email", "ezequiel@gmail.com");
            objectMap.put("password", "1234");

            willDoNothing().given(userService).patchUser(any(), any());

            ResultActions response = mockMvc.perform(patch("/auth/users/{id}", "123", objectMap)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonMapper.writeValueAsString(objectMap)));

            response.andExpect(status().isAccepted())
                    .andDo(print());
        }

        @Test
        @DisplayName("patchUser status.NOTFOUND")
        @WithMockUser(username = "adminmock@gmail.com", roles = "ADMIN")
        void test2() throws Exception {
            String exceptionResponse = "User Not Found";
            Map<Object, Object> objectMap = new HashMap<Object, Object>();
            objectMap.put("firstName", "Ezequiel");
            objectMap.put("lastName", "Perez");
            objectMap.put("email", "ezequiel@gmail.com");
            objectMap.put("password", "1234");

            willDoNothing().given(userService).patchUser(any(), any());
            doThrow(NotFoundException.class).when(userService).patchUser(any(), any());

            ResultActions response = mockMvc.perform(patch("/auth/users/{id}", "123", objectMap)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonMapper.writeValueAsString(objectMap)));

            response.andExpect(status().isNotFound())
                    .andExpect(content().string("User Not Found"))
                    .andDo(print());
        }

        @Test
        @DisplayName("patchUser status.FORBIDDEN")
        @WithMockUser(username = "usermock@gmail.com", roles = "USER")
        void test3() throws Exception {
            Map<Object, Object> objectMap = new HashMap<Object, Object>();
            objectMap.put("firstName", "Ezequiel");
            objectMap.put("lastName", "Perez");
            objectMap.put("email", "ezequiel@gmail.com");
            objectMap.put("password", "1234");

            willDoNothing().given(userService).patchUser(any(), any());
            doThrow(NotFoundException.class).when(userService).patchUser(any(), any());

            ResultActions response = mockMvc.perform(patch("/auth/users/{id}", "123", objectMap)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonMapper.writeValueAsString(objectMap)));

            response.andExpect(status().isForbidden())
                    .andDo(print());
        }
    }
}