package com.sensei.backend.util;

import org.springframework.util.StringUtils;

public class ValidationUtils {

    public static boolean isValidName(String name) {
        return StringUtils.hasText(name) && name.length() >= 2;
    }

    // Add more validation methods as needed
}
