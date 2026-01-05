# Movie Database Console Application

A simple Java console application for managing movies with actors and directors. Built with Spring Boot and H2 database.

## What It Does

Manage a movie database from the command line:
- List movies with filtering and sorting
- Add new people (actors/directors) and movies
- Delete people from the database

**Business Rules:**
- Actors can be directors and vice versa
- Cannot delete directors who have movies

## Technologies

- Java 21
- Spring Boot 4.0.1 (with Spring Data JPA)
- H2 Database (in-memory)
- Gradle

## How to Run

### Using IntelliJ
1. Open project in IntelliJ
2. Run `MovieDbApplication.java`

## Commands

```
l [switches]                list movies
   -v                       show actors
   -t "REGEX"               filter by title
   -d "REGEX"               filter by director
   -a "REGEX"               filter by actor
   -la                      sort by length (shortest first)
   -ld                      sort by length (longest first)

a -p                        add person
a -m                        add movie

d -p <name>                 delete person

help                        show commands
back                        cancel (only in prompts)
exit                        quit
```

## Examples

**List all movies:**
```
> l
Star Wars by George Lucas, 02:01:00
```

**List with actors:**
```
> l -v
Star Wars by George Lucas, 02:01:00
  Starring:
    - Mark Hamill
    - Harrison Ford
```

**Filter by title:**
```
> l -t "Star.*"
```

**Add a person:**
```
> a -p
Name: Tom Hanks
Nationality: American
```

**Add a movie:**
```
> a -m
Title: Forrest Gump
Length: 02:22:00
Director: Robert Zemeckis
Starring: Tom Hanks
Starring: exit
```

## Project Structure

```
Presentation Layer (CLI)
    ↓
Service Layer (Business Logic)
    ↓
Repository Layer (Data Access)
    ↓
Database (H2)
```

**Key Components:**
- `ConsoleRunner` - Main console loop
- `CommandDispatcher` - Routes commands to services
- `MovieService` / `PersonService` - Business logic
- `MovieRepository` / `PersonRepository` - Database access
- Custom exceptions for error handling

## Notes

- Database recreates on each startup (all data lost on exit)
- Movie length stored in seconds, displayed as hh:mm:ss
- Regex must be quoted: `"Star.*"`
- Person names are case-sensitive

---

**Author:** Tiago Silva

