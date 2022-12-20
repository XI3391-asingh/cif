package com.finx.sampleservice.core.task.domain;

import java.util.UUID;

public class Task {

    private UUID id;
    private String name;

    public Task() {
    }

    public Task(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
