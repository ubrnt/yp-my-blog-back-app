package ru.practicum.blog.repository;

import ru.practicum.blog.domain.Post;

public interface PostRepository {
    Post findById(long id);

    Post save(Post post);

    void update(Post post);

    void deleteById(long id);

    int addLike(long id);

    byte[] findImage(long id);

    void saveImage(long id, byte[] image);
}
