package com.TiagoSilva.movie_db.cli;

import java.util.List;

public class TokenizationResult {
    private final boolean ok;
    private final String error;
    private final List<Token> tokens;

    private TokenizationResult(boolean ok, String error, List<Token> tokens) {
        this.ok = ok;
        this.error = error;
        this.tokens = tokens;
    }

    public static TokenizationResult ok(List<Token> tokens) {
        return new TokenizationResult(true, null, tokens);
    }

    public static TokenizationResult error(String message) {
        return new TokenizationResult(false, message, null);
    }

    public boolean isOk() { return ok; }
    public String getError() { return error; }
    public List<Token> getTokens() { return tokens; }
}

