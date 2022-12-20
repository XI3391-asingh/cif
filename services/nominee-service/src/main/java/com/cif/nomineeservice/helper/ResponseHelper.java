package com.cif.nomineeservice.helper;


import com.cif.nomineeservice.api.ResponseApi;
import com.cif.nomineeservice.api.ResponseApiMeta;
import com.cif.nomineeservice.api.ResponseApiStatus;
import com.cif.nomineeservice.api.SuccessResponseApi;

public final class ResponseHelper {

    private ResponseHelper() {
    }

    public static ResponseApi buildResponseApi(String requestId, String statusCode, String statusMessage) {
        return ResponseApi.builder()
                .meta(ResponseApiMeta.builder().requestId(requestId)
                        .build())
                .status(ResponseApiStatus.builder()
                        .code(statusCode)
                        .message(statusMessage)
                        .build())
                .build();
    }

    public static <T> SuccessResponseApi buildSuccessResponseApi(String requestId, String statusCode,
                                                                 String statusMessage, T payload) {
        return SuccessResponseApi.builder()
                .meta(ResponseApiMeta.builder().requestId(requestId)
                        .build())
                .status(ResponseApiStatus.builder()
                        .code(statusCode)
                        .message(statusMessage)
                        .build())
                .data(payload)
                .build();
    }
}
