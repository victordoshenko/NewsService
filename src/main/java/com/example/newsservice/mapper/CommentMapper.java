package com.example.newsservice.mapper;

import com.example.newsservice.dto.CommentDTO;
import com.example.newsservice.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "newsId", ignore = true)
    CommentDTO toDto(Comment comment);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "news", ignore = true)
    Comment toEntity(CommentDTO commentDTO);

    List<CommentDTO> toDtoList(List<Comment> comments);
}