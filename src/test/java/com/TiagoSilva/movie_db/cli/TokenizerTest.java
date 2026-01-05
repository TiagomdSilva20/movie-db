package com.TiagoSilva.movie_db.cli;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenizerTest {

    @Test
    void tokenizesQuotedRegexAsSingleToken() {
        Tokenizer t = new Tokenizer();
        TokenizationResult r = t.tokenize("l -t \"Die .*\" -v");
        assertTrue(r.isOk());
        assertEquals(4, r.getTokens().size());
        assertEquals("Die .*", r.getTokens().get(2).getText());
        assertTrue(r.getTokens().get(2).isQuoted());
    }

    @Test
    void failsOnUnclosedQuotes() {
        Tokenizer t = new Tokenizer();
        TokenizationResult r = t.tokenize("l -t \"Die .* -v");
        assertFalse(r.isOk());
    }
}