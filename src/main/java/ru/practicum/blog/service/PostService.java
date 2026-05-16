package ru.practicum.blog.service;

import org.springframework.stereotype.Service;
import ru.practicum.blog.domain.Post;
import ru.practicum.blog.dto.PostDto;
import ru.practicum.blog.dto.PostsList;
import ru.practicum.blog.mapper.PostMapper;
import ru.practicum.blog.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    public PostService(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    public PostDto getById(long id) {
        return postMapper.toDto(postRepository.findById(id));
    }

    public PostsList getAll(String search, int pageNumber, int pageSize) {
        List<String> tags = new ArrayList<>();
        List<String> words = new ArrayList<>();

        if (search != null && !search.isBlank()) {
            for (String token : search.split("\\s+")) {
                if (token.startsWith("#")) {
                    String tag = token.substring(1);
                    if (!tag.isEmpty()) {
                        tags.add(tag);
                    }
                } else {
                    words.add(token);
                }
            }
        }

        String titleSubstring = words.isEmpty() ? null : String.join(" ", words);
        int offset = (pageNumber - 1) * pageSize;

        List<Post> posts = postRepository.findAll(titleSubstring, tags, pageSize, offset);
        int totalCount = postRepository.countAll(titleSubstring, tags);
        int lastPage = Math.max(1, (int) Math.ceil((double) totalCount / pageSize));

        PostsList postsList = new PostsList();
        postsList.setPosts(posts.stream().map(postMapper::toPreviewDto).toList());
        postsList.setHasPrev(pageNumber > 1);
        postsList.setHasNext(pageNumber < lastPage);
        postsList.setLastPage(lastPage);
        return postsList;
    }

    public PostDto create(PostDto postDto) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setText(postDto.getText());
        post.setTags(postDto.getTags());
        return postMapper.toDto(postRepository.save(post));
    }

    public PostDto update(long id, PostDto postDto) {
        Post post = postRepository.findById(id);
        post.setTitle(postDto.getTitle());
        post.setText(postDto.getText());
        post.setTags(postDto.getTags());
        postRepository.update(post);
        return postMapper.toDto(post);
    }

    public void deleteById(long id) {
        postRepository.deleteById(id);
    }

    public int addLike(long id) {
        return postRepository.addLike(id);
    }

    public byte[] getImage(long id) {
        return postRepository.findImage(id);
    }

    public void saveImage(long id, byte[] image) {
        postRepository.saveImage(id, image);
    }
}
