package com.example.newsservice.service;

import com.example.newsservice.dto.UserDTO;
import com.example.newsservice.entity.Role;
import com.example.newsservice.entity.User;
import com.example.newsservice.exception.EntityNotFoundException;
import com.example.newsservice.mapper.UserMapper;
import com.example.newsservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toDto);
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userMapper.toDto(user);
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        
        // Шифруем пароль
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        
        // Устанавливаем роль по умолчанию
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Set<Role> defaultRoles = new HashSet<>();
            defaultRoles.add(Role.ROLE_USER);
            user.setRoles(defaultRoles);
        }
        
        return userMapper.toDto(userRepository.save(user));
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setUsername(userDTO.getUsername());
        
        // Шифруем пароль только если он был изменен
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        
        // Обновляем роли если они переданы
        if (userDTO.getRoles() != null) {
            user.setRoles(userDTO.getRoles());
        }
        
        return userMapper.toDto(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}