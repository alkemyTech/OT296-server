package com.alkemy.ong.service;

import com.alkemy.ong.dto.*;
import com.alkemy.ong.entity.Comment;
import com.alkemy.ong.entity.Users;
import com.alkemy.ong.mapper.CommentMapper;
import com.alkemy.ong.repository.CommentRepository;
import com.alkemy.ong.repository.NewsRepository;
import com.alkemy.ong.repository.UsersRepository;
import com.alkemy.ong.service.implement.CommentServiceImpl;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CommentServiceImpl.class})
public class CommentServiceImplTest {
    @MockBean
    CommentRepository commentRepository;
    @MockBean
    NewsRepository newsRepository;
    @MockBean
    UsersRepository usersRepository;
    @SpyBean
    CommentMapper commentMapper;
    @InjectMocks
    private CommentServiceImpl commentServiceImpl;

    private Comment comment;
    private CommentDTO commentDTO;
    private Users user;

    @BeforeEach
    void SetUp(){
        commentServiceImpl = new CommentServiceImpl(commentRepository,commentMapper,newsRepository,usersRepository);

        user = new Users();
        user.setId("1");
        user.setEmail("admin@gmail.com");
        user.setPassword("1234");
        user.setPhoto("adminPhoto");
        user.setFirstName("Luis");
        user.setLastName("Lopez");

        comment= new Comment();
        comment.setId("1");
        comment.setUserId("1");
        comment.setUser(user);
        comment.setBody("Hi!");
        comment.setNewsId("12sf");

        commentDTO= new CommentDTO();
        commentDTO.setUserId(comment.getUserId());
        commentDTO.setBody(comment.getBody());
        commentDTO.setUserId(comment.getUserId());
        commentDTO.setNewsId(comment.getNewsId());
    }

    @Test
    @DisplayName("Test Get All Comment list with items")
    void getAllComments() {
        List<Comment> comments = List.of(comment);
        given(commentRepository.findAll()).willReturn(comments);
        List<CommentDTOBody> categoryBasicDTO = commentMapper.commentsEntityList2DTOCommentsList(comments);
        given(commentServiceImpl.getAllComments()).willReturn(categoryBasicDTO);

        assertThat(categoryBasicDTO).isNotNull();
        assertThat(categoryBasicDTO.size()).isEqualTo(1);
        Mockito.verify(commentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test Get All Comment list with no items")
    void getAllComments_EmptyList() {
        List<Comment> comments = new ArrayList<>();
        given(commentRepository.findAll()).willReturn(comments);
        List<CommentDTOBody> categoryBasicDTO = commentMapper.commentsEntityList2DTOCommentsList(comments);
        given(commentServiceImpl.getAllComments()).willReturn(categoryBasicDTO);

        assertThat(categoryBasicDTO.size()).isEqualTo(0);
        Mockito.verify(commentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Create Comment - success")
    void createSuccess() throws NotFoundException {
        given(usersRepository.existsById(any())).willReturn(true);
        given(newsRepository.existsById(any())).willReturn(true);

        when(commentRepository.save(any())).thenReturn(comment);
        ResponseEntity<CommentDTO> commentResponse = commentServiceImpl.create(commentDTO);
        ResponseEntity<CommentDTO> expectedResponse = new ResponseEntity("Comment created",HttpStatus.CREATED);

        verify(commentRepository, times(1)).save(any());
        assertThat(commentResponse).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Create Comment - user not found")
    void createUserNotFound() throws NotFoundException {
        given(usersRepository.existsById(any())).willReturn(false);

        when(commentRepository.save(any())).thenReturn(comment);
        ResponseEntity<CommentDTO> commentResponse = commentServiceImpl.create(commentDTO);
        ResponseEntity<CommentDTO> expectedResponse = new ResponseEntity("User not found", HttpStatus.NOT_FOUND);

        verify(commentRepository, never()).save(any());
        assertThat(commentResponse).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Create Comment - news not found")
    void createNewsNotFound() throws NotFoundException {
        given(usersRepository.existsById(any())).willReturn(true);
        given(newsRepository.existsById(any())).willReturn(false);
        when(commentRepository.save(any())).thenReturn(comment);
        ResponseEntity<CommentDTO> commentResponse = commentServiceImpl.create(commentDTO);
        ResponseEntity<CommentDTO> expectedResponse = new ResponseEntity("News not found", HttpStatus.NOT_FOUND);

        verify(commentRepository, never()).save(any());
        assertThat(commentResponse).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Update Comment - success")
    void updateCommentSuccess() throws Exception{
        given(commentRepository.findById(any())).willReturn(Optional.of(comment));
        given(usersRepository.findById(any())).willReturn(Optional.of(user));

        ResponseEntity<?> commentResponse = commentServiceImpl.updateComment("1", commentDTO);
        ResponseEntity<CommentDTO> expectedResponse = new ResponseEntity(HttpStatus.ACCEPTED);

        assertThat(commentResponse).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("GetAllPostComments - list with items")
    void getAllPostComments(){
        given(commentRepository.findAllByNewsId(any())).willReturn(List.of(comment));

        List<CommentDTOBody> listOfDtoBody = commentServiceImpl.getAllPostComments("1");

        assertThat(listOfDtoBody).isNotNull();
        assertThat(listOfDtoBody.size()).isNotEqualTo(0);
    }

    @Test
    @DisplayName("GetAllPostComments - list with no items")
    void getAllPostCommentsEmptyList(){
        given(commentRepository.findAllByNewsId(any())).willReturn(Collections.emptyList());

        List<CommentDTOBody> listOfDtoBody = commentServiceImpl.getAllPostComments("1");

        assertThat(listOfDtoBody).isNotNull();
        assertThat(listOfDtoBody.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("DeleteCommentById - success")
    void deleteComment() throws NotFoundException {
        given(commentRepository.findById(any())).willReturn(Optional.of(comment));
        given(usersRepository.findByEmail(any())).willReturn(Optional.of(user));

        Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), new ArrayList<>());
        auth.setAuthenticated(false);

        ResponseEntity<?> commentResponse = commentServiceImpl.deleteComment("1", auth);
        ResponseEntity<?> expectedResponse = new ResponseEntity<>(HttpStatus.ACCEPTED);

        assertThat(commentResponse).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("DeleteCommentById - not found exception")
    void deleteCommentNotFound() throws NotFoundException {
        Users anotherUser = new Users();
        anotherUser.setId("2");
        anotherUser.setEmail("jose@gmail.com");
        anotherUser.setPassword("321");
        given(commentRepository.findById(any())).willReturn(Optional.of(comment));
        given(usersRepository.findByEmail(any())).willReturn(Optional.of(anotherUser));

        Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), new ArrayList<>());
        auth.setAuthenticated(false);

        assertThrows(NotFoundException.class, () -> {
            commentServiceImpl.deleteComment("1", auth);
        });
        verify(commentRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Exist by id - not empty")
    void existById(){
        given(commentRepository.findById(any())).willReturn(Optional.of(comment));
        boolean itExist = commentServiceImpl.exitsById("1");
        assertThat(itExist).isEqualTo(false);
    }

    @Test
    @DisplayName("Exist by id - empty")
    void existByIdEmpty(){
        given(commentRepository.findById(any())).willReturn(Optional.empty());
        boolean itExist = commentServiceImpl.exitsById("1");
        assertThat(itExist).isEqualTo(true);
    }
}