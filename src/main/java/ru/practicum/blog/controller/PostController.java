package ru.practicum.blog.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.practicum.blog.dto.PostDto;
import ru.practicum.blog.dto.PostsList;
import ru.practicum.blog.service.PostService;

import java.io.IOException;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public PostsList getAll(@RequestParam("search") String search,
                            @RequestParam("pageNumber") int pageNumber,
                            @RequestParam("pageSize") int pageSize) {
        return postService.getAll(search, pageNumber, pageSize);
    }

    @GetMapping("/{id}")
    public PostDto getById(@PathVariable("id") long id) {
        return postService.getById(id);
    }

    @PostMapping
    public PostDto create(@RequestBody PostDto postDto) {
        return postService.create(postDto);
    }

    @PutMapping("/{id}")
    public PostDto update(@PathVariable("id") long id, @RequestBody PostDto postDto) {
        return postService.update(id, postDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) {
        postService.deleteById(id);
    }

    @PostMapping("/{id}/likes")
    public int addLike(@PathVariable("id") long id) {
        return postService.addLike(id);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable("id") long id) {
        byte[] image = postService.getImage(id);
        if (image == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE)
                .body(image);
    }

    @PutMapping("/{id}/image")
    public void saveImage(@PathVariable("id") long id, @RequestParam("image") MultipartFile image) throws IOException {
        postService.saveImage(id, image.getBytes());
    }
}
