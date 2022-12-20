package com.finx.commonservice.util;

import com.finx.commonservice.api.Error;

import javax.ws.rs.core.Response;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class OtpUtil {

    private OtpUtil() {
    }

    private static final String VALID_MOBILE_NUMBER_REGEX = "^((0[1-9])|([1-9]))([0-9]{8})$";
    public static boolean isMobileNumberNotValid(String number) {
        Pattern pattern = Pattern.compile(VALID_MOBILE_NUMBER_REGEX);
        Matcher matcher = pattern.matcher(number);
        return !matcher.find();
    }

    public static Response getErrorResponse(Response.Status badRequest, String message) {
        Error error = Error.builder().statusCode(badRequest.getStatusCode())
                .message(message).build();
        return Response.status(badRequest).entity(error).build();
    }
}
