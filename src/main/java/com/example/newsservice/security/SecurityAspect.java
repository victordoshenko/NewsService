package com.example.newsservice.security;

import com.example.newsservice.exception.UnauthorizedAccessException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class SecurityAspect {

    @Before("@annotation(com.example.newsservice.security.CheckOwner)")
    public void checkOwner(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Object[] args = joinPoint.getArgs();
        Long entityId = (Long) args[0];

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = principal.getId();

        if (!isOwner(method.getName(), entityId, userId)) {
            throw new UnauthorizedAccessException("You are not authorized to perform this action");
        }
    }

    private boolean isOwner(String methodName, Long entityId, Long userId) {
        if ("updateNews".equals(methodName) || "deleteNews".equals(methodName)) {
            return isNewsOwner(entityId, userId);
        } else if ("updateComment".equals(methodName) || "deleteComment".equals(methodName)) {
            return isCommentOwner(entityId, userId);
        }
        return false;
    }

    private boolean isNewsOwner(Long newsId, Long userId) {
        // Logic to check if the news belongs to the user
        return true; // Replace with actual logic
    }

    private boolean isCommentOwner(Long commentId, Long userId) {
        // Logic to check if the comment belongs to the user
        return true; // Replace with actual logic
    }
}