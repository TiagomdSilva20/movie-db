package com.TiagoSilva.movie_db.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DurationFormat {
    private DurationFormat() {}

    private static final Pattern P = Pattern.compile("^(\\d{2,}):(\\d{2}):(\\d{2})$");

    public static int parseToSeconds(String hhmmss) {
        if (hhmmss == null) throw new IllegalArgumentException("null");

        Matcher m = P.matcher(hhmmss.trim());
        if (!m.matches()) {
            throw new IllegalArgumentException("Bad input format (hh:mm:ss)");
        }

        int hh = Integer.parseInt(m.group(1));
        int mm = Integer.parseInt(m.group(2));
        int ss = Integer.parseInt(m.group(3));

        if (mm < 0 || mm > 59 || ss < 0 || ss > 59) {
            throw new IllegalArgumentException("Bad input format (hh:mm:ss)");
        }

        long total = (long) hh * 3600L + (long) mm * 60L + ss;
        if (total > Integer.MAX_VALUE) throw new IllegalArgumentException("Duration too large");
        return (int) total;
    }

    public static String formatFromSeconds(int seconds) {
        if (seconds < 0) seconds = 0;
        int hh = seconds / 3600;
        int mm = (seconds % 3600) / 60;
        int ss = seconds % 60;
        return String.format("%02d:%02d:%02d", hh, mm, ss);
    }
}