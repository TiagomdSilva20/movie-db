package com.TiagoSilva.movie_db.cli;

import com.TiagoSilva.movie_db.exceptions.BadCommandFormatExcpetion;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ListCommandParser {

    public MovieList parse(List<Token> tokens) {

        MovieList query = new MovieList();

        boolean hasLa = false;
        boolean hasLd = false;

        boolean hasT = false, hasD = false, hasA = false;

        for (int i = 1; i < tokens.size(); i++) {
            String t = tokens.get(i).getText();

            if (t.equals("-v")) {
                query.setVerbose(true);
                continue;
            }

            if (t.equals("-la")) {
                hasLa = true;
                query.setSortOrder(MovieList.SortOrder.LENGTH_ASC);
                continue;
            }
            if (t.equals("-ld")) {
                hasLd = true;
                query.setSortOrder(MovieList.SortOrder.LENGTH_DESC);
                continue;
            }

            if (t.equals("-t") || t.equals("-d") || t.equals("-a")) {
                if (i + 1 >= tokens.size()) {
                    throw new BadCommandFormatExcpetion("Missing parameter after " + t);
                }

                Token param = tokens.get(i + 1);

                if (!param.isQuoted()) {
                    throw new BadCommandFormatExcpetion("Regex parameter after " + t + " must be quoted");
                }

                Pattern p;
                try {
                    p = Pattern.compile(param.getText());
                } catch (PatternSyntaxException ex) {
                    throw new BadCommandFormatExcpetion("Corrupted regex after " + t);
                }

                if (t.equals("-t")) {
                    if (hasT) throw new BadCommandFormatExcpetion("Duplicate -t");
                    query.setTitlePattern(p);
                    hasT = true;
                } else if (t.equals("-d")) {
                    if (hasD) throw new BadCommandFormatExcpetion("Duplicate -d");
                    query.setDirectorPattern(p);
                    hasD = true;
                } else {
                    if (hasA) throw new BadCommandFormatExcpetion("Duplicate -a");
                    query.setActorPattern(p);
                    hasA = true;
                }

                i++;
                continue;
            }

            throw new BadCommandFormatExcpetion("Unknown switch: " + t);
        }

        if (hasLa && hasLd) {
            throw new BadCommandFormatExcpetion("Cannot use both -la and -ld");
        }

        return query;
    }
}