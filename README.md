# Study Planner
[![Backend CI](https://github.com/VeriteIgiraneza/study-planner/actions/workflows/backend-ci.yml/badge.svg)](https://github.com/VeriteIgiraneza/study-planner/actions/workflows/backend-ci.yml)

A planner for university students that automatically schedules assignment work into their available time and learns from how long tasks actually take.

> Status: In development. Student sign-up is complete with validation, BCrypt password hashing, and automated tests. Login and scheduling features are next.

## The problem

Students know their deadlines but not when they'll actually do the work. Assignments pile up, time estimates are off, and work gets scheduled late at night or not at all.

## Implemented

- Student sign-up API with input validation and duplicate-email protection
- Passwords hashed with BCrypt; hashes never returned by the API
- Standardized error responses (RFC 9457 Problem Details)
- Database schema managed with Liquibase migrations
- Unit tests (JUnit, Mockito) and CI with GitHub Actions

## Planned features

- Assignments with due dates and estimated hours
- Weekly availability windows, so work is never scheduled during sleep, class, or work
- Automatic scheduling of work sessions before each deadline
- Overload warnings when there's more work than free time
- Focus timer that tracks actual time spent
- Estimates that improve based on past accuracy
- Mobile app with login and reminders

## Tech stack

- **Backend:** Java 21, Spring Boot, Maven
- **Database:** PostgreSQL, Liquibase
- **Testing & CI:** JUnit, Mockito, GitHub Actions
- **Mobile (planned):** React Native, Expo, TypeScript

## Project structure
```text
study-planner/
├── backend/   Spring Boot API
└── mobile/    React Native app (coming soon)
```

## Running locally

### Prerequisites
- Java 21
- PostgreSQL

### Database setup
```sql
CREATE USER planner_user WITH PASSWORD 'your_password';
CREATE DATABASE study_planner OWNER planner_user;
```

### Run the backend

**macOS / Linux**
```bash
cd backend
export DB_USERNAME=planner_user
export DB_PASSWORD=your_password
./mvnw spring-boot:run
```

**Windows (PowerShell)**
```powershell
cd backend
$env:DB_USERNAME="planner_user"
$env:DB_PASSWORD="your_password"
.\mvnw.cmd spring-boot:run
```

**Windows (Command Prompt)**
```cmd
cd backend
set DB_USERNAME=planner_user
set DB_PASSWORD=your_password
mvnw.cmd spring-boot:run
```

> Environment variables set this way only last for the current terminal window.

#### Keeping variables between sessions (optional)

- **macOS / Linux:** add the `export` lines to your shell profile (`~/.zshrc` on macOS, `~/.bashrc` on most Linux systems), then open a new terminal.
- **Windows:** run `setx DB_USERNAME "planner_user"` and `setx DB_PASSWORD "your_password"`, then open a new terminal (`setx` doesn't affect the current window). You can also add them under *System Properties -> Environment Variables*.
- **IDE:** set them in your IDE's run configuration (for example, IntelliJ IDEA or VS Code launch settings).

Liquibase creates the tables automatically on startup!


## API

### `POST /api/auth/signup`

Request:
```json
{
  "firstName": "Jane",
  "lastName": "Doe",
  "email": "jane@example.com",
  "password": "password123"
}
```

Responses:
- `201 Created` with the new user (no password data)
- `400 Bad Request` with per-field validation errors
- `409 Conflict` if the email is already registered

## Running tests

```bash
cd backend
./mvnw test
```