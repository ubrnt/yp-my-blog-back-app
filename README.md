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

## Tests

```bash
./gradlew test
```

### Unit
- Services: `PostServiceTest`, `CommentServiceTest`
- Mappers: `PostMapperTest`, `CommentMapperTest`

### Integration
- Controllers (MockMvc): `PostControllerTest`, `CommentControllerTest`
- Repositories (H2): `JdbcNativePostRepositoryTest`, `JdbcNativeCommentRepositoryTest`

## Running locally with cargo

```bash
./gradlew cargoRunLocal --info
```

Backend will be available at `http://localhost:8080`. `--info` enables Tomcat logs in the console.

## API

### Posts
| Method | URL | Description |
|--------|-----|-------------|
| GET | `/api/posts?search=&pageNumber=1&pageSize=10` | List posts with search and pagination |
| GET | `/api/posts/{id}` | Get post by id |
| POST | `/api/posts` | Create post |
| PUT | `/api/posts/{id}` | Update post |
| DELETE | `/api/posts/{id}` | Delete post with comments |
| POST | `/api/posts/{id}/likes` | Add like, returns new count |
| GET | `/api/posts/{id}/image` | Get post image |
| PUT | `/api/posts/{id}/image` | Upload post image (multipart) |

### Comments
| Method | URL | Description |
|--------|-----|-------------|
| GET | `/api/posts/{postId}/comments` | List comments for post |
| GET | `/api/posts/{postId}/comments/{id}` | Get comment by id |
| POST | `/api/posts/{postId}/comments` | Create comment |
| PUT | `/api/posts/{postId}/comments/{id}` | Update comment |
| DELETE | `/api/posts/{postId}/comments/{id}` | Delete comment |

## Known frontend 'issues'

### Hits 'GET /api/posts/undefined/comments'

When opening a post, the frontend hits this URL 'GET /api/posts/undefined/comments' with `undefined` instead of a proper id. `CommentController` has an `@ExceptionHandler` that returns an empty `[]` instead of a Tomcat HTML error page.

### Editing a post without selecting an image crashes the frontend

When editing a post without picking a new image file, the frontend throws `Failed to execute 'append' on 'FormData': parameter 2 is not of type 'Blob'`. The error seems to happen on the client side before the request reaches the backend.

### Image not loading after post creation

After creating a post with an image, the frontend redirects to the post page before the image upload finishes. The post page requests the image while it's still being uploaded, so the image doesn't show up. Especially noticeable with large images. Refreshing the page fixes it.

### Page refresh does not work on the post page

When reading post page refresh causing 404 nginx error, to make it work you can re-build frontend app with following changes in nginx config:
- `try_files $uri $uri/ /index.html;` without `=404`, so client-side routing works on page refresh.
- `Dockerfile`: `COPY nginx.conf /etc/nginx/conf.d/default.conf` copy as `default.conf` to override the default nginx config, otherwise it gets ignored.