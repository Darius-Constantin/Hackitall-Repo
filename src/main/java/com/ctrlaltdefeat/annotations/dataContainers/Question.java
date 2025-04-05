package com.ctrlaltdefeat.annotations.dataContainers;

import java.util.ArrayList;

public class Question {
    private final Entry question;
    private final ArrayList<Entry> replies;

    public Question(Entry question) {
        this.question = question;
        this.replies = new ArrayList<>();
    }

    public Question(Entry question, ArrayList<Entry> replies) {
        this.question = question;
        this.replies = replies;
    }

    public Entry getQuestion() {
        return question;
    }

    public ArrayList<Entry> getReplies() {
        return replies;
    }
}
