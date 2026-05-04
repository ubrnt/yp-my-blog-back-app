# my-blog-back-app

A simple blog app backend.

## Stack

- Java 21, Spring MVC, Spring JDBC
- Deployed as WAR on Tomcat 10
- H2 as storage
- JUnit5 + Spring Test as testing frameworks

## Build

### WAR

```bash
./gradlew war
```

## Running locally with cargo

```bash
./gradlew cargoRunLocal
```

Backend will be available at `http://localhost:8080`

## Known frontend 'issues'

### Hits 'GET /api/posts/undefined/comments'

When opening a post, the frontend hits this URL 'GET /api/posts/undefined/comments' with `undefined` instead of a proper id. `CommentController` has an `@ExceptionHandler` that returns an empty `[]` instead of a Tomcat HTML error page.

### Page refresh does not work on the post page

When reading post page refresh causing 404 nginx error, to make it work you can re-build frontend app with following changes in nginx config:
- `try_files $uri $uri/ /index.html;` without `=404`, so client-side routing works on page refresh.
- `Dockerfile`: `COPY nginx.conf /etc/nginx/conf.d/default.conf` copy as `default.conf` to override the default nginx config, otherwise it gets ignored.