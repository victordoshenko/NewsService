package com.example.newsservice.mapper;

import com.example.newsservice.dto.UserDTO;
import com.example.newsservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO toDto(User user);

    @Mapping(target = "comments", ignore = true)
    User toEntity(UserDTO userDTO);

    List<UserDTO> toDtoList(List<User> users);
}