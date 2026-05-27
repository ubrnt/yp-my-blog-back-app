package ru.practicum.blog.mapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.practicum.blog.domain.Post;
import ru.practicum.blog.dto.PostDto;

@Component
public class PostMapper {

    private final int previewLength;

    public PostMapper(@Value("${app.post.text.preview-length}") int previewLength) {
        this.previewLength = previewLength;
    }

    public PostDto toDto(Post post) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setText(post.getText());
        dto.setTags(post.getTags());
        dto.setLikesCount(post.getLikesCount());
        dto.setCommentsCount(post.getCommentsCount());
        return dto;
    }

    public PostDto toPreviewDto(Post post) {
        PostDto dto = toDto(post);
        String text = post.getText();
        if (text != null && text.length() > previewLength) {
            dto.setText(text.substring(0, previewLength) + "\u2026");
        }
        return dto;
    }
}
