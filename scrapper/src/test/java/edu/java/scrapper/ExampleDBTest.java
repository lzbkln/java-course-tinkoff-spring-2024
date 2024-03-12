package edu.java.scrapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ExampleDBTest extends IntegrationTest {
    @Test
    @DisplayName("Test that chats table exists and can insert and retrieve data")
    @SneakyThrows
    public void testThatChatsTableExistsAndCanInsertAndRetrieveData() {
        Connection connection =
            DriverManager.getConnection(POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword());

        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO chats (chat_id) VALUES ('1')");
        ResultSet resultSet = statement.executeQuery("SELECT * FROM chats");

        String actualChatId = null;
        Timestamp actualCreatedAt = null;
        while (resultSet.next()) {
            actualChatId = resultSet.getString("chat_id");
            actualCreatedAt = resultSet.getTimestamp("created_at");
        }

        assertThat(actualChatId).isEqualTo("1");
        assertThat(actualCreatedAt).isNotNull();

        Instant now = Instant.now();
        Instant oneMinuteAgo = now.minusSeconds(60);
        Instant oneMinuteFromNow = now.plusSeconds(60);

        assertThat(actualCreatedAt.toInstant()).isBetween(oneMinuteAgo, oneMinuteFromNow);
    }

    @Test
    @DisplayName("Test that links table exists and can insert and retrieve data")
    @SneakyThrows
    public void testThatLinksTableExistsAndCanInsertAndRetrieveData() {
        Connection connection =
            DriverManager.getConnection(POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword());

        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO links (url) VALUES ('https://github.com')");
        ResultSet resultSet = statement.executeQuery("SELECT * FROM links");

        String actualUrl = null;
        String actualId = null;
        while (resultSet.next()) {
            actualUrl = resultSet.getString("url");
            actualId = resultSet.getString("link_id");
        }

        assertThat(actualId).isNotNull();
        assertThat(actualUrl).isEqualTo("https://github.com");
    }

    @Test
    @DisplayName("Test that linkage_table exists and can insert and retrieve data")
    @SneakyThrows
    public void testThatLinkageTableExistsAndCanInsertAndRetrieveData() {
        Connection connection =
            DriverManager.getConnection(POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword());

        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO chats (chat_id) VALUES ('2')");
        statement.execute("INSERT INTO links (url) VALUES ('https://example.com')");
        statement.execute("INSERT INTO linkage_table (chat_id, link_id) VALUES ('2', '1')");

        ResultSet resultSet = statement.executeQuery("SELECT * FROM linkage_table");

        Long actualId = null;
        Long actualChatId = null;
        Long actualLinkId = null;
        while (resultSet.next()) {
            actualId = resultSet.getLong("id");
            actualChatId = resultSet.getLong("chat_id");
            actualLinkId = resultSet.getLong("link_id");
        }

        assertThat(actualId).isNotNull();
        assertThat(actualChatId).isEqualTo(2);
        assertThat(actualLinkId).isEqualTo(1);
    }
}
