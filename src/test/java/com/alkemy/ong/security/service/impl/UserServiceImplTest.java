package com.alkemy.ong.security.service.impl;

import com.alkemy.ong.dto.UserDTO;
import com.alkemy.ong.entity.Role;
import com.alkemy.ong.entity.Users;
import com.alkemy.ong.repository.RoleRepository;
import com.alkemy.ong.repository.UsersRepository;
import com.alkemy.ong.security.dto.RegisterDTO;
import com.alkemy.ong.security.dto.UserWithoutPassDTO;
import com.alkemy.ong.security.mapper.UserWithJWTMapper;
import com.alkemy.ong.security.mapper.UserWithoutPassMapper;
import com.alkemy.ong.security.util.JwTUtil;
import com.alkemy.ong.service.implement.EmailServiceImpl;
import javassist.NotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import static org.assertj.core.api.Assertions.*;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserServiceImpl.class)
class UserServiceImplTest {

    @MockBean
    private UsersRepository usersRepository;

    @SpyBean
    private UserWithoutPassMapper userWithoutPassMapper;

    @MockBean
    private PasswordEncoder encoder;

    @SpyBean
    private UserWithJWTMapper userWithJWTMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private JwTUtil jwTUtil;

    @MockBean
    private EmailServiceImpl emailService;

    private UserServiceImpl userServiceImpl;

    private Users user;

    private Users admin;

    private UserDetails details;

    private Authentication userDetails;

    private UserWithoutPassDTO userDTO;

    private Role roleUser;

    private Role roleAdmin;

    private RegisterDTO registerDTO;

    @BeforeEach
    void setUp() {
        userServiceImpl = new UserServiceImpl(usersRepository, userWithoutPassMapper, roleRepository, authenticationManager, jwTUtil, userWithJWTMapper, encoder, emailService);

        roleUser = Role.builder()
                .id("1")
                .name("ROLE_USER")
                .description("any user")
                .build();

        user = Users.builder()
                .id("1")
                .firstName("Ezequiel")
                .lastName("Alvarez")
                .email("ezequiel@gmail.com")
                .password("1234")
                .photo("userPhoto")
                .role(roleUser)
                .build();

        roleAdmin = Role.builder()
                .id("1")
                .name("ROLE_ADMIN")
                .description("administrator")
                .build();

        admin = Users.builder()
                .id("2")
                .firstName("Ulises")
                .lastName("Admin")
                .email("ulises@admin.com")
                .password("1234")
                .photo("adminPhoto")
                .role(roleAdmin)
                .build();
    }

    @DisplayName("LOAD user - Correct User Login")
    @Test
    void loadUserByUsername(){
        given(usersRepository.findByEmailOrPassword(user.getEmail(), user.getEmail())).willReturn(user);

        details = userServiceImpl.loadUserByUsername(user.getEmail());

        assertThat(details).isNotNull();
    }

