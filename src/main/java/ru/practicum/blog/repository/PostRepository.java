package ru.practicum.blog.repository;

import ru.practicum.blog.domain.Post;

import java.util.List;

public interface PostRepository {
    Post findById(long id);

    List<Post> findAll(int pageSize, int offset);

    int countAll();

    Post save(Post post);

    void update(Post post);

    void deleteById(long id);

    int addLike(long id);

    byte[] findImage(long id);

    void saveImage(long id, byte[] image);
}
