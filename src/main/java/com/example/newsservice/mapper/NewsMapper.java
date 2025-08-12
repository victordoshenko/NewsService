package com.example.newsservice.mapper;

import com.example.newsservice.dto.NewsDTO;
import com.example.newsservice.entity.News;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NewsMapper {

    NewsMapper INSTANCE = Mappers.getMapper(NewsMapper.class);

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "comments", ignore = true)
    NewsDTO toDto(News news);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "comments", ignore = true)
    News toEntity(NewsDTO newsDTO);

    List<NewsDTO> toDtoList(List<News> newsList);
}