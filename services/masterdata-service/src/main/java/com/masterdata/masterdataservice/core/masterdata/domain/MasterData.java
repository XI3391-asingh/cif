package com.masterdata.masterdataservice.core.masterdata.domain;

import java.util.UUID;

public class MasterData {

    private UUID id;
    private String name;

    public MasterData() {
    }

    public MasterData(String name) {
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
