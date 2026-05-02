package ru.practicum.blog.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.blog.domain.Comment;

import static org.junit.jupiter.api.Assertions.*;

class CommentMapperTest {

    private final CommentMapper mapper = new CommentMapper();

    @Test
    void toDtoCopiesAllFields() {
        Comment comment = new Comment();
        comment.setId(10L);
        comment.setText("Nice post!");
        comment.setPostId(1L);

        var dto = mapper.toDto(comment);

        assertEquals(10L, dto.getId());
        assertEquals("Nice post!", dto.getText());
        assertEquals(1L, dto.getPostId());
    }
}
