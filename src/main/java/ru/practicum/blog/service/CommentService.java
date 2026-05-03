package ru.practicum.blog.service;

import org.springframework.stereotype.Service;
import ru.practicum.blog.domain.Comment;
import ru.practicum.blog.dto.CommentDto;
import ru.practicum.blog.mapper.CommentMapper;
import ru.practicum.blog.repository.CommentRepository;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public CommentService(CommentRepository commentRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    public List<CommentDto> getByPostId(long postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(commentMapper::toDto)
                .toList();
    }

    public CommentDto getById(long id) {
        return commentMapper.toDto(commentRepository.findById(id));
    }

    public CommentDto create(long postId, CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setText(commentDto.getText());
        return commentMapper.toDto(commentRepository.save(comment));
    }

    public void update(long id, CommentDto commentDto) {
        Comment comment = commentRepository.findById(id);
        comment.setText(commentDto.getText());
        commentRepository.update(comment);
    }

    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }
}
