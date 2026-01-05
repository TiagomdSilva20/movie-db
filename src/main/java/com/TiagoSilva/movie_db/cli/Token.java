package com.TiagoSilva.movie_db.cli;

public class Token {
    private final String text;
    private final boolean quoted;

    public Token(String text, boolean quoted) {
        this.text = text;
        this.quoted = quoted;
    }

    public String getText() { return text; }
    public boolean isQuoted() { return quoted; }

    @Override
    public String toString() {
        return quoted ? ("\"" + text + "\"") : text;
    }
}
