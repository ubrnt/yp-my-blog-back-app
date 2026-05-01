package ru.practicum.blog.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.practicum.blog.domain.Post;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcNativePostRepository implements PostRepository {

    private final JdbcTemplate jdbc;

    public JdbcNativePostRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Post findById(long id) {
        Post post = jdbc.queryForObject(
                "SELECT id, title, text, image, likes_count FROM posts WHERE id = ?",
                this::mapRow, id);
        post.setTags(findTagsByPostId(id));
        return post;
    }

    @Override
    public Post save(Post post) {
        var keyHolder = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO posts (title, text, image, likes_count) VALUES (?, ?, ?, ?)",
                    new String[]{"id"}
            );
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getText());
            ps.setBytes(3, post.getImage());
            ps.setInt(4, post.getLikesCount());
            return ps;
        }, keyHolder);

        post.setId(keyHolder.getKey().longValue());
        saveTags(post.getId(), post.getTags());
        return post;
    }

    @Override
    public void update(Post post) {
        jdbc.update("UPDATE posts SET title = ?, text = ? WHERE id = ?",
                post.getTitle(), post.getText(), post.getId());
        jdbc.update("DELETE FROM post_tags WHERE post_id = ?", post.getId());
        saveTags(post.getId(), post.getTags());
    }

    @Override
    public void deleteById(long id) {
        jdbc.update("DELETE FROM posts WHERE id = ?", id);
    }

    @Override
    public int addLike(long id) {
        jdbc.update("UPDATE posts SET likes_count = likes_count + 1 WHERE id = ?", id);
        return jdbc.queryForObject("SELECT likes_count FROM posts WHERE id = ?", Integer.class, id);
    }

    @Override
    public byte[] findImage(long id) {
        return jdbc.queryForObject("SELECT image FROM posts WHERE id = ?", byte[].class, id);
    }

    @Override
    public void saveImage(long id, byte[] image) {
        jdbc.update("UPDATE posts SET image = ? WHERE id = ?", image, id);
    }

    private void saveTags(long id, List<String> tags) {
        if (tags == null || tags.isEmpty()) return;
        for (String tag : tags) {
            jdbc.update("INSERT INTO post_tags (post_id, tag) VALUES (?, ?)", id, tag);
        }
    }

    private List<String> findTagsByPostId(long id) {
        return jdbc.queryForList("SELECT tag FROM post_tags WHERE post_id = ?", String.class, id);
    }

    private Post mapRow(ResultSet rs, int rowNum) throws SQLException {
        Post post = new Post();
        post.setId(rs.getLong("id"));
        post.setTitle(rs.getString("title"));
        post.setText(rs.getString("text"));
        post.setImage(rs.getBytes("image"));
        post.setLikesCount(rs.getInt("likes_count"));
        return post;
    }
}
