package com.finx.masterdataservice.api.common;

import com.finx.masterdataservice.api.ResponseApi;
import javax.ws.rs.core.Response;

public interface CustomResponseApi {

    static Response buildResponse(Response.Status status, boolean success, String reason, Object data) {
        Response response;
        response = Response.status(status).entity(
                        ResponseApi.builder()
                                .statusCode(status.getStatusCode())
                                .success(success)
                                .message(reason)
                                .data(data).build())
                .build();
        return response;
    }
}
