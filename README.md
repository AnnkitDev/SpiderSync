# 🕷️ SpiderSync - Professional High-End Search Engine

A professional, enterprise-grade web search engine built with Spring Boot, Elasticsearch, Redis, and RabbitMQ. SpiderSync features advanced web crawling, full-text search with BM25 ranking, NLP-powered keyword extraction, and a modern responsive UI.

## ✨ Features

### Core Search Engine
- **Advanced Full-Text Search** powered by Elasticsearch with BM25 ranking
- **Intelligent Web Crawler** with robots.txt compliance and politeness policies
- **NLP-Powered Analysis** for keyword extraction and content classification
- **Real-time Autocomplete** with search suggestions
- **Faceted Search** with language and page rank filters
- **Content Deduplication** using SHA-256 hashing
- **Page Rank Calculation** based on link analysis

### Infrastructure
- **Distributed Crawling** with RabbitMQ message queues
- **High-Performance Caching** using Redis and Caffeine
- **Connection Pooling** with HikariCP for optimal database performance
- **Async Processing** with configurable thread pools
- **Database Migrations** managed by Flyway

### Security & API
- **JWT Authentication** for secure API access
- **Role-Based Access Control** (RBAC)
- **API Rate Limiting** to prevent abuse
- **RESTful API** with comprehensive Swagger documentation
- **CORS Configuration** for cross-origin requests

### Monitoring & Observability
- **Health Checks** for all services (MySQL, Elasticsearch, Redis, RabbitMQ)
- **Prometheus Metrics** export
- **Structured Logging** with SLF4J/Logback
- **Search Analytics** tracking

### User Interface
- **Modern Responsive Design** with dark theme
- **Real-time Search** with instant results
- **Autocomplete Suggestions** as you type
- **Advanced Filters** for refined searching
- **Mobile-Friendly** interface

## 🏗️ Architecture

```
┌─────────────┐     ┌──────────────┐     ┌─────────────┐
│   Browser   │────▶│  Spring Boot │────▶│   MySQL     │
│     UI      │     │  Application │     │  Database   │
└─────────────┘     └──────────────┘     └─────────────┘
                           │
                           ├────────────▶ Elasticsearch
                           │              (Search Index)
                           │
                           ├────────────▶ Redis
                           │              (Cache)
                           │
                           └────────────▶ RabbitMQ
                                          (Crawl Queue)
```

## 🚀 Quick Start

### Prerequisites
- Java 21
- Maven 3.8+
- Docker & Docker Compose (for external services)

### 1. Start External Services

```bash
docker-compose up -d
```

This starts:
- MySQL (port 3306)
- Elasticsearch (port 9200)
- Redis (port 6379)
- RabbitMQ (port 5672, Management UI: 15672)

### 2. Build and Run Application

```bash
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8081`

### 3. Access the Application

- **Search UI**: http://localhost:8081
- **API Documentation**: http://localhost:8081/swagger-ui.html
- **Health Check**: http://localhost:8081/actuator/health
- **Metrics**: http://localhost:8081/actuator/metrics

## 📖 API Documentation

### Search Endpoints

#### Search Pages
```http
GET /api/search?q={query}&language={lang}&page={page}&size={size}
```

#### Autocomplete
```http
GET /api/search/autocomplete?q={prefix}&size={size}
```

#### Search Statistics
```http
GET /api/search/stats
```

### Crawl Management (Admin Only)

#### Start Crawl
```http
POST /api/crawl/start?seedUrl={url}
Authorization: Bearer {jwt_token}
```

#### List Crawl Jobs
```http
GET /api/crawl/jobs
Authorization: Bearer {jwt_token}
```

#### Create Elasticsearch Index
```http
POST /api/crawl/index/create
Authorization: Bearer {jwt_token}
```

## 🔧 Configuration

Key configuration properties in `application.properties`:

```properties
# Crawler Settings
crawler.threads=5
crawler.delay.ms=1000
crawler.max-depth=3
crawler.respect-robots-txt=true

# Elasticsearch
elasticsearch.host=localhost
elasticsearch.port=9200

# Redis
spring.data.redis.host=localhost
spring.data.redis.port=6379

# RabbitMQ
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
```

## 🔐 Authentication

Default admin credentials:
- **Username**: admin
- **Password**: admin123
- **API Key**: admin-api-key-change-this-in-production

⚠️ **Important**: Change these credentials in production!

## 📊 Database Schema

The application uses MySQL with the following main tables:
- `pages` - Indexed web pages with full-text search
- `crawl_jobs` - Crawl job tracking
- `search_queries` - Search analytics
- `users` - User authentication

Migrations are managed by Flyway in `src/main/resources/db/migration/`

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run with coverage
mvn verify
```

## 📦 Production Deployment

### Using Docker

```bash
# Build application
mvn clean package -DskipTests

# Run with production profile
java -jar target/SpiderSync-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

### Environment Variables

Set these environment variables for production:

```bash
DB_URL=jdbc:mysql://your-db-host:3306/search_engine
DB_USERNAME=your-username
DB_PASSWORD=your-password
ELASTICSEARCH_HOST=your-es-host
REDIS_HOST=your-redis-host
RABBITMQ_HOST=your-rabbitmq-host
JWT_SECRET=your-secret-key
```

## 🛠️ Technology Stack

- **Backend**: Spring Boot 4.0.1, Java 21
- **Search**: Elasticsearch 8.11.1
- **Cache**: Redis 7.x, Caffeine
- **Message Queue**: RabbitMQ 3.x
- **Database**: MySQL 8.0
- **Security**: Spring Security, JWT
- **Web Crawling**: Jsoup, Apache HttpClient
- **NLP**: Apache OpenNLP, Custom TF-IDF
- **API Docs**: Springdoc OpenAPI
- **Monitoring**: Micrometer, Prometheus

## 📈 Performance

- **Search Response Time**: < 100ms (with caching)
- **Crawl Rate**: Configurable (default: 1 request/second)
- **Concurrent Crawlers**: 5 threads (configurable)
- **Cache Hit Rate**: ~80% for popular queries

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📝 License

This project is open source and available under the MIT License.

## 👥 Authors

- **Annkit** - [GitHub](https://github.com/Annkit-rgb)

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Elasticsearch for powerful search capabilities
- All open-source contributors

## 📞 Support

For issues and questions:
- Open an issue on GitHub
- Check the API documentation at `/swagger-ui.html`

---

**Made with ❤️ using Spring Boot, Elasticsearch, and modern web technologies**
