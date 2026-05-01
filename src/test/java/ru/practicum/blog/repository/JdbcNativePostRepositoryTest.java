package ru.practicum.blog.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.blog.configuration.DataSourceConfiguration;
import ru.practicum.blog.domain.Post;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = {DataSourceConfiguration.class, JdbcNativePostRepository.class})
@TestPropertySource(locations = "classpath:test-application.properties")
class JdbcNativePostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private JdbcTemplate jdbc;

    @BeforeEach
    void setUp() {
        jdbc.execute("DELETE FROM posts");
    }

    private Post makePost(String title, String text) {
        Post p = new Post();
        p.setTitle(title);
        p.setText(text);
        return p;
    }

    @Test
    void saveAndFindById() {
        Post saved = postRepository.save(makePost("Test Post", "Hello world"));
        assertNotNull(saved.getId());

        Post found = postRepository.findById(saved.getId());
        assertEquals("Test Post", found.getTitle());
        assertEquals("Hello world", found.getText());
        assertEquals(0, found.getLikesCount());
    }

    @Test
    void updatePost() {
        Post saved = postRepository.save(makePost("Old title", "Old text"));

        saved.setTitle("New title");
        saved.setText("New text");
        postRepository.update(saved);

        Post found = postRepository.findById(saved.getId());
        assertEquals("New title", found.getTitle());
        assertEquals("New text", found.getText());
    }

    @Test
    void deleteById() {
        Post saved = postRepository.save(makePost("To delete", "..."));
        postRepository.deleteById(saved.getId());

        assertThrows(EmptyResultDataAccessException.class,
                () -> postRepository.findById(saved.getId()));
    }

    @Test
    void addLike() {
        Post saved = postRepository.save(makePost("Likeable", "..."));

        int likes = postRepository.addLike(saved.getId());
        assertEquals(1, likes);

        likes = postRepository.addLike(saved.getId());
        assertEquals(2, likes);
    }

    @Test
    void saveWithTags() {
        Post p = makePost("Tagged", "...");
        p.setTags(List.of("java", "spring"));
        Post saved = postRepository.save(p);

        Post found = postRepository.findById(saved.getId());
        assertEquals(2, found.getTags().size());
        assertTrue(found.getTags().containsAll(List.of("java", "spring")));
    }

    @Test
    void updateWithTags() {
        Post p = makePost("Before", "...");
        p.setTags(List.of("old"));
        Post saved = postRepository.save(p);

        saved.setTitle("After");
        saved.setTags(List.of("new1", "new2"));
        postRepository.update(saved);

        Post found = postRepository.findById(saved.getId());
        assertEquals("After", found.getTitle());
        assertEquals(2, found.getTags().size());
        assertTrue(found.getTags().containsAll(List.of("new1", "new2")));
        assertFalse(found.getTags().contains("old"));
    }

    @Test
    void saveAndFindImage() {
        Post saved = postRepository.save(makePost("With image", "..."));
        byte[] image = {1, 2, 3, 4, 5};
        postRepository.saveImage(saved.getId(), image);

        byte[] loaded = postRepository.findImage(saved.getId());
        assertArrayEquals(image, loaded);
    }
}
