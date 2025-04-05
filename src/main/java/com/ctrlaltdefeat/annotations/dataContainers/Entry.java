package com.ctrlaltdefeat.annotations.dataContainers;

public class Entry {
    private final Footprint footprint;
    private final String text;

    public Entry(final Footprint footprint, final String text) {
        this.footprint = footprint;
        this.text = text;
    }

    public Footprint getFootprint() {
        return footprint;
    }

    public String getText() {
        return text;
    }
}
