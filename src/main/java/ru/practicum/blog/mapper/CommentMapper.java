package ru.practicum.blog.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.blog.domain.Comment;
import ru.practicum.blog.dto.CommentDto;

@Component
public class CommentMapper {

    public CommentDto toDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setPostId(comment.getPostId());
        return dto;
    }
}
