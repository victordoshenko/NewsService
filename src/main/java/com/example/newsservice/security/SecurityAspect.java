package com.example.newsservice.security;

import com.example.newsservice.entity.Comment;
import com.example.newsservice.entity.News;
import com.example.newsservice.entity.Role;
import com.example.newsservice.exception.UnauthorizedAccessException;
import com.example.newsservice.repository.CommentRepository;
import com.example.newsservice.repository.NewsRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

@Aspect
@Component
public class SecurityAspect {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Before("@annotation(com.example.newsservice.security.CheckOwner)")
    public void checkOwner(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CheckOwner annotation = method.getAnnotation(CheckOwner.class);

        Object[] args = joinPoint.getArgs();
        Long entityId = (Long) args[0];

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = principal.getId();
        Set<Role> userRoles = principal.getAuthorities().stream()
                .map(auth -> Role.valueOf(auth.getAuthority()))
                .collect(java.util.stream.Collectors.toSet());

        // Проверяем роли
        if (annotation.roles().length > 0) {
            boolean hasRequiredRole = Arrays.stream(annotation.roles())
                    .anyMatch(role -> userRoles.contains(Role.valueOf(role)));
            if (!hasRequiredRole) {
                throw new UnauthorizedAccessException("You don't have required role");
            }
        }

        // Проверяем владельца
        if (annotation.allowSelf()) {
            // ROLE_USER может работать только со своими ресурсами
            if (userRoles.contains(Role.ROLE_USER) && !userRoles.contains(Role.ROLE_ADMIN) && !userRoles.contains(Role.ROLE_MODERATOR)) {
                if (!isOwner(method.getName(), entityId, userId)) {
                    throw new UnauthorizedAccessException("You can only modify your own resources");
                }
            }
        }
    }

    @Before("@annotation(com.example.newsservice.security.CheckUserAccess)")
    public void checkUserAccess(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CheckUserAccess annotation = method.getAnnotation(CheckUserAccess.class);

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = principal.getId();
        Set<Role> userRoles = principal.getAuthorities().stream()
                .map(auth -> Role.valueOf(auth.getAuthority()))
                .collect(java.util.stream.Collectors.toSet());

        // Проверяем роли
        if (annotation.roles().length > 0) {
            boolean hasRequiredRole = Arrays.stream(annotation.roles())
                    .anyMatch(role -> userRoles.contains(Role.valueOf(role)));
            if (!hasRequiredRole) {
                throw new UnauthorizedAccessException("You don't have required role");
            }
        }

        // Проверяем доступ к пользователю
        if (annotation.allowSelf()) {
            Object[] args = joinPoint.getArgs();
            Long targetUserId = (Long) args[0];
            
            // ROLE_USER может работать только со своим профилем
            if (userRoles.contains(Role.ROLE_USER) && !userRoles.contains(Role.ROLE_ADMIN) && !userRoles.contains(Role.ROLE_MODERATOR)) {
                if (!userId.equals(targetUserId)) {
                    throw new UnauthorizedAccessException("You can only access your own profile");
                }
            }
        }
    }

    private boolean isOwner(String methodName, Long entityId, Long userId) {
        if ("updateNews".equals(methodName) || "deleteNews".equals(methodName)) {
            return isNewsOwner(entityId, userId);
        } else if ("updateComment".equals(methodName)) {
            return isCommentOwner(entityId, userId);
        } else if ("deleteComment".equals(methodName)) {
            // Для удаления комментариев ROLE_USER может удалять только свои,
            // ROLE_ADMIN и ROLE_MODERATOR могут удалять любые
            UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Set<Role> userRoles = principal.getAuthorities().stream()
                    .map(auth -> Role.valueOf(auth.getAuthority()))
                    .collect(java.util.stream.Collectors.toSet());
            
            if (userRoles.contains(Role.ROLE_ADMIN) || userRoles.contains(Role.ROLE_MODERATOR)) {
                return true; // Админы и модераторы могут удалять любые комментарии
            } else {
                return isCommentOwner(entityId, userId); // Обычные пользователи только свои
            }
        }
        return false;
    }

    private boolean isNewsOwner(Long newsId, Long userId) {
        News news = newsRepository.findById(newsId).orElse(null);
        return news != null && news.getUser() != null && news.getUser().getId().equals(userId);
    }

    private boolean isCommentOwner(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        return comment != null && comment.getUser() != null && comment.getUser().getId().equals(userId);
    }
}