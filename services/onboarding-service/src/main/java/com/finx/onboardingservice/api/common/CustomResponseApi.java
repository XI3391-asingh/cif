package com.finx.onboardingservice.api.common;

import com.finx.onboardingservice.api.SuccessResponseApi;
import javax.ws.rs.core.Response;

public interface CustomResponseApi {

    static Response buildResponse(Response.Status status, boolean success, String reason, Object data) {
        Response response;
        response = Response.status(status).entity(
                            SuccessResponseApi.builder()
                                .statusCode(status.getStatusCode())
                                .success(success)
                                .message(reason)
                                .data(data).build())
                .build();
        return response;
    }
}
