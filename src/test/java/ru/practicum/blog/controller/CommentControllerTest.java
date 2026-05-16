package ru.practicum.blog.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.practicum.blog.dto.CommentDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CommentControllerTest extends BaseControllerTest {

    private long createPost() {
        jdbc.update("INSERT INTO posts (title, text, likes_count) VALUES ('test', 'text', 0)");
        return jdbc.queryForObject("SELECT MAX(id) FROM posts", Long.class);
    }

    private long createComment(long postId, String text) throws Exception {
        CommentDto dto = new CommentDto();
        dto.setText(text);

        String response = mockMvc.perform(post("/api/posts/{postId}/comments", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return mapper.readValue(response, CommentDto.class).getId();
    }

    @Test
    void getByPostId() throws Exception {
        long postId = createPost();
        createComment(postId, "First");
        createComment(postId, "Second");

        mockMvc.perform(get("/api/posts/{postId}/comments", postId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].text").value("First"))
                .andExpect(jsonPath("$[1].text").value("Second"));
    }

    @Test
    void getByPostIdEmpty() throws Exception {
        long postId = createPost();

        mockMvc.perform(get("/api/posts/{postId}/comments", postId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void createComment() throws Exception {
        long postId = createPost();

        CommentDto dto = new CommentDto();
        dto.setText("New comment");

        mockMvc.perform(post("/api/posts/{postId}/comments", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.text").value("New comment"))
                .andExpect(jsonPath("$.postId").value(postId));
    }

    @Test
    void updateComment() throws Exception {
        long postId = createPost();
        long commentId = createComment(postId, "Old text");

        CommentDto dto = new CommentDto();
        dto.setText("Updated text");

        mockMvc.perform(put("/api/posts/{postId}/comments/{id}", postId, commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Updated text"));
    }

    @Test
    void deleteComment() throws Exception {
        long postId = createPost();
        long commentId = createComment(postId, "To delete");

        mockMvc.perform(delete("/api/posts/{postId}/comments/{id}", postId, commentId))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/posts/{postId}/comments", postId))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getById() throws Exception {
        long postId = createPost();
        long commentId = createComment(postId, "My comment");

        mockMvc.perform(get("/api/posts/{postId}/comments/{id}", postId, commentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentId))
                .andExpect(jsonPath("$.text").value("My comment"))
                .andExpect(jsonPath("$.postId").value(postId));
    }

    @Test
    void commentsDeletedWithPost() throws Exception {
        long postId = createPost();
        createComment(postId, "Comment 1");
        createComment(postId, "Comment 2");

        jdbc.update("DELETE FROM posts WHERE id = ?", postId);

        int count = jdbc.queryForObject("SELECT COUNT(*) FROM comments WHERE post_id = ?", Integer.class, postId);
        assertEquals(0, count);
    }

}
