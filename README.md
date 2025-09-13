# News Service Application

A secure Spring Boot application for managing news with PostgreSQL database, protected by Spring Security with role-based access control.

## Features

- **User Management**: View, add, edit, delete users with role-based permissions
- **News Management**: Create, read, update, delete news articles
- **Category Management**: Organize news by categories
- **Comment System**: Add comments to news articles
- **Security**: Role-based access control with Spring Security
- **Authentication**: User registration and authentication
- **AOP Security**: Automatic permission checking using Aspect-Oriented Programming

## Prerequisites

- Docker
- Java 17 or higher
- Maven

## Installation and Running

1. Clone the repository
2. Start PostgreSQL database with Docker:
   ```bash
   docker-compose up -d
   ```
3. Run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```
4. Open your browser and navigate to: http://localhost:8080/

## Configuration

Default database configuration (can be changed in application.properties):

- Database URL: `jdbc:postgresql://localhost:5432/news_db`
- Username: `postgres`
- Password: `password`

## Security Overview

The application is protected with Spring Security featuring a comprehensive role-based access control system and AOP for permission checking.

### User Roles

- **ROLE_USER** - Regular user with limited permissions
- **ROLE_MODERATOR** - Moderator with extended permissions
- **ROLE_ADMIN** - Administrator with full access

### Access Control

#### Users (UserController)
- **Search all users** - ROLE_ADMIN only
- **Get user by ID** - ROLE_USER, ROLE_ADMIN, ROLE_MODERATOR (ROLE_USER can only access their own profile)
- **Update user** - ROLE_USER, ROLE_ADMIN, ROLE_MODERATOR (ROLE_USER can only update their own profile)
- **Delete user** - ROLE_USER, ROLE_ADMIN, ROLE_MODERATOR (ROLE_USER can only delete their own profile)

#### Categories (CategoryController)
- **Get categories** - ROLE_USER, ROLE_ADMIN, ROLE_MODERATOR
- **Create/Edit/Delete categories** - ROLE_ADMIN, ROLE_MODERATOR only

#### News (NewsController)
- **Get/Create news** - ROLE_USER, ROLE_ADMIN, ROLE_MODERATOR
- **Update news** - Only the author of the news
- **Delete news** - Author or ROLE_ADMIN/ROLE_MODERATOR

#### Comments (CommentController)
- **Get/Create comments** - ROLE_USER, ROLE_ADMIN, ROLE_MODERATOR
- **Update comment** - Only the author of the comment
- **Delete comment** - Author or ROLE_ADMIN/ROLE_MODERATOR

## Test Users

The following test users are created when the application starts:

1. **admin** / **admin** - ROLE_ADMIN
2. **moderator** / **admin** - ROLE_MODERATOR  
3. **user** / **admin** - ROLE_USER

## API Endpoints

### Public Endpoints
- `GET /` - Application information
- `GET /health` - Health check
- `POST /auth/register` - User registration
- `POST /users/register` - User registration (alternative)

### Authentication
- `GET /auth/me` - Get current user information

### Users
- `GET /users` - Get all users (ROLE_ADMIN only)
- `GET /users/{id}` - Get user by ID
- `PUT /users/{id}` - Update user
- `DELETE /users/{id}` - Delete user

### Categories
- `GET /categories` - Get all categories
- `GET /categories/{id}` - Get category by ID
- `POST /categories` - Create category (ROLE_ADMIN, ROLE_MODERATOR)
- `PUT /categories/{id}` - Update category (ROLE_ADMIN, ROLE_MODERATOR)
- `DELETE /categories/{id}` - Delete category (ROLE_ADMIN, ROLE_MODERATOR)

### News
- `GET /news` - Get all news
- `GET /news/{id}` - Get news by ID
- `POST /news` - Create news
- `PUT /news/{id}` - Update news (author only)
- `DELETE /news/{id}` - Delete news

### Comments
- `GET /comments/news/{newsId}` - Get comments for news
- `POST /comments/news/{newsId}` - Create comment
- `PUT /comments/{id}` - Update comment (author only)
- `DELETE /comments/{id}` - Delete comment (author or ROLE_ADMIN/ROLE_MODERATOR)

## Usage

1. Start the application
2. Use test credentials for authentication
3. All requests (except public endpoints) require authentication
4. Permission checking happens automatically via AOP

## Technical Details

- **Password Encryption**: BCrypt for secure password hashing
- **AOP Security**: `@CheckUserAccess` and `@CheckOwner` annotations for permission checking
- **Spring Security**: Method-level security configuration
- **Automatic Owner Checking**: ROLE_USER can only access their own resources

## Database Schema

The application uses the following main entities:
- **Users**: User accounts with roles
- **Categories**: News categories
- **News**: News articles with author information
- **Comments**: Comments on news articles
- **User Roles**: Many-to-many relationship between users and roles

## Security Features

- **Role-based Access Control**: Different permissions for different user roles
- **AOP Permission Checking**: Automatic validation of user permissions
- **Password Security**: BCrypt encryption for all passwords
- **Session Management**: Stateless authentication
- **CSRF Protection**: Disabled for API usage
- **CORS Configuration**: Configured for cross-origin requests

## Development

The application is built with:
- Spring Boot 3.4.8
- Spring Security
- Spring Data JPA
- PostgreSQL
- MapStruct for DTO mapping
- Lombok for reducing boilerplate code
- Maven for dependency management


