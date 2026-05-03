package ru.practicum.blog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.blog.domain.Post;
import ru.practicum.blog.dto.PostDto;
import ru.practicum.blog.dto.PostsList;
import ru.practicum.blog.mapper.PostMapper;
import ru.practicum.blog.repository.PostRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PostServiceTest {

    private PostRepository postRepository;
    private PostService postService;

    @BeforeEach
    void setUp() {
        postRepository = Mockito.mock(PostRepository.class);
        PostMapper postMapper = new PostMapper(128);
        postService = new PostService(postRepository, postMapper);
    }

    private Post makePost(long id, String title, String text) {
        Post post = new Post();
        post.setId(id);
        post.setTitle(title);
        post.setText(text);
        post.setTags(List.of());
        return post;
    }

    @Test
    void getByIdReturnsDto() {
        Post post = makePost(1L, "Title", "Full text");
        when(postRepository.findById(1L)).thenReturn(post);

        PostDto dto = postService.getById(1L);

        assertEquals("Title", dto.getTitle());
        assertEquals("Full text", dto.getText());
    }

    @Test
    void getAllParsesSearchWithTags() {
        when(postRepository.findAll(eq("spring"), eq(List.of("java")), eq(10), eq(0)))
                .thenReturn(List.of(makePost(1L, "Spring Guide", "text")));
        when(postRepository.countAll(eq("spring"), eq(List.of("java")))).thenReturn(1);

        PostsList response = postService.getAll("spring #java", 1, 10);

        assertEquals(1, response.getPosts().size());
        assertFalse(response.isHasPrev());
        assertFalse(response.isHasNext());
    }

    @Test
    void getAllParsesOnlyTags() {
        when(postRepository.findAll(isNull(), eq(List.of("java", "spring")), eq(10), eq(0)))
                .thenReturn(List.of());
        when(postRepository.countAll(isNull(), eq(List.of("java", "spring")))).thenReturn(0);

        PostsList response = postService.getAll("#java #spring", 1, 10);

        assertTrue(response.getPosts().isEmpty());
    }

    @Test
    void getAllPagination() {
        when(postRepository.findAll(isNull(), eq(List.of()), eq(2), eq(2)))
                .thenReturn(List.of(makePost(3L, "T", "text")));
        when(postRepository.countAll(isNull(), eq(List.of()))).thenReturn(5);

        PostsList response = postService.getAll("", 2, 2);

        assertTrue(response.isHasPrev());
        assertTrue(response.isHasNext());
        assertEquals(3, response.getLastPage());
    }

    @Test
    void getAllTruncatesTextInList() {
        String longText = "x".repeat(200);
        when(postRepository.findAll(any(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(makePost(1L, "T", longText)));
        when(postRepository.countAll(any(), any())).thenReturn(1);

        PostsList response = postService.getAll("", 1, 10);

        String previewText = response.getPosts().get(0).getText();
        assertEquals(129, previewText.length());
        assertTrue(previewText.endsWith("\u2026"));
    }

    @Test
    void createDelegatesToRepository() {
        PostDto input = new PostDto();
        input.setTitle("New");
        input.setText("Content");
        input.setTags(List.of("tag1"));

        Post saved = makePost(1L, "New", "Content");
        saved.setTags(List.of("tag1"));
        when(postRepository.save(any())).thenReturn(saved);

        PostDto result = postService.create(input);

        assertEquals(1L, result.getId());
        verify(postRepository).save(any());
    }

    @Test
    void deleteByIdDelegatesToRepository() {
        postService.deleteById(5L);
        verify(postRepository).deleteById(5L);
    }

    @Test
    void addLikeReturnsNewCount() {
        when(postRepository.addLike(1L)).thenReturn(6);

        int count = postService.addLike(1L);

        assertEquals(6, count);
    }
}
