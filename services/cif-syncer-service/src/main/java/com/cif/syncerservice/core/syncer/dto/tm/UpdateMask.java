package com.cif.syncerservice.core.syncer.dto.tm;


import java.util.List;

public class UpdateMask {

    public UpdateMask() {
    }

    private List<String> paths;

    public UpdateMask(List<String> paths) {
        this.paths = paths;
    }

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }
}
