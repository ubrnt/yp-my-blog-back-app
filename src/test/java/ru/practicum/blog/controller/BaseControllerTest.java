package ru.practicum.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.practicum.blog.configuration.DataSourceConfiguration;
import ru.practicum.blog.configuration.WebConfiguration;

@SpringJUnitConfig(classes = {WebConfiguration.class, DataSourceConfiguration.class})
@WebAppConfiguration
@TestPropertySource(locations = "classpath:test-application.properties")
abstract class BaseControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    protected JdbcTemplate jdbc;

    protected MockMvc mockMvc;
    protected final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUpBase() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        jdbc.execute("DELETE FROM post_tags");
        jdbc.execute("DELETE FROM comments");
        jdbc.execute("DELETE FROM posts");
    }
}
