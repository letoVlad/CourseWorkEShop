package ru.skypro.homework.controller;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CommentsDTO;
import ru.skypro.homework.dto.CreateOrUpdateCommentDTO;
import ru.skypro.homework.mappers.CommentMapper;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.entities.AdEntity;
import ru.skypro.homework.service.entities.CommentEntity;
import ru.skypro.homework.service.repositories.CommentRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    @Mock
    CommentService commentService;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private CommentRepository commentRepository;


    @InjectMocks
    CommentController controller;

    private CommentDTO commentDTO;

    @BeforeEach
    void setUp() {
        commentDTO = new CommentDTO();
        commentDTO.setAuthor(1);
        commentDTO.setAuthorImage("TestImage");
        commentDTO.setAuthorFirstName("TestFirstName");
        commentDTO.setCreatedAt(123L);
        commentDTO.setPk(12);
        commentDTO.setText("TestText");
    }

    @Test
    void receivingAdComments() {
        //given
        CommentsDTO commentsDTO = new CommentsDTO(1, java.util.List.of(commentDTO));

        //when
        when(commentService.receivingAdComments(1)).thenReturn(commentsDTO);
        var responseEntity = this.controller.receivingAdComments(1);

        //then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(commentsDTO, responseEntity.getBody());
    }

    @Test
    void addComment() {
        int adId = 1;
        CreateOrUpdateCommentDTO text = new CreateOrUpdateCommentDTO();
        text.setText("TestText");

        AdEntity adEntity = new AdEntity();
        CommentEntity newCommentEntity = new CommentEntity();
        CommentDTO expectedCommentDTO = new CommentDTO();
        expectedCommentDTO.setText("TestText"); // Установка ожидаемого текста

//        // mock-логика

        when(commentMapper.createCommentEntity(text, adEntity)).thenReturn(newCommentEntity);
        when(commentRepository.saveAndFlush(newCommentEntity)).thenReturn(newCommentEntity);
        when(commentMapper.toCommentDto(newCommentEntity)).thenReturn(expectedCommentDTO);


        // when
        CommentDTO resultCommentDTO = commentService.addComment(adId, text);


        // Проверяем, что текст в CommentDTO совпадает с ожидаемым текстом
        assertEquals(expectedCommentDTO.getText(), resultCommentDTO.getText());

        // проверка вызовов методов

        verify(commentMapper, times(1)).createCommentEntity(text, adEntity);
        verify(commentRepository, times(1)).saveAndFlush(newCommentEntity);
        verify(commentMapper, times(1)).toCommentDto(newCommentEntity);
    }

    @Test
    void deleteComment() {
    }

    @Test
    void updateComment() {
    }
}