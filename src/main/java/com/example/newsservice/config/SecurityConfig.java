package com.example.newsservice.config;

import com.example.newsservice.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Публичные эндпоинты
                .requestMatchers("/").permitAll()
                .requestMatchers("/health").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/users/register").permitAll()
                .requestMatchers("/auth/register").permitAll()
                .requestMatchers("/auth/me").authenticated()
                
                // Эндпоинты для пользователей
                .requestMatchers("/users").hasRole("ADMIN")
                .requestMatchers("/users/**").hasAnyRole("USER", "ADMIN", "MODERATOR")
                
                // Эндпоинты для категорий
                .requestMatchers("/categories").hasAnyRole("USER", "ADMIN", "MODERATOR")
                .requestMatchers("/categories/**").hasAnyRole("USER", "ADMIN", "MODERATOR")
                
                // Эндпоинты для новостей
                .requestMatchers("/news").hasAnyRole("USER", "ADMIN", "MODERATOR")
                .requestMatchers("/news/**").hasAnyRole("USER", "ADMIN", "MODERATOR")
                
                // Эндпоинты для комментариев
                .requestMatchers("/comments/**").hasAnyRole("USER", "ADMIN", "MODERATOR")
                
                .anyRequest().authenticated()
            )
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
            .authenticationProvider(authenticationProvider());

        return http.build();
    }
}
