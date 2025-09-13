package com.example.newsservice.controller;

import com.example.newsservice.dto.CommentDTO;
import com.example.newsservice.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@Validated
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/news/{newsId}")
    public ResponseEntity<Page<CommentDTO>> getCommentsByNewsId(@PathVariable Long newsId, Pageable pageable) {
        return ResponseEntity.ok(commentService.getCommentsByNewsId(newsId, pageable));
    }

    @PostMapping("/news/{newsId}")
    public ResponseEntity<CommentDTO> createComment(@PathVariable Long newsId, @Valid @RequestBody CommentDTO commentDTO) {
        return ResponseEntity.ok(commentService.createComment(newsId, commentDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Long id, @Valid @RequestBody CommentDTO commentDTO) {
        return ResponseEntity.ok(commentService.updateComment(id, commentDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}