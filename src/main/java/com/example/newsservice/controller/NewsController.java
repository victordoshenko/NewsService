package com.example.newsservice.controller;

import com.example.newsservice.dto.NewsDTO;
import com.example.newsservice.security.CheckOwner;
import com.example.newsservice.security.CheckUserAccess;
import com.example.newsservice.security.UserPrincipal;
import com.example.newsservice.service.NewsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/news")
@Validated
public class NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    @CheckUserAccess(roles = {"ROLE_USER", "ROLE_ADMIN", "ROLE_MODERATOR"})
    public ResponseEntity<Page<NewsDTO>> getAllNews(Pageable pageable) {
        return ResponseEntity.ok(newsService.getAllNews(pageable));
    }

    @GetMapping("/{id}")
    @CheckUserAccess(roles = {"ROLE_USER", "ROLE_ADMIN", "ROLE_MODERATOR"})
    public ResponseEntity<NewsDTO> getNewsById(@PathVariable Long id) {
        return ResponseEntity.ok(newsService.getNewsById(id));
    }

    @PostMapping
    @CheckUserAccess(roles = {"ROLE_USER", "ROLE_ADMIN", "ROLE_MODERATOR"})
    public ResponseEntity<NewsDTO> createNews(@Valid @RequestBody NewsDTO newsDTO, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return ResponseEntity.ok(newsService.createNews(newsDTO, userPrincipal.getId()));
    }

    @PutMapping("/{id}")
    @CheckOwner(roles = {"ROLE_USER", "ROLE_ADMIN", "ROLE_MODERATOR"}, allowSelf = true)
    public ResponseEntity<NewsDTO> updateNews(@PathVariable Long id, @Valid @RequestBody NewsDTO newsDTO) {
        return ResponseEntity.ok(newsService.updateNews(id, newsDTO));
    }

    @DeleteMapping("/{id}")
    @CheckOwner(roles = {"ROLE_USER", "ROLE_ADMIN", "ROLE_MODERATOR"}, allowSelf = true)
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }
}