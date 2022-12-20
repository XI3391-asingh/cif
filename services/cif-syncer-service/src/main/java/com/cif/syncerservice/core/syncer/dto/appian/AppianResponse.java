package com.cif.syncerservice.core.syncer.dto.appian;

import java.util.List;

public class AppianResponse {

    private List<AppianRequest> success;
    private List<AppianRequest> error;

    public List<AppianRequest> getSuccess() {
        return success;
    }

    public void setSuccess(List<AppianRequest> success) {
        this.success = success;
    }

    public void setError(List<AppianRequest> error) {
        this.error = error;
    }

}
