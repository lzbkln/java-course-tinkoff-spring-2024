package edu.java.bot.handlers;

public abstract class LinkHandler {
    private LinkHandler nextHandler;
    private final String pattern;

    public LinkHandler(String pattern) {
        this.pattern = pattern;
    }

    public void setNextHandler(LinkHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public boolean isValid(String link) {
        return link.matches(pattern) || (nextHandler != null && nextHandler.isValid(link));
    }
}
