package com.example.newsservice.controller;

import com.example.newsservice.dto.CommentDTO;
import com.example.newsservice.security.CheckOwner;
import com.example.newsservice.security.CheckUserAccess;
import com.example.newsservice.security.UserPrincipal;
import com.example.newsservice.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    @CheckUserAccess(roles = {"ROLE_USER", "ROLE_ADMIN", "ROLE_MODERATOR"})
    public ResponseEntity<Page<CommentDTO>> getCommentsByNewsId(@PathVariable Long newsId, Pageable pageable) {
        return ResponseEntity.ok(commentService.getCommentsByNewsId(newsId, pageable));
    }

    @PostMapping("/news/{newsId}")
    @CheckUserAccess(roles = {"ROLE_USER", "ROLE_ADMIN", "ROLE_MODERATOR"})
    public ResponseEntity<CommentDTO> createComment(@PathVariable Long newsId, @Valid @RequestBody CommentDTO commentDTO, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return ResponseEntity.ok(commentService.createComment(newsId, commentDTO, userPrincipal.getId()));
    }

    @PutMapping("/{id}")
    @CheckOwner(roles = {"ROLE_USER", "ROLE_ADMIN", "ROLE_MODERATOR"}, allowSelf = true)
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Long id, @Valid @RequestBody CommentDTO commentDTO) {
        return ResponseEntity.ok(commentService.updateComment(id, commentDTO));
    }

    @DeleteMapping("/{id}")
    @CheckOwner(roles = {"ROLE_USER", "ROLE_ADMIN", "ROLE_MODERATOR"}, allowSelf = true)
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}