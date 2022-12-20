package com.cif.syncerservice.core.syncer.helper;

import com.cif.syncerservice.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class ResponseHelper {

    private ResponseHelper() {
    }

    public static <T> SuccessResponseApi buildSuccessResponse(String statusCode,
                                                              String statusMessage, T data) {
        return SuccessResponseApi.builder().status(ResponseApiStatus.builder().code(statusCode).message(statusMessage).build())
                .data(data != null ? data : new ArrayList<T>()).build().meta(ResponseApiMeta.builder().requestID(UUID.randomUUID().toString()).build());
    }

    public static <T> ResponseApi buildErrorResponse(String statusCode,
                                                     String statusMessage) {
        return ResponseApi.builder().status(ResponseApiStatus.builder().code(statusCode).message(statusMessage).build())
                .build().meta(ResponseApiMeta.builder().requestID(UUID.randomUUID().toString()).build());
    }

    public static <T> ErrorResponseApi buildResponseForErrors(String statusCode,
                                                              List<Object> statusMessage) {
        return ErrorResponseApi.builder().status(ErrorResponseApiStatus.builder().code(statusCode).errors(statusMessage).build())
                .build().meta(ResponseApiMeta.builder().requestID(UUID.randomUUID().toString()).build());
    }

}
