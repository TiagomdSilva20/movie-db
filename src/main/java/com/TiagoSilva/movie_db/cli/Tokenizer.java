package com.TiagoSilva.movie_db.cli;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

    public TokenizationResult tokenize(String line) {
        if (line == null) return TokenizationResult.error("Empty input");

        List<Token> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        boolean currentQuoted = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);

            if (ch == '"') {
                inQuotes = !inQuotes;
                if (inQuotes) currentQuoted = true;
                continue;
            }

            if (Character.isWhitespace(ch) && !inQuotes) {
                if (current.length() > 0) {
                    tokens.add(new Token(current.toString(), currentQuoted));
                    current.setLength(0);
                    currentQuoted = false;
                }
            } else {
                current.append(ch);
            }
        }

        if (inQuotes) {
            return TokenizationResult.error("Unclosed quotes");
        }

        if (current.length() > 0) {
            tokens.add(new Token(current.toString(), currentQuoted));
        }

        return TokenizationResult.ok(tokens);
    }
}