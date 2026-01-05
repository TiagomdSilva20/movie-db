package com.TiagoSilva.movie_db.cli;

import com.TiagoSilva.movie_db.exceptions.BadCommandFormatExcpetion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ListCommandParserTest {

    @Test
    void rejectsBothLengthSortSwitches() {
        Tokenizer tok = new Tokenizer();
        TokenizationResult r = tok.tokenize("l -la -ld");
        ListCommandParser p = new ListCommandParser();
        assertThrows(BadCommandFormatExcpetion.class, () -> p.parse(r.getTokens()));
    }

    @Test
    void rejectsMissingParam() {
        Tokenizer tok = new Tokenizer();
        TokenizationResult r = tok.tokenize("l -d");
        ListCommandParser p = new ListCommandParser();
        assertThrows(BadCommandFormatExcpetion.class, () -> p.parse(r.getTokens()));
    }

    @Test
    void rejectsUnquotedRegex() {
        Tokenizer tok = new Tokenizer();
        TokenizationResult r = tok.tokenize("l -t Die");
        ListCommandParser p = new ListCommandParser();
        assertThrows(BadCommandFormatExcpetion.class, () -> p.parse(r.getTokens()));
    }

    @Test
    void rejectsBadRegex() {
        Tokenizer tok = new Tokenizer();
        TokenizationResult r = tok.tokenize("l -t \"([)\"");
        ListCommandParser p = new ListCommandParser();
        assertThrows(BadCommandFormatExcpetion.class, () -> p.parse(r.getTokens()));
    }
}