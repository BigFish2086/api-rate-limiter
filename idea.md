# API Rate Limiter

This is a simple design for an API rate limiter Demo using: 
- Java Spring Boot
- Redis
- Docker / Docker Compose
- PostgreSQL

### Endpoints
| Method | Endpoint                |
|--------|-------------------------|
| POST   | `/api/v1/register`      |
| POST   | `/api/v1/login`         |
| POST   | `/api/v1/logout`        |
| GET    | `/api/v1/subscriptions` |
| POST   | `/api/v1/subscribe`     |
| POST   | `/api/v1/unsubscribe`   |
| GET    | `/api/v1/usage`         |
| GET    | `/api/v1/ping`          |

`api/v1/ping` simulates the main resource of the application which should be rate limited.

### TODO:
- [ ] Add timeouts to `api/v1/login` to prevent brute force attacks.
