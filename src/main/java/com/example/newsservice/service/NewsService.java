package com.example.newsservice.service;

import com.example.newsservice.dto.NewsDTO;
import com.example.newsservice.entity.Category;
import com.example.newsservice.entity.News;
import com.example.newsservice.exception.EntityNotFoundException;
import com.example.newsservice.mapper.NewsMapper;
import com.example.newsservice.repository.CategoryRepository;
import com.example.newsservice.repository.NewsRepository;
import com.example.newsservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {

    private final NewsRepository newsRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final NewsMapper newsMapper;

    @Autowired
    public NewsService(NewsRepository newsRepository, CategoryRepository categoryRepository, UserRepository userRepository, NewsMapper newsMapper) {
        this.newsRepository = newsRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.newsMapper = newsMapper;
    }

    public Page<NewsDTO> getAllNews(Pageable pageable) {
        Specification<News> spec = Specification.where(null);
        return newsRepository.findAll(spec, pageable).map(newsMapper::toDto);
    }

    public NewsDTO getNewsById(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("News not found"));
        NewsDTO newsDTO = newsMapper.toDto(news);
        newsDTO.setComments(commentService.getCommentsByNewsId(news.getId(), Pageable.unpaged()).getContent());
        return newsDTO;
    }

    public NewsDTO createNews(NewsDTO newsDTO) {
        News news = newsMapper.toEntity(newsDTO);
        Category category = categoryRepository.findById(newsDTO.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        news.setCategory(category);
        return newsMapper.toDto(newsRepository.save(news));
    }

    public NewsDTO updateNews(Long id, NewsDTO newsDTO) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("News not found"));
        news.setTitle(newsDTO.getTitle());
        news.setDescription(newsDTO.getDescription());
        news.setContent(newsDTO.getContent());
        Category category = categoryRepository.findById(newsDTO.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        news.setCategory(category);
        return newsMapper.toDto(newsRepository.save(news));
    }

    public void deleteNews(Long id) {
        newsRepository.deleteById(id);
    }

    private CommentService commentService;

    @Autowired
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }
}