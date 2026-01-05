package com.TiagoSilva.movie_db.cli;

import java.util.regex.Pattern;

public class MovieList {
    public enum SortOrder {TITLE, LENGTH_ASC, LENGTH_DESC}

    private boolean verbose;
    private Pattern titlePattern;
    private Pattern directorPattern;
    private Pattern actorPattern;
    private SortOrder sortOrder = SortOrder.TITLE;

    public boolean isVerbose() { return verbose;}
    public void setVerbose(boolean verbose) {this.verbose = verbose;}

    public Pattern getTitlePattern(){ return titlePattern;}
    public void  setTitlePattern(Pattern titlePattern) {this.titlePattern = titlePattern;}

    public Pattern getDirectorPattern(){ return directorPattern;}
    public void setDirectorPattern(Pattern directorPattern) {this.directorPattern = directorPattern;}

    public Pattern getActorPattern(){ return actorPattern;}
    public void setActorPattern(Pattern actorPattern) {this.actorPattern = actorPattern;}

    public SortOrder getSortOrder() {return sortOrder;}
    public void setSortOrder(SortOrder sortOrder) {this.sortOrder = sortOrder;}
}

