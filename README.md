# News Application

A simple Spring Boot application for managing news with PostgreSQL database.

## Features
- View all users, news, categories, comments
- Add users, news, categories, comments
- Edit users, news, categories, comments
- Delete users, news, categories, comments

## Prerequisites
- Docker
- Java 17 or higher
- Maven

## Installation and Running

1. Clone the repository
2. Start PostgreSQL database with Docker:
   ```bash
   docker-compose up -d
3. Run the Spring Boot application:
   ```bash
   mvn spring-boot:run
4. Open your browser and navigate to: http://localhost:8080/

## Configuration

Default database configuration (can be changed in application.properties):

Database URL: jdbc:postgresql://localhost:5432/news_db

Username: postgres

Password: password

## Usage
View all news: GET /news

## API Endpoints
GET /news - Show all contacts

GET /users - Show all users

GET /categories - Show all categories

POST /comments - Show all comments

## Run Application
1. Run command:
   ```bash
   mvn spring-boot:run
2. Open your browser at: http://localhost:8080/
