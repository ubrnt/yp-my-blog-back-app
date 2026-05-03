package ru.practicum.blog.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.blog.dto.CommentDto;
import ru.practicum.blog.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public List<CommentDto> getByPostId(@PathVariable("postId") long postId) {
        return commentService.getByPostId(postId);
    }

    @GetMapping("/{id}")
    public CommentDto getById(@PathVariable("id") long id) {
        return commentService.getById(id);
    }

    @PostMapping
    public CommentDto create(@PathVariable("postId") long postId, @RequestBody CommentDto commentDto) {
        return commentService.create(postId, commentDto);
    }

    @PutMapping("/{id}")
    public CommentDto update(@PathVariable("id") long id, @RequestBody CommentDto commentDto) {
        return commentService.update(id, commentDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) {
        commentService.deleteById(id);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public List<CommentDto> handleTypeMismatch() {
        return List.of();
    }
}
