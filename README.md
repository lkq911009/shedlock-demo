# ShedLock Demo - Spring Boot Application

A Spring Boot application that demonstrates distributed job scheduling using ShedLock to ensure only one instance runs a scheduled job in a distributed environment.

## Purpose

This application runs a scheduled job at 11:30 PM EST, Monday to Friday, that marks a flag column in the `daily_report_status` table to "EOD" for the current business date.

## Features

- **Distributed Safety**: Uses ShedLock with JDBC to ensure only one instance runs the job
- **Scheduled Execution**: Runs at 11:30 PM EST, Monday to Friday only
- **Idempotency**: Checks if EOD is already marked before updating
- **Database Support**: Configurable for H2 (development) or PostgreSQL (production)
- **Database Migration**: Uses Liquibase for schema management
- **REST API**: Optional endpoint to check EOD status

## Technology Stack

- Spring Boot 3.2.0
- Java 17
- ShedLock 5.10.2 (with JDBC provider)
- Liquibase for database migrations
- H2 Database (development) / PostgreSQL (production)
- Spring Data JPA
- Spring Scheduling

## Project Structure

```
src/
├── main/
│   ├── java/com/example/shedlockdemo/
│   │   ├── ShedLockDemoApplication.java    # Main application class
│   │   ├── controller/
│   │   │   └── EODController.java          # REST controller
│   │   ├── entity/
│   │   │   └── DailyReportStatus.java      # JPA entity
│   │   ├── repository/
│   │   │   └── DailyReportStatusRepository.java # Repository interface
│   │   ├── scheduler/
│   │   │   └── EODScheduler.java           # Scheduled job
│   │   └── service/
│   │       └── EODService.java             # Business logic
│   └── resources/
│       ├── application.yml                 # Configuration
│       └── db/changelog/                   # Liquibase changelogs
│           ├── db.changelog-master.xml
│           └── changes/
│               ├── 001-create-daily-report-status-table.xml
│               └── 002-create-shedlock-table.xml
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

## Setup and Running

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

## Scheduled Job Details

- **Schedule**: 11:30 PM EST, Monday to Friday
- **Cron Expression**: `0 30 23 ? * MON-FRI`
- **Timezone**: America/New_York (EST/EDT)
- **Lock Duration**: 1-30 minutes
- **Idempotency**: Checks if EOD is already marked before updating

## Testing the Application

### Manual Testing

1. Start the application
2. Check the current status:
   ```bash
   curl http://localhost:8080/eod/status
   ```

3. For testing the scheduled job, you can temporarily modify the cron expression in `EODScheduler.java` to run more frequently:
   ```java
   @Scheduled(cron = "0 */1 * * * *", zone = "America/New_York") // Every minute
   ```

### H2 Console (Development)

Access the H2 console at `http://localhost:8080/h2-console` with:
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (empty)

## Distributed Deployment

To run multiple instances:

1. Ensure all instances point to the same database
2. Each instance will attempt to acquire the lock
3. Only one instance will successfully execute the job
4. Other instances will skip execution

## Configuration Options

### ShedLock Configuration
```yaml
shedlock:
  defaults:
    lock-at-most-for: 30m    # Maximum lock duration
    lock-at-least-for: 1m    # Minimum lock duration
```

### Logging Configuration
```yaml
logging:
  level:
    com.example.shedlockdemo: DEBUG
    net.javacrumbs.shedlock: DEBUG
```

## Troubleshooting

### Common Issues

1. **Job not running**: Check if the current time is within the scheduled window (11:30 PM EST, Mon-Fri)
2. **Database connection issues**: Verify database configuration in `application.yml`
3. **Lock acquisition failures**: Check if another instance is already running the job

### Logs

The application provides detailed logging for:
- ShedLock operations
- Scheduled job execution
- Database operations
- EOD status changes

## License

This project is for demonstration purposes. 