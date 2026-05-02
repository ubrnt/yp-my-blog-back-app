package ru.practicum.blog.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.practicum.blog.domain.Comment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class JdbcNativeCommentRepository implements CommentRepository {

    private final JdbcTemplate jdbc;

    public JdbcNativeCommentRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Comment save(Comment comment) {
        var keyHolder = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO comments (text, post_id) VALUES (?, ?)",
                    new String[]{"id"}
            );
            ps.setString(1, comment.getText());
            ps.setLong(2, comment.getPostId());
            return ps;
        }, keyHolder);

        comment.setId(keyHolder.getKey().longValue());
        return comment;
    }

    @Override
    public Comment findById(long id) {
        return jdbc.queryForObject("SELECT id, text, post_id FROM comments WHERE id = ?",
                this::mapRow, id);
    }

    @Override
    public void deleteById(long id) {
        jdbc.update("DELETE FROM comments WHERE id = ?", id);
    }

    private Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
        Comment comment = new Comment();
        comment.setId(rs.getLong("id"));
        comment.setText(rs.getString("text"));
        comment.setPostId(rs.getLong("post_id"));
        return comment;
    }
}
