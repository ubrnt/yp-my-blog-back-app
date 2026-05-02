package ru.practicum.blog.repository;

import ru.practicum.blog.domain.Comment;

public interface CommentRepository {

    Comment save(Comment comment);

    Comment findById(long id);

    void deleteById(long id);
}
