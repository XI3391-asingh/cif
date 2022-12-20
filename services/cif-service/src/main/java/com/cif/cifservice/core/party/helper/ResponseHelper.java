package com.cif.cifservice.core.party.helper;


import com.cif.cifservice.api.*;
import org.slf4j.MDC;

import java.util.ArrayList;
import java.util.List;

public final class ResponseHelper {

    private ResponseHelper() {
    }

    public static <T> SuccessResponseApi buildSuccessResponse(String statusCode,
                                                              String statusMessage, T data) {
        String requestId = MDC.get("X-Request-ID");
        return SuccessResponseApi.builder().status(ResponseApiStatus.builder().code(statusCode).message(statusMessage).build())
                .data(data != null ? data : new ArrayList<T>()).build().meta(ResponseApiMeta.builder().requestID(requestId).build());
    }

    public static <T> ResponseApi buildErrorResponse(String statusCode,
                                                     String statusMessage) {
        String requestId = MDC.get("X-Request-ID");
        return ResponseApi.builder().status(ResponseApiStatus.builder().code(statusCode).message(statusMessage).build())
                .build().meta(ResponseApiMeta.builder().requestID(requestId).build());
    }

    public static <T> ErrorResponseApi buildResponseForErrors(String statusCode,
                                                              List<Object> statusMessage) {
        String requestId = MDC.get("X-Request-ID");
        return ErrorResponseApi.builder().status(ErrorResponseApiStatus.builder().code(statusCode).errors(statusMessage).build())
                .build().meta(ResponseApiMeta.builder().requestID(requestId).build());
    }

}