    @DisplayName("LOAD user - Inorrect User Login - UsernameNotFoundException")
    @Test
    void loadUserByUsernameUsernameNotFoundException(){
        given(usersRepository.findByEmailOrPassword(user.getEmail(), user.getEmail())).willReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            details = userServiceImpl.loadUserByUsername(user.getEmail());
        });

        assertThat(details).isNull();
    }

    @DisplayName("CREATE user - ADMIN")
    @Test
    void createAdmin() {
        registerDTO = new RegisterDTO();
        registerDTO.setFirstName(admin.getFirstName());
        registerDTO.setLastName(admin.getLastName());
        registerDTO.setEmail(admin.getEmail());
        registerDTO.setPassword(admin.getPassword());
        registerDTO.setJwt("1234");

        userDetails = new UsernamePasswordAuthenticationToken(registerDTO.getEmail(), registerDTO.getPassword(), new ArrayList<>());
        userDetails.setAuthenticated(false);
        Authentication auth = authenticationManager.authenticate(userDetails);

        given(roleRepository.findByName(roleAdmin.getName())).willReturn(Optional.of(roleAdmin));
        given(usersRepository.save(any())).willReturn(admin);
        given(authenticationManager.authenticate(userDetails)).willReturn(auth);

        when(jwTUtil.generateToken(auth)).thenReturn("1234");

        RegisterDTO usersRegisterDTO = userServiceImpl.create(registerDTO);

        assertEquals(usersRegisterDTO.getFirstName(), registerDTO.getFirstName());
        assertEquals(usersRegisterDTO.getLastName(), registerDTO.getLastName());
        assertEquals(usersRegisterDTO.getEmail(), registerDTO.getEmail());
        assertEquals(usersRegisterDTO.getPassword(), registerDTO.getPassword());
        verify(jwTUtil, times(1)).generateToken(auth);
        verify(userWithJWTMapper, times(1)).userDTO2Entity(registerDTO);
        verify(userWithJWTMapper, times(1)).userEntity2DTO(admin, "1234");
    }

    @DisplayName("CREATE user - USER")
    @Test
    void createUser() {
        registerDTO = new RegisterDTO();
        registerDTO.setFirstName(user.getFirstName());
        registerDTO.setLastName(user.getLastName());
        registerDTO.setEmail(user.getEmail());
        registerDTO.setPassword(user.getPassword());
        registerDTO.setJwt("1234");

        userDetails = new UsernamePasswordAuthenticationToken(registerDTO.getEmail(), registerDTO.getPassword(), new ArrayList<>());
        userDetails.setAuthenticated(false);
        Authentication auth = authenticationManager.authenticate(userDetails);

        given(roleRepository.findByName(roleUser.getName())).willReturn(Optional.of(roleUser));
        given(usersRepository.save(any())).willReturn(user);

        when(jwTUtil.generateToken(auth)).thenReturn("1234");

        RegisterDTO usersRegisterDTO = userServiceImpl.create(registerDTO);

        assertEquals(usersRegisterDTO.getFirstName(), registerDTO.getFirstName());
        assertEquals(usersRegisterDTO.getLastName(), registerDTO.getLastName());
        assertEquals(usersRegisterDTO.getEmail(), registerDTO.getEmail());
        assertEquals(usersRegisterDTO.getPassword(), registerDTO.getPassword());
        assertEquals(usersRegisterDTO.getJwt(), registerDTO.getJwt());
        verify(jwTUtil, times(1)).generateToken(auth);
        verify(userWithJWTMapper, times(1)).userDTO2Entity(registerDTO);
        verify(userWithJWTMapper, times(1)).userEntity2DTO(user, "1234");
    }

    @DisplayName("CREATE user - BadCredentialsException")
    @Test
    void createBadCretentials() {
        registerDTO = new RegisterDTO();
        registerDTO.setFirstName(user.getFirstName());
        registerDTO.setLastName(user.getLastName());
        registerDTO.setEmail(user.getEmail());
        registerDTO.setPassword(user.getPassword());
        registerDTO.setJwt("1234");

        userDetails = new UsernamePasswordAuthenticationToken(registerDTO.getEmail(), registerDTO.getPassword(), new ArrayList<>());
        userDetails.setAuthenticated(false);
        Authentication auth = authenticationManager.authenticate(userDetails);

        given(roleRepository.findByName(roleUser.getName())).willReturn(Optional.of(roleUser));
        given(usersRepository.save(any())).willReturn(user);

        when(authenticationManager.authenticate(userDetails)).thenThrow(BadCredentialsException.class);

        assertThrows(BadCredentialsException.class, () -> {
            userServiceImpl.create(registerDTO);
        });

        verify(jwTUtil, never()).generateToken(auth);
        verify(userWithJWTMapper, never()).userEntity2DTO(user, "1234");
    }

    @DisplayName("DELETE user - user found")
    @Test
    void delete() throws NotFoundException{
        given(usersRepository.findById(user.getId())).willReturn(Optional.of(user));
        willDoNothing().given(usersRepository).deleteById(user.getId());

        userServiceImpl.delete(user.getId());

        verify(usersRepository, times(1)).deleteById(user.getId());
    }

    @DisplayName("DELETE user - user NOT found")
    @Test
    void deleteUserNotFound() {
        given(usersRepository.findById(user.getId())).willReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            userServiceImpl.delete(user.getId());
        });

        verify(usersRepository, never()).deleteById(user.getId());
    }

    @DisplayName("PATCH user - user found")
    @Test
    void patchUser() throws NotFoundException {
        Map<Object, Object> objectMap = new HashMap<>();
        objectMap.put("firstName", "Esteban");
        objectMap.put("lastName", "Quito");
        objectMap.put("email", "esteban@gmail.com");
        objectMap.put("password", "4321");

        given(usersRepository.findById("1")).willReturn(Optional.of(user));
        given(encoder.encode((String) objectMap.get("password"))).willReturn("4321");

        userServiceImpl.patchUser("1", objectMap);

        verify(usersRepository, times(1)).findById("1");
        verify(encoder, times(1)).encode((String) objectMap.get("password"));
    }

    @DisplayName("PATCH user - user NOT found")
    @Test
    void patchUserNotFound() {
        Map<Object, Object> objectMap = new HashMap<>();
        objectMap.put("firstName", "Esteban");
        objectMap.put("lastName", "Quito");
        objectMap.put("email", "esteban@gmail.com");
        objectMap.put("password", "4321");

        given(usersRepository.findById("1")).willReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            userServiceImpl.patchUser("1", objectMap);
        });

        verify(usersRepository, never()).save(any(Users.class));
    }

    @DisplayName("GET data user - login")
    @Test
    void meDataFound() {
        given(usersRepository.findByEmail(user.getEmail())).willReturn(Optional.ofNullable(user));

        UserDTO userDTO = userServiceImpl.meData(user.getEmail());

        assertThat(userDTO).isNotNull();
    }

    @DisplayName("GET list of users - get list with an element")
    @Test
    void findAllUsersListWithUsers() {
        given(usersRepository.findAll()).willReturn(List.of(user));

        List<UserWithoutPassDTO> usersDTO = userServiceImpl.findAllUsers();

        assertThat(usersDTO).isNotEmpty();
        assertThat(usersDTO.size()).isEqualTo(1);
    }

    @DisplayName("GET list of users - get list with no elements")
    @Test
    void findAllUsersEmptyList() {
        userDTO = new UserWithoutPassDTO();
        userDTO.setFirstName("Ezequiel");
        userDTO.setLastName("Alvarez");
        userDTO.setEmail("ezequiel@gmail.com");
        userDTO.setPhoto("ezequielPhoto");

        given(usersRepository.findAll()).willReturn(Collections.emptyList());
        given(userWithoutPassMapper.userWPEntityList2DTOList(Collections.emptyList())).willReturn(Collections.emptyList());

        List<UserWithoutPassDTO> usersDTO = userServiceImpl.findAllUsers();

        assertThat(usersDTO).isEmpty();
        assertThat(usersDTO.size()).isEqualTo(0);
    }
}
