package com.ctrlaltdefeat.annotations.dataContainers;

public class Suggestion {
    private final Entry suggestion;
    private int upvotes;

    public Suggestion(Entry suggestion, int upvotes) {
        this.suggestion = suggestion;
        this.upvotes = upvotes;
    }

    public Entry getSuggestion() {
        return suggestion;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }
}
