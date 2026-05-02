package ru.practicum.blog.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.blog.configuration.DataSourceConfiguration;
import ru.practicum.blog.domain.Comment;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = {DataSourceConfiguration.class, JdbcNativeCommentRepository.class})
@TestPropertySource(locations = "classpath:test-application.properties")
class JdbcNativeCommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private JdbcTemplate jdbc;

    @BeforeEach
    void setUp() {
        jdbc.execute("DELETE FROM comments");
        jdbc.execute("DELETE FROM posts");
    }

    private long createPost() {
        jdbc.update("INSERT INTO posts (title, text, likes_count) VALUES ('test', 'text', 0)");
        return jdbc.queryForObject("SELECT MAX(id) FROM posts", Long.class);
    }

    private Comment makeComment(String text, long postId) {
        Comment c = new Comment();
        c.setText(text);
        c.setPostId(postId);
        return c;
    }

    @Test
    void saveAndFindById() {
        long postId = createPost();
        Comment saved = commentRepository.save(makeComment("Nice post!", postId));
        assertNotNull(saved.getId());

        Comment found = commentRepository.findById(saved.getId());
        assertEquals("Nice post!", found.getText());
        assertEquals(postId, found.getPostId());
    }

    @Test
    void findByPostId() {
        long postId = createPost();
        commentRepository.save(makeComment("First", postId));
        commentRepository.save(makeComment("Second", postId));

        List<Comment> comments = commentRepository.findByPostId(postId);
        assertEquals(2, comments.size());
    }

    @Test
    void updateComment() {
        long postId = createPost();
        Comment saved = commentRepository.save(makeComment("Old text", postId));

        saved.setText("Updated text");
        commentRepository.update(saved);

        Comment found = commentRepository.findById(saved.getId());
        assertEquals("Updated text", found.getText());
    }

    @Test
    void deleteComment() {
        long postId = createPost();
        Comment saved = commentRepository.save(makeComment("To delete", postId));
        commentRepository.deleteById(saved.getId());

        assertThrows(EmptyResultDataAccessException.class,
                () -> commentRepository.findById(saved.getId()));
    }
}
