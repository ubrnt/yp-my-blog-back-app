// TODO: migrate to Spring Boot test configuration
//package ru.practicum.blog.repository;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
//import ru.practicum.blog.configuration.DataSourceConfiguration;
//
//@SpringJUnitConfig(classes = {
//        DataSourceConfiguration.class,
//        JdbcNativePostRepository.class,
//        JdbcNativeCommentRepository.class
//})
//@TestPropertySource(locations = "classpath:test-application.properties")
//abstract class BaseRepositoryTest {
//
//    @Autowired
//    protected JdbcTemplate jdbc;
//
//    @BeforeEach
//    void setUpBase() {
//        jdbc.execute("DELETE FROM post_tags");
//        jdbc.execute("DELETE FROM comments");
//        jdbc.execute("DELETE FROM posts");
//    }
//}
