# my-blog-back-app

A simple blog app backend.

## Stack

- Java 21, Spring MVC, Spring JDBC
- Deployed as WAR on Tomcat 10
- Postgresql as storage
- JUnit5 + Spring Test as testing frameworks

## Build

### WAR

```bash
./gradlew war
```

## Running locally

```bash
./gradlew cargoRunLocal
```

Backend will be available at `http://localhost:8080`