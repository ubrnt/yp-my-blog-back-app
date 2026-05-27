// TODO: migrate to Spring Boot test configuration
//package ru.practicum.blog.controller;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.http.MediaType;
//import ru.practicum.blog.dto.PostDto;
//
//import java.util.List;
//
//import static org.hamcrest.Matchers.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//class PostControllerTest extends BaseControllerTest {
//
//    private long createPost(String title, String text, List<String> tags) throws Exception {
//        PostDto dto = new PostDto();
//        dto.setTitle(title);
//        dto.setText(text);
//        dto.setTags(tags);
//
//        String response = mockMvc.perform(post("/api/posts")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(dto)))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        return mapper.readValue(response, PostDto.class).getId();
//    }
//
//    @Test
//    void getAll() throws Exception {
//        createPost("Post 1", "Text 1", List.of());
//        createPost("Post 2", "Text 2", List.of());
//
//        mockMvc.perform(get("/api/posts")
//                        .param("search", "")
//                        .param("pageNumber", "1")
//                        .param("pageSize", "10"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.posts", hasSize(2)));
//    }
//
//    @Test
//    void getAllWithPagination() throws Exception {
//        for (int i = 1; i <= 5; i++) {
//            createPost("Post " + i, "Text", List.of());
//        }
//
//        mockMvc.perform(get("/api/posts")
//                        .param("search", "")
//                        .param("pageNumber", "1")
//                        .param("pageSize", "2"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.posts", hasSize(2)))
//                .andExpect(jsonPath("$.hasPrev").value(false))
//                .andExpect(jsonPath("$.hasNext").value(true))
//                .andExpect(jsonPath("$.lastPage").value(3));
//    }
//
//    @Test
//    void searchByTitle() throws Exception {
//        createPost("Java Tutorial", "text", List.of());
//        createPost("Spring Guide", "text", List.of());
//        createPost("Java Spring", "text", List.of());
//
//        mockMvc.perform(get("/api/posts")
//                        .param("search", "java")
//                        .param("pageNumber", "1")
//                        .param("pageSize", "10"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.posts", hasSize(2)));
//    }
//
//    @Test
//    void searchByTag() throws Exception {
//        createPost("Post 1", "text", List.of("java", "spring"));
//        createPost("Post 2", "text", List.of("python"));
//        createPost("Post 3", "text", List.of("java"));
//
//        mockMvc.perform(get("/api/posts")
//                        .param("search", "#java")
//                        .param("pageNumber", "1")
//                        .param("pageSize", "10"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.posts", hasSize(2)));
//    }
//
//    @Test
//    void searchByMultipleTags() throws Exception {
//        createPost("Post 1", "text", List.of("java", "spring"));
//        createPost("Post 2", "text", List.of("java"));
//        createPost("Post 3", "text", List.of("spring"));
//
//        mockMvc.perform(get("/api/posts")
//                        .param("search", "#java #spring")
//                        .param("pageNumber", "1")
//                        .param("pageSize", "10"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.posts", hasSize(1)))
//                .andExpect(jsonPath("$.posts[0].title").value("Post 1"));
//    }
//
//    @Test
//    void searchByTitleAndTag() throws Exception {
//        createPost("Java Tutorial", "text", List.of("java"));
//        createPost("Java Guide", "text", List.of("python"));
//        createPost("Spring Guide", "text", List.of("java"));
//
//        mockMvc.perform(get("/api/posts")
//                        .param("search", "java #java")
//                        .param("pageNumber", "1")
//                        .param("pageSize", "10"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.posts", hasSize(1)))
//                .andExpect(jsonPath("$.posts[0].title").value("Java Tutorial"));
//    }
//
//    @Test
//    void getById() throws Exception {
//        long id = createPost("Test Post", "Full text here", List.of("tag1"));
//
//        mockMvc.perform(get("/api/posts/{id}", id))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.title").value("Test Post"))
//                .andExpect(jsonPath("$.text").value("Full text here"))
//                .andExpect(jsonPath("$.tags[0]").value("tag1"));
//    }
//
//    @Test
//    void createPost() throws Exception {
//        PostDto dto = new PostDto();
//        dto.setTitle("New Post");
//        dto.setText("Content");
//        dto.setTags(List.of("tag1", "tag2"));
//
//        mockMvc.perform(post("/api/posts")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(dto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").isNumber())
//                .andExpect(jsonPath("$.title").value("New Post"))
//                .andExpect(jsonPath("$.tags", hasSize(2)))
//                .andExpect(jsonPath("$.likesCount").value(0))
//                .andExpect(jsonPath("$.commentsCount").value(0));
//    }
//
//    @Test
//    void updatePost() throws Exception {
//        long id = createPost("Old Title", "Old text", List.of("old"));
//
//        PostDto dto = new PostDto();
//        dto.setTitle("New Title");
//        dto.setText("New text");
//        dto.setTags(List.of("new"));
//
//        mockMvc.perform(put("/api/posts/{id}", id)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(dto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.title").value("New Title"))
//                .andExpect(jsonPath("$.tags[0]").value("new"));
//    }
//
//    @Test
//    void deletePost() throws Exception {
//        long id = createPost("To delete", "text", List.of());
//
//        mockMvc.perform(delete("/api/posts/{id}", id))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get("/api/posts")
//                        .param("search", "")
//                        .param("pageNumber", "1")
//                        .param("pageSize", "10"))
//                .andExpect(jsonPath("$.posts", hasSize(0)));
//    }
//
//    @Test
//    void addLike() throws Exception {
//        long id = createPost("Likeable", "text", List.of());
//
//        mockMvc.perform(post("/api/posts/{id}/likes", id))
//                .andExpect(status().isOk())
//                .andExpect(content().string("1"));
//
//        mockMvc.perform(post("/api/posts/{id}/likes", id))
//                .andExpect(status().isOk())
//                .andExpect(content().string("2"));
//    }
//
//    @Test
//    void saveAndGetImage() throws Exception {
//        long id = createPost("With image", "text", List.of());
//        byte[] image = {1, 2, 3, 4, 5};
//
//        mockMvc.perform(multipart("/api/posts/{id}/image", id)
//                        .file("image", image)
//                        .with(request -> { request.setMethod("PUT"); return request; }))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get("/api/posts/{id}/image", id))
//                .andExpect(status().isOk())
//                .andExpect(header().string("Content-Type", MediaType.IMAGE_PNG_VALUE))
//                .andExpect(content().bytes(image));
//    }
//
//    @Test
//    void getImageNoContent() throws Exception {
//        long id = createPost("No image", "text", List.of());
//
//        mockMvc.perform(get("/api/posts/{id}/image", id))
//                .andExpect(status().isNoContent());
//    }
//}
