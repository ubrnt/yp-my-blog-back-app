package ru.practicum.blog.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.blog.domain.Post;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PostMapperTest {

    private final PostMapper mapper = new PostMapper(128);

    private Post makePost(String text) {
        Post post = new Post();
        post.setId(1L);
        post.setTitle("Title");
        post.setText(text);
        post.setTags(List.of("java", "spring"));
        post.setLikesCount(5);
        post.setCommentsCount(3);
        return post;
    }

    @Test
    void toDtoCopiesAllFields() {
        Post post = makePost("Some text");
        var dto = mapper.toDto(post);

        assertEquals(1L, dto.getId());
        assertEquals("Title", dto.getTitle());
        assertEquals("Some text", dto.getText());
        assertEquals(List.of("java", "spring"), dto.getTags());
        assertEquals(5, dto.getLikesCount());
        assertEquals(3, dto.getCommentsCount());
    }

    @Test
    void toPreviewDtoShortTextUnchanged() {
        Post post = makePost("Short text");
        var dto = mapper.toPreviewDto(post);

        assertEquals("Short text", dto.getText());
    }

    @Test
    void toPreviewDtoTruncatesLongText() {
        String longText = "a".repeat(200);
        Post post = makePost(longText);
        var dto = mapper.toPreviewDto(post);

        assertEquals(129, dto.getText().length());
        assertTrue(dto.getText().endsWith("\u2026"));
        assertEquals("a".repeat(128) + "\u2026", dto.getText());
    }

    @Test
    void toPreviewDtoExactLengthNotTruncated() {
        String exactText = "b".repeat(128);
        Post post = makePost(exactText);
        var dto = mapper.toPreviewDto(post);

        assertEquals(exactText, dto.getText());
    }
}
