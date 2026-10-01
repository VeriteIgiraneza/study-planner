# Study Planner

A planner for university students that automatically schedules assignment work into their available time and learns from how long tasks actually take.

> Status: In development. Backend foundation is in place; features are being built incrementally.

## The problem

Students know their deadlines but not when they'll actually do the work. Assignments pile up, time estimates are off, and work gets scheduled late at night or not at all.

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
- **Mobile (planned):** React Native, Expo, TypeScript

## Project structure
study-planner/
├── backend/ Spring Boot API
└── mobile/ React Native app (coming soon)

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

Liquibase creates the tables automatically on startup!