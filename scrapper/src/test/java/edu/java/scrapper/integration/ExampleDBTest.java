package edu.java.scrapper.integration;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExampleDBTest extends IntegrationTest {
    @Test
    @DisplayName("Test that all tables exist")
    @Transactional
    @Rollback
    public void testThatAllTablesExist() {
        List<String> expectedTableNames =
            List.of("chats", "links", "linkage", "github_branches", "stackoverflow_question");
        List<String> actualTableNames = jdbcTemplate.query(
            "SELECT table_name FROM information_schema.tables " +
                "WHERE table_schema='public' " +
                "AND table_catalog = current_database()",
            (rs, rowNum) -> rs.getString("table_name")
        );

        assertTrue(actualTableNames.containsAll(expectedTableNames));
    }
}
