package com.alkemy.ong.controller;

import com.alkemy.ong.dto.CommentDTO;
import com.alkemy.ong.dto.CommentDTOBody;
import com.alkemy.ong.exception.GlobalExceptionHandler;
import com.alkemy.ong.security.service.impl.UserServiceImpl;
import com.alkemy.ong.security.util.JwTUtil;
import com.alkemy.ong.service.implement.CommentServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import javassist.NotFoundException;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentServiceImpl commentService;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    JwTUtil jwTUtil;

    @Autowired
    ObjectMapper jsonMapper;

    @BeforeEach
    void setUp() {
        this.jsonMapper = new ObjectMapper();
    }

    @Nested
    class createComment {

        @DisplayName("create comment as USER added successful")
        @WithMockUser(username = "mock@admin.com", roles = "USER")
        @Test
        void test1() throws Exception{
            CommentDTO commentDTO = generateDTO();
            ResponseEntity<CommentDTO> responseEntity = new ResponseEntity<>(HttpStatus.CREATED);
            when(commentService.create(any())).thenReturn(responseEntity);

            mockMvc.perform(post("/comment")
                            .contentType(APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(jsonMapper.writeValueAsString(commentDTO)))
                    .andExpect(status().isCreated())
                    .andDo(MockMvcResultHandlers.print());

            Mockito.verify(commentService).createComment(Mockito.any());
        }

        @DisplayName("create comment as ADMIN added successful")
        @WithMockUser(username = "mock@admin.com", roles = "ADMIN")
        @Test
        void test2() throws Exception{
            CommentDTO commentDTO = generateDTO();
            ResponseEntity<CommentDTO> responseEntity = new ResponseEntity<>(HttpStatus.CREATED);
            when(commentService.create(any())).thenReturn(responseEntity);

            mockMvc.perform(post("/comment")
                            .contentType(APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(jsonMapper.writeValueAsString(commentDTO)))
                    .andExpect(status().isCreated())
                    .andDo(MockMvcResultHandlers.print());

            Mockito.verify(commentService).createComment(Mockito.any());
        }

        @DisplayName("User or news not found")
        @WithMockUser(username = "mock@admin.com", roles = "ADMIN")
        @Test
        void test3() throws Exception{
            CommentDTO commentDTO = generateDTO();
            ResponseEntity<CommentDTO> responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);

            when(commentService.create(any())).thenThrow(NotFoundException.class);

            mockMvc.perform(post("/comment")
                            .contentType(APPLICATION_JSON)
                            .content(jsonMapper.writeValueAsString(commentDTO)))
                    .andExpect(status().isNotFound())
                    .andDo(MockMvcResultHandlers.print());

            Mockito.verify(commentService).createComment(any());
        }

    }

    @Nested
    class updateComment {

        @DisplayName("comment as admin updated successful")
        @WithMockUser(username = "mock@admin.com", roles = "ADMIN")
        @Test
        void test1() throws Exception{
            String id = "123";
            CommentDTO commentDTO = generateDTO();
            when(commentService.updateComment(any(),any())).thenReturn(new ResponseEntity<>(HttpStatus.ACCEPTED));

            mockMvc.perform(put("/comment" + "/{id}", id)
                            .contentType(APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(jsonMapper.writeValueAsString(commentDTO)))
                    .andExpect(status().isAccepted())
                    .andDo(MockMvcResultHandlers.print());

            Mockito.verify(commentService).updateComment(any(),any());
        }

        @DisplayName("comment not found")
        @WithMockUser(username = "mock@admin.com", roles = "ADMIN")
        @Test
        void test2() throws Exception{
            String id = "123";
            CommentDTO commentDTO = generateDTO();

            when(commentService.exitsById(id)).thenReturn(true);
            when(commentService.updateComment(any(),any())).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

            mockMvc.perform(put("/comment" + "/{id}", id)
                            .contentType(APPLICATION_JSON)
                            .content(jsonMapper.writeValueAsString(commentDTO)))
                    .andExpect(status().isNotFound())
                    .andDo(MockMvcResultHandlers.print());

            Mockito.verify(commentService,never()).updateComment(any(),any());
        }

        @DisplayName("comment forbidden")
        @WithMockUser(username = "mock@admin.com", roles = "ADMIN")
        @Test
        void test3() throws Exception{
            String id = "123";
            CommentDTO commentDTO = generateDTO();

            when(commentService.updateComment(any(),any())).thenThrow(NotFoundException.class);

            mockMvc.perform(put("/comment" + "/{id}", id)
                            .contentType(APPLICATION_JSON)
                            .content(jsonMapper.writeValueAsString(commentDTO)))
                    .andExpect(status().isForbidden())
                    .andDo(MockMvcResultHandlers.print());

            Mockito.verify(commentService).updateComment(any(),any());
        }

    }

    @Nested
    class deleteComment {

        @DisplayName("delete comment as admin successful")
        @WithMockUser(username = "mock@admin.com", roles = "ADMIN")
        @Test
        void test1() throws Exception{
            String id = "123";
            CommentDTO commentDTO = generateDTO();

            when(commentService.deleteComment(any(),any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

            mockMvc.perform(MockMvcRequestBuilders.delete("/comment/{id}", id)
                            .content(jsonMapper.writeValueAsString(commentDTO))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print());

            verify(commentService).deleteComment(any(),any());
        }

        @DisplayName("delete comment as user successful")
        @WithMockUser(username = "mock@admin.com", roles = "USER")
        @Test
        void test2() throws Exception{
            String id = "123";
            CommentDTO commentDTO = generateDTO();

            when(commentService.deleteComment(any(),any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

            mockMvc.perform(MockMvcRequestBuilders.delete("/comment/{id}", id)
                            .content(jsonMapper.writeValueAsString(commentDTO))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print());

            verify(commentService).deleteComment(any(),any());
        }

        @DisplayName("Comment not found")
        @WithMockUser(username = "mock@admin.com", roles = "ADMIN")
        @Test
        void test3() throws Exception{
            String id = "123";
            CommentDTO commentDTO = generateDTO();

            when(commentService.exitsById(id)).thenReturn(true);
            when(commentService.deleteComment(any(),any())).thenThrow(NotFoundException.class);

            mockMvc.perform(delete("/comment" + "/{id}", id)
                            .contentType(APPLICATION_JSON)
                            .content(jsonMapper.writeValueAsString(commentDTO)))
                    .andExpect(status().isNotFound())
                    .andDo(MockMvcResultHandlers.print());

            Mockito.verify(commentService,never()).deleteComment(any(),any());
        }

        @DisplayName("forbidden")
        @WithMockUser(username = "mock@admin.com", roles = "ADMIN")
        @Test
        void test4() throws Exception{
            String id = "123";
            CommentDTO commentDTO = generateDTO();

            when(commentService.deleteComment(any(),any())).thenThrow(NotFoundException.class);

            mockMvc.perform(delete("/comment" + "/{id}", id)
                            .contentType(APPLICATION_JSON)
                            .content(jsonMapper.writeValueAsString(commentDTO)))
                    .andExpect(status().isForbidden())
                    .andDo(MockMvcResultHandlers.print());

            Mockito.verify(commentService).deleteComment(any(),any());
        }

    }

    @Nested
    class getAllPostComments {

        @DisplayName("get all post comments by admin")
        @WithMockUser(username = "mock@admin.com", roles = "ADMIN")
        @Test
        void test1() throws Exception{
            String id = "123";
            List<CommentDTOBody> commentDTOList = generateListCommentDTO();
            when(commentService.getAllPostComments(id)).thenReturn(commentDTOList);

            mockMvc.perform(get("/comment/posts/{id}/comments", id))
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isAccepted())
                    .andDo(MockMvcResultHandlers.print());

            verify(commentService).getAllPostComments(any());
        }

        @DisplayName("get all post comments by user")
        @WithMockUser(username = "mock@admin.com", roles = "USER")
        @Test
        void test2() throws Exception{
            String id = "123";
            List<CommentDTOBody> commentDTOList = generateListCommentDTO();
            when(commentService.getAllPostComments(id)).thenReturn(commentDTOList);

            mockMvc.perform(MockMvcRequestBuilders.get("/comment/posts/" + "{id}" + "/comments", id))
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isAccepted())
                    .andDo(MockMvcResultHandlers.print());

            Mockito.verify(commentService).getAllPostComments(any());
        }

    }

    @Nested
    class getAllComments {

        @DisplayName("get all comments as admin is valid")
        @WithMockUser(username = "mock@admin.com", roles = "ADMIN")
        @Test
        void test1() throws Exception{
            List<CommentDTOBody> commentDTOList = generateListCommentDTO();
            when(commentService.getAllComments()).thenReturn(commentDTOList);

            mockMvc.perform(MockMvcRequestBuilders.get("/comment"))
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isAccepted())
                    .andDo(MockMvcResultHandlers.print());

            Mockito.verify(commentService).getAllComments();
        }

        @DisplayName("get all comments as user is invalid")
        @WithMockUser(username = "mock@admin.com", roles = "USER")
        @Test
        void test2() throws Exception{
            List<CommentDTOBody> commentDTOList = generateListCommentDTO();
            when(commentService.getAllComments()).thenReturn(commentDTOList);

            mockMvc.perform(MockMvcRequestBuilders.get("/comment")
                            .content(jsonMapper.writeValueAsString(commentDTOList))
                            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden())
                    .andDo(MockMvcResultHandlers.print());

            Mockito.verify(commentService,never()).getAllComments();
        }

    }

    private static CommentDTO generateDTO(){
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setUserId("1");
        commentDTO.setNewsId("1");
        commentDTO.setBody("test comment");
        return commentDTO;
    }

    private static List<CommentDTOBody> generateListCommentDTO(){
        CommentDTOBody commentDTOBody = new CommentDTOBody();
        commentDTOBody.setBody("test comment");
        return Collections.singletonList(commentDTOBody);
    }

}
