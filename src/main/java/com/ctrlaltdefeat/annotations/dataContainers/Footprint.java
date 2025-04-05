package com.ctrlaltdefeat.annotations.dataContainers;

import java.util.Date;

public class Footprint {
    private final String name;
    private final Date timestamp;

    public Footprint(String name, Date timestamp) {
        this.name = name;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
