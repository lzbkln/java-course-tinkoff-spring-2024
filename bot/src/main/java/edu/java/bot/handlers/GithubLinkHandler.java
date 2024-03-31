package edu.java.bot.handlers;


public class GithubLinkHandler extends LinkHandler {
    public GithubLinkHandler() {
        super("https://github.com/([^/]+)/([^/]+)");
    }
}
