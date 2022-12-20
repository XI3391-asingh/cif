package com.cif.cifservice.core.party.domain;

import com.cif.cifservice.core.party.util.KeyType;

import java.util.List;

public class ConfigData {
    private KeyType key;
    private List<String> values;

    public String getKey() {
        return key.toString();
    }

    public void setKey(String key) {
        this.key = KeyType.valueOf(key);
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

}
