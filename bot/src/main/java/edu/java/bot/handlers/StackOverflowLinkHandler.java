package edu.java.bot.handlers;

public class StackOverflowLinkHandler extends LinkHandler {
    public StackOverflowLinkHandler() {
        super("https?://stackoverflow.com/questions/(\\d+)/.*");
    }
}
