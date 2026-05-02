package ru.practicum.blog.repository;

import ru.practicum.blog.domain.Comment;

import java.util.List;

public interface CommentRepository {

    Comment save(Comment comment);

    Comment findById(long id);

    void deleteById(long id);

    List<Comment> findByPostId(long postId);

    void update(Comment comment);
}
