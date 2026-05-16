package ru.practicum.blog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.blog.domain.Comment;
import ru.practicum.blog.dto.CommentDto;
import ru.practicum.blog.mapper.CommentMapper;
import ru.practicum.blog.repository.CommentRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CommentServiceTest {

    private CommentRepository commentRepository;
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        commentRepository = Mockito.mock(CommentRepository.class);
        CommentMapper commentMapper = new CommentMapper();
        commentService = new CommentService(commentRepository, commentMapper);
    }

    private Comment makeComment(long id, String text, long postId) {
        Comment c = new Comment();
        c.setId(id);
        c.setText(text);
        c.setPostId(postId);
        return c;
    }

    @Test
    void getByPostIdReturnsDtos() {
        when(commentRepository.findByPostId(1L))
                .thenReturn(List.of(makeComment(1L, "First", 1L), makeComment(2L, "Second", 1L)));

        List<CommentDto> result = commentService.getByPostId(1L);

        assertEquals(2, result.size());
        assertEquals("First", result.get(0).getText());
        assertEquals("Second", result.get(1).getText());
    }

    @Test
    void getByIdReturnsDto() {
        when(commentRepository.findById(5L)).thenReturn(makeComment(5L, "Hello", 1L));

        CommentDto dto = commentService.getById(5L);

        assertEquals(5L, dto.getId());
        assertEquals("Hello", dto.getText());
    }

    @Test
    void createDelegatesToRepository() {
        Comment saved = makeComment(10L, "New comment", 1L);
        when(commentRepository.save(any())).thenReturn(saved);

        CommentDto input = new CommentDto();
        input.setText("New comment");

        CommentDto result = commentService.create(1L, input);

        assertEquals(10L, result.getId());
        assertEquals(1L, result.getPostId());
        verify(commentRepository).save(any());
    }

    @Test
    void updateChangesText() {
        Comment existing = makeComment(5L, "Old", 1L);
        when(commentRepository.findById(5L)).thenReturn(existing);

        CommentDto input = new CommentDto();
        input.setText("Updated");

        commentService.update(5L, input);

        verify(commentRepository).update(argThat(c -> c.getText().equals("Updated")));
    }

    @Test
    void deleteByIdDelegatesToRepository() {
        commentService.deleteById(5L);
        verify(commentRepository).deleteById(5L);
    }
}
