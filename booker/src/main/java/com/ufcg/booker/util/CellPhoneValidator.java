package com.ufcg.booker.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CellPhoneValidator {
    static String regex = "^55(\\d{2})9?\\d{8}$|^55(\\d{2})\\d{8}$";
    public static boolean validateCellPhone(String phoneNumber) {
        Boolean isValid = false;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
}
