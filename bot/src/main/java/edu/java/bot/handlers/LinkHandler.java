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
        if (link.matches(pattern)) {
            return true;
        } else if (nextHandler != null) {
            return nextHandler.isValid(link);
        }
        return false;
    }
}
