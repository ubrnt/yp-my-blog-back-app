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
    void deleteById() {
        Post saved = postRepository.save(makePost("To delete", "..."));
        postRepository.deleteById(saved.getId());

        assertThrows(EmptyResultDataAccessException.class,
                () -> postRepository.findById(saved.getId()));
    }
}
