// TODO: migrate to Spring Boot test configuration
//package ru.practicum.blog.repository;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.EmptyResultDataAccessException;
//import ru.practicum.blog.domain.Post;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class JdbcNativePostRepositoryTest extends BaseRepositoryTest {
//
//    @Autowired
//    private PostRepository postRepository;
//
//    private Post makePost(String title, String text) {
//        Post p = new Post();
//        p.setTitle(title);
//        p.setText(text);
//        return p;
//    }
//
//    @Test
//    void saveAndFindById() {
//        Post saved = postRepository.save(makePost("Test Post", "Hello world"));
//        assertNotNull(saved.getId());
//
//        Post found = postRepository.findById(saved.getId());
//        assertEquals("Test Post", found.getTitle());
//        assertEquals("Hello world", found.getText());
//        assertEquals(0, found.getLikesCount());
//    }
//
//    @Test
//    void updatePost() {
//        Post saved = postRepository.save(makePost("Old title", "Old text"));
//
//        saved.setTitle("New title");
//        saved.setText("New text");
//        postRepository.update(saved);
//
//        Post found = postRepository.findById(saved.getId());
//        assertEquals("New title", found.getTitle());
//        assertEquals("New text", found.getText());
//    }
//
//    @Test
//    void deleteById() {
//        Post saved = postRepository.save(makePost("To delete", "..."));
//        postRepository.deleteById(saved.getId());
//
//        assertThrows(EmptyResultDataAccessException.class,
//                () -> postRepository.findById(saved.getId()));
//    }
//
//    @Test
//    void addLike() {
//        Post saved = postRepository.save(makePost("Likeable", "..."));
//
//        int likes = postRepository.addLike(saved.getId());
//        assertEquals(1, likes);
//
//        likes = postRepository.addLike(saved.getId());
//        assertEquals(2, likes);
//    }
//
//    @Test
//    void saveWithTags() {
//        Post p = makePost("Tagged", "...");
//        p.setTags(List.of("java", "spring"));
//        Post saved = postRepository.save(p);
//
//        Post found = postRepository.findById(saved.getId());
//        assertEquals(2, found.getTags().size());
//        assertTrue(found.getTags().containsAll(List.of("java", "spring")));
//    }
//
//    @Test
//    void updateWithTags() {
//        Post p = makePost("Before", "...");
//        p.setTags(List.of("old"));
//        Post saved = postRepository.save(p);
//
//        saved.setTitle("After");
//        saved.setTags(List.of("new1", "new2"));
//        postRepository.update(saved);
//
//        Post found = postRepository.findById(saved.getId());
//        assertEquals("After", found.getTitle());
//        assertEquals(2, found.getTags().size());
//        assertTrue(found.getTags().containsAll(List.of("new1", "new2")));
//        assertFalse(found.getTags().contains("old"));
//    }
//
//    @Test
//    void findAllWithPagination() {
//        for (int i = 1; i <= 5; i++) {
//            postRepository.save(makePost("Post " + i, "Text"));
//        }
//
//        List<Post> page = postRepository.findAll(null, null, 2, 0);
//        assertEquals(2, page.size());
//
//        int total = postRepository.countAll(null, null);
//        assertEquals(5, total);
//    }
//
//    @Test
//    void findAllWithTags() {
//        Post p = makePost("Tagged post", "...");
//        p.setTags(List.of("java", "spring"));
//        postRepository.save(p);
//
//        List<Post> posts = postRepository.findAll(null, null, 10, 0);
//        assertEquals(1, posts.size());
//        assertTrue(posts.get(0).getTags().containsAll(List.of("java", "spring")));
//    }
//
//    @Test
//    void commentsCount() {
//        Post saved = postRepository.save(makePost("Commented", "..."));
//        jdbc.update("INSERT INTO comments (text, post_id) VALUES (?, ?)", "c1", saved.getId());
//        jdbc.update("INSERT INTO comments (text, post_id) VALUES (?, ?)", "c2", saved.getId());
//
//        Post found = postRepository.findById(saved.getId());
//        assertEquals(2, found.getCommentsCount());
//
//        List<Post> posts = postRepository.findAll(null, null, 10, 0);
//        assertEquals(2, posts.get(0).getCommentsCount());
//    }
//
//    @Test
//    void findAllByTitleSubstring() {
//        postRepository.save(makePost("Java Tutorial", "..."));
//        postRepository.save(makePost("Spring Guide", "..."));
//        postRepository.save(makePost("Java Spring", "..."));
//
//        List<Post> results = postRepository.findAll("java", null, 10, 0);
//        assertEquals(2, results.size());
//
//        int count = postRepository.countAll("java", null);
//        assertEquals(2, count);
//    }
//
//    @Test
//    void findAllByTags() {
//        Post p1 = makePost("Post 1", "...");
//        p1.setTags(List.of("java", "spring"));
//        postRepository.save(p1);
//
//        Post p2 = makePost("Post 2", "...");
//        p2.setTags(List.of("java"));
//        postRepository.save(p2);
//
//        Post p3 = makePost("Post 3", "...");
//        p3.setTags(List.of("python"));
//        postRepository.save(p3);
//
//        List<Post> javaOnly = postRepository.findAll(null, List.of("java"), 10, 0);
//        assertEquals(2, javaOnly.size());
//
//        List<Post> javaAndSpring = postRepository.findAll(null, List.of("java", "spring"), 10, 0);
//        assertEquals(1, javaAndSpring.size());
//        assertEquals("Post 1", javaAndSpring.get(0).getTitle());
//    }
//
//    @Test
//    void findAllByTitleAndTags() {
//        Post p1 = makePost("Java Tutorial", "...");
//        p1.setTags(List.of("java"));
//        postRepository.save(p1);
//
//        Post p2 = makePost("Java Guide", "...");
//        p2.setTags(List.of("python"));
//        postRepository.save(p2);
//
//        List<Post> results = postRepository.findAll("java", List.of("java"), 10, 0);
//        assertEquals(1, results.size());
//        assertEquals("Java Tutorial", results.get(0).getTitle());
//    }
//
//    @Test
//    void saveAndFindImage() {
//        Post saved = postRepository.save(makePost("With image", "..."));
//        byte[] image = {1, 2, 3, 4, 5};
//        postRepository.saveImage(saved.getId(), image);
//
//        byte[] loaded = postRepository.findImage(saved.getId());
//        assertArrayEquals(image, loaded);
//    }
//}
