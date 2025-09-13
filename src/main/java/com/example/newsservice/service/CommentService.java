package com.example.newsservice.service;

import com.example.newsservice.dto.CommentDTO;
import com.example.newsservice.entity.Comment;
import com.example.newsservice.entity.News;
import com.example.newsservice.entity.User;
import com.example.newsservice.exception.EntityNotFoundException;
import com.example.newsservice.mapper.CommentMapper;
import com.example.newsservice.repository.CommentRepository;
import com.example.newsservice.repository.NewsRepository;
import com.example.newsservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentService(CommentRepository commentRepository, NewsRepository newsRepository, UserRepository userRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.newsRepository = newsRepository;
        this.userRepository = userRepository;
        this.commentMapper = commentMapper;
    }

    public Page<CommentDTO> getCommentsByNewsId(Long newsId, Pageable pageable) {
        return commentRepository.findByNewsId(newsId, pageable).map(commentMapper::toDto);
    }

    public CommentDTO createComment(Long newsId, CommentDTO commentDTO) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new EntityNotFoundException("News not found"));
        User user = userRepository.findById(commentDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Comment comment = commentMapper.toEntity(commentDTO);
        comment.setNews(news);
        comment.setUser(user);
        return commentMapper.toDto(commentRepository.save(comment));
    }

    public CommentDTO updateComment(Long id, CommentDTO commentDTO) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        comment.setContent(commentDTO.getContent());
        return commentMapper.toDto(commentRepository.save(comment));
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}