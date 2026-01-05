package com.TiagoSilva.movie_db.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DurationFormatTest {

    @Test
    void parseOk() {
        assertEquals(73, DurationFormat.parseToSeconds("00:01:13"));
    }

    @Test
    void parseRejectsBadFormat() {
        assertThrows(IllegalArgumentException.class, () -> DurationFormat.parseToSeconds("13"));
        assertThrows(IllegalArgumentException.class, () -> DurationFormat.parseToSeconds("00:99:00"));
        assertThrows(IllegalArgumentException.class, () -> DurationFormat.parseToSeconds("00:00:99"));
    }

    @Test
    void formatOk() {
        assertEquals("00:01:13", DurationFormat.formatFromSeconds(73));
    }
}