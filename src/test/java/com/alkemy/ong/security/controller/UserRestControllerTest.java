package com.alkemy.ong.security.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.alkemy.ong.security.configuration.SecurityConfiguration;
import com.alkemy.ong.security.dto.LoginDTO;
import com.alkemy.ong.security.service.UserService;
import com.alkemy.ong.security.service.impl.UserServiceImpl;
import com.alkemy.ong.security.util.JwTUtil;
import com.alkemy.ong.utils.OpenAPISecurityConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.alkemy.ong.util.LoginMocksUtil.*;
import static com.alkemy.ong.util.RegisterMocksUtil.*;

@WebMvcTest(UserRestController.class)
@Import({SecurityConfiguration.class, BCryptPasswordEncoder.class, OpenAPISecurityConfiguration.class})
class UserRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AuthenticationManager authenticationManager;

	@MockBean
	private JwTUtil jwTUtil;

	@MockBean
	private UserServiceImpl userServiceImpl;

//	@MockBean
//	private UserService userService;

	@Autowired
	private ObjectMapper jsonMapper;

	@BeforeEach
	public void setting(){
		this.jsonMapper = new ObjectMapper();
		userServiceImpl.create(generateRegisterDTO());
	}

	@Nested
	class meTest{
		@Test
		@DisplayName("Valid case")
		void testMeData() throws Exception {
			fail("Not yet implemented");
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
//					.andDo(MockMvcResultHandlers.print());
					;

			assertEquals(200, result.andReturn().getResponse().getStatus());
		}

		@Test
		@DisplayName("Invalid Json")
		void test2() throws Exception {
			LoginDTO requestBody = generateFakeLoginDTO();

			ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.content(jsonMapper.writeValueAsString(requestBody)))
					//        			.andExpect(status().isForbidden())
					//        			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.TEXT_PLAIN))
					//        			.andExpect(MockMvcResultMatchers.content().string("ok: false"))
					.andDo(MockMvcResultHandlers.print());

			assertEquals(403, result.andReturn().getResponse().getStatus());
		}

		@Test
		@DisplayName("Invalid Request")
		void test3() throws Exception {

			ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.content(" "));
			//.andExpect(status().isForbidden())
			//        			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.TEXT_PLAIN))
			//        			.andExpect(MockMvcResultMatchers.content().string("ok: false"))
			//        			.andDo(MockMvcResultHandlers.print());

			assertEquals(500, result.andReturn().getResponse().getStatus());
		}
	}

	@Nested
	class registerTest{
		@Test
		@DisplayName("Valid case")
		//        @WithMockUser(username = "adminmock@gmail.com", roles = "ADMIN")
		void testRegister() throws Exception {
			fail("Not yet implemented");
		}
	}
}