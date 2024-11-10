# News Management System - API Endpoints

## Documentation
- Swagger documentation is available at: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## API Endpoints

### News Endpoints
- **POST /api/news** — Create a news article
- **GET /api/news** — Retrieve a paginated list of news articles
- **GET /api/news/{newsId}/comments** — Retrieve a news article along with its comments
- **PUT /api/news/{newsId}** — Update a news article
- **PATCH /api/news/{newsId}** — Partially update a news article
- **DELETE /api/news/{newsId}** — Delete a news article
- **GET /api/news/search** — Search for news articles by text and title

### Comment Endpoints
- **POST /api/news/{newsId}/comments** — Create a comment for a specific news article
- **GET /api/news/{newsId}/comments/{commentId}** — Retrieve a specific comment
- **PUT /api/news/{newsId}/comments/{commentId}** — Update a specific comment
- **PATCH /api/news/{newsId}/comments/{commentId}** — Partially update a comment
- **DELETE /api/news/{newsId}/comments/{commentId}** — Delete a comment
- **GET /api/news/{newsId}/comments/search** — Search comments by text for a specific news article