package edu.java.bot.handlers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DirtiesContext
public class LinkHandlerTest {

    @Test
    @DisplayName("Test GitHub link handler with valid link")
    public void testGithubLinkHandlerWithValidLink() {
        LinkHandler githubLinkHandler = new GithubLinkHandler();

        boolean isValid = githubLinkHandler.isValid("https://github.com/user/repository");

        assertTrue(isValid);
    }

    @Test
    @DisplayName("Test GitHub link handler with not valid link")
    public void testGithubLinkHandlerWithNotValidLink() {
        LinkHandler githubLinkHandler = new GithubLinkHandler();

        boolean isValid = githubLinkHandler.isValid("https://github.ru/user/repository");

        assertFalse(isValid);
    }

    @Test
    @DisplayName("Test StackOverflow link handler with valid link")
    public void testStackOverflowLinkHandlerWithValidLink() {
        LinkHandler stackOverflowLinkHandler = new StackOverflowLinkHandler();

        boolean isValid = stackOverflowLinkHandler.isValid("https://stackoverflow.com/questions/544535/test");

        assertTrue(isValid);
    }

    @Test
    @DisplayName("Test Stack Overflow link handler with invalid link")
    public void testStackOverflowLinkHandlerWithInvalidLink() {
        LinkHandler stackOverflowLinkHandler = new StackOverflowLinkHandler();

        boolean isValid = stackOverflowLinkHandler.isValid("https://stakoverflow.com/questions");

        assertFalse(isValid);
    }
}
