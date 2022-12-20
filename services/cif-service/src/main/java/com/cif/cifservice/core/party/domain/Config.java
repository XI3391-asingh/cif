package com.cif.cifservice.core.party.domain;

import com.cif.cifservice.core.party.util.ConfigType;

import java.util.List;

public class Config {
    private ConfigType type;
    private List<ConfigData> configData;

    public String getType() {
        return type.toString();
    }

    public void setType(String type) {
        this.type = ConfigType.valueOf(type);
    }

    public List<ConfigData> getConfigData() {
        return configData;
    }

    public void setConfigData(List<ConfigData> configData) {
        this.configData = configData;
    }
}
