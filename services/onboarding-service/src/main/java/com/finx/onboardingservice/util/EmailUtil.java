package com.finx.onboardingservice.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class EmailUtil {

    private EmailUtil() {
    }

    private static final String VALID_EMAIL_REGEX =
            "^[-.\\w]+@(?![^.]{0,2}\\.[a-zA-Z]{2,}$)([-a-zA-Z0-9]+\\.)+[a-zA-Z]{2,}$";

    public static boolean isEmailNotValid(String email) {
        Pattern pattern = Pattern.compile(VALID_EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return !matcher.find();
    }
}
