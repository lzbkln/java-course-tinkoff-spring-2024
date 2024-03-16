package edu.java.scrapper.repository.jdbc;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExampleDBTest extends IntegrationTest {
    @Test
    @DisplayName("Test that all tables exist")
    public void testThatAllTablesExist() {
        List<String> expectedTableNames = List.of("chats", "links", "linkage");
        List<String> actualTableNames = jdbcTemplate.query(
            "SELECT table_name FROM information_schema.tables " +
                "WHERE table_schema='public' " +
                "AND table_catalog = current_database()",
            (rs, rowNum) -> rs.getString("table_name")
        );

        assertTrue(actualTableNames.containsAll(expectedTableNames));
    }
}
