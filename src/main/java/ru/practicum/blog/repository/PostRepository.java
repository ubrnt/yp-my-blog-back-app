package ru.practicum.blog.repository;

import ru.practicum.blog.domain.Post;

public interface PostRepository {
    Post findById(long id);

    Post save(Post post);

    void deleteById(long id);
}
