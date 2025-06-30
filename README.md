# ShedLock Demo - Spring Boot Application

A Spring Boot application that demonstrates distributed job scheduling using ShedLock to ensure only one instance runs a scheduled job in a distributed environment.

## Purpose

This application runs a scheduled job at 11:30 PM EST, Monday to Friday, that marks a flag column in the `daily_report_status` table to "EOD" for the current business date.

## Features

- **Distributed Safety**: Uses ShedLock with JDBC to ensure only one instance runs the job
- **Scheduled Execution**: Runs at 11:30 PM EST, Monday to Friday only
- **Idempotency**: Checks if EOD is already marked before updating
- **Database Support**: Configurable for H2 (development) or PostgreSQL/MySQL (production)
- **Database Migration**: Uses Liquibase for schema management
- **REST API**: Optional endpoint to check EOD status
- **Docker Support**: Multi-instance setup with MySQL database

## Technology Stack

- Spring Boot 3.2.0
- Java 17
- ShedLock 5.10.2 (with JDBC provider)
- Liquibase for database migrations
- H2 Database (development) / PostgreSQL/MySQL (production)
- Spring Data JPA
- Spring Scheduling
- Docker & Docker Compose

## Quick Start with Docker (Recommended)

### Prerequisites
- Docker Desktop installed and running
- Git

### 1. Clone and Start
```bash
# Clone the repository
git clone git@github.com:lkq911009/shedlock-demo.git
cd shedlock-demo

# Start all services (MySQL + 3 Spring Boot instances)
./docker-start.sh
```

### 2. Verify Setup
The script will show you all the URLs:
- **Instance 1**: http://localhost:8080
- **Instance 2**: http://localhost:8081  
- **Instance 3**: http://localhost:8082
- **MySQL**: localhost:3306

### 3. Test the Application
```bash
# Check EOD status on different instances
curl http://localhost:8080/eod/status
curl http://localhost:8081/eod/status
curl http://localhost:8082/eod/status

# Check health status
curl http://localhost:8080/actuator/health
```

### 4. Docker Management
```bash
# View logs
./docker-start.sh logs

# Check status
./docker-start.sh status

# Stop services
./docker-start.sh stop

# Restart services
./docker-start.sh restart

# Clean up everything
./docker-start.sh cleanup
```

## Manual Setup (Development)

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- PostgreSQL (optional, for production)

### Running with H2 (Development)

1. Clone the repository
2. Run the application:
   ```bash
   mvn spring-boot:run
   ```

3. The application will start on `http://localhost:8080`

### Running with PostgreSQL (Production)

1. Create a PostgreSQL database:
   ```sql
   CREATE DATABASE shedlock_demo;
   ```

2. Update `application.yml` to use PostgreSQL:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/shedlock_demo
       driver-class-name: org.postgresql.Driver
       username: your_username
       password: your_password
     jpa:
       properties:
         hibernate:
           dialect: org.hibernate.dialect.PostgreSQLDialect
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

## Docker Architecture

### Services
- **MySQL 8.0**: Database server with persistent storage
- **Spring Boot Instance 1**: Port 8080
- **Spring Boot Instance 2**: Port 8081  
- **Spring Boot Instance 3**: Port 8082

### Network
- All services run on a custom Docker network
- MySQL is accessible only within the Docker network
- Application instances can communicate with each other

### Data Persistence
- MySQL data is persisted in a Docker volume
- Application logs are available via `docker-compose logs`

## API Endpoints

### Check EOD Status
```
GET /eod/status
```

Response:
```json
{
  "businessDate": "2024-01-15",
  "isEODMarked": true,
  "currentTimeEST": "2024-01-15T23:30:00-05:00[America/New_York]",
  "statusFlag": "EOD",
  "updatedAt": "2024-01-15T23:30:15"
}
```

### Health Check
```
GET /actuator/health
```

## Scheduled Job Details

- **Schedule**: 11:30 PM EST, Monday to Friday
- **Cron Expression**: `0 30 23 ? * MON-FRI`
- **Timezone**: America/New_York (EST/EDT)
- **Lock Duration**: 1-30 minutes
- **Idempotency**: Checks if EOD is already marked before updating

## Testing the Application

### Docker Testing
```bash
# Start the multi-instance setup
./docker-start.sh

# Test distributed locking by checking logs
docker-compose logs -f shedlock-app-1
docker-compose logs -f shedlock-app-2
docker-compose logs -f shedlock-app-3

# You'll see that only one instance acquires the lock
```

### Manual Testing
```bash
# Check EOD status
curl http://localhost:8080/eod/status

# For testing the scheduled job, modify the cron expression temporarily:
# In EODScheduler.java, change to: @Scheduled(cron = "0 */1 * * * *")
```

## Database Schema

### daily_report_status Table
- `business_date` (DATE, primary key) - The business date
- `status_flag` (VARCHAR) - Status flag (e.g., "EOD")
- `updated_at` (TIMESTAMP) - Last update timestamp

### shedlock Table (ShedLock requirement)
- `name` (VARCHAR(64), primary key) - Lock name
- `lock_until` (TIMESTAMP) - Lock expiration time
- `locked_at` (TIMESTAMP) - Lock acquisition time
- `locked_by` (VARCHAR(255)) - Instance that acquired the lock

## Configuration Options

### Docker Environment Variables
```yaml
SPRING_PROFILES_ACTIVE: docker
SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/shedlock_demo
SPRING_DATASOURCE_USERNAME: shedlock_user
SPRING_DATASOURCE_PASSWORD: shedlock_pass
SERVER_PORT: 8080
```

### ShedLock Configuration
```yaml
shedlock:
  defaults:
    lock-at-most-for: 30m    # Maximum lock duration
    lock-at-least-for: 1m    # Minimum lock duration
```

## Troubleshooting

### Common Docker Issues
1. **Port conflicts**: Make sure ports 8080-8082 and 3306 are available
2. **Docker not running**: Start Docker Desktop first
3. **Build failures**: Check Docker logs with `docker-compose logs`

### Common Application Issues
1. **Database connection**: Ensure MySQL is healthy before starting apps
2. **Lock acquisition**: Check ShedLock logs for lock conflicts
3. **Scheduling**: Verify timezone settings

### Logs and Debugging
```bash
# View all logs
docker-compose logs

# View specific service logs
docker-compose logs shedlock-app-1
docker-compose logs mysql

# Follow logs in real-time
docker-compose logs -f
```

## Benefits of This Implementation

1. **Distributed Safety**: Multiple instances can run without conflicts
2. **Fault Tolerance**: Failed jobs don't prevent future executions
3. **Idempotency**: Safe to run multiple times
4. **Timezone Awareness**: Proper EST/EDT handling
5. **Database Migration**: Version-controlled schema changes
6. **Monitoring**: REST endpoint for status checking
7. **Docker Ready**: Easy deployment with Docker Compose
8. **Scalability**: Easy to add more instances

## Future Enhancements

1. **Metrics**: Add Micrometer for job execution metrics
2. **Alerting**: Integrate with monitoring systems
3. **Retry Logic**: Add retry mechanism for failed jobs
4. **Audit Trail**: Enhanced logging for compliance
5. **Configuration**: External configuration for cron expression
6. **Kubernetes**: Add Kubernetes deployment manifests

## License

This project is for demonstration purposes. 