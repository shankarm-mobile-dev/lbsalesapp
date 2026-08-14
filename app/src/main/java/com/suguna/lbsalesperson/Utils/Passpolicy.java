package com.suguna.lbsalesperson.Utils;

import java.util.regex.Pattern;

public class Passpolicy {
    private static String statusMessage = "";

    public static String getStatusMessage() {
        return statusMessage;
    }

    public static boolean isValid(String empcode, String password) {

        Pattern specailCharPatten = Pattern.compile("[^a-z0-9 ]",
                Pattern.CASE_INSENSITIVE);
        Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
        Pattern lowerCasePatten = Pattern.compile("[a-z ]");
        Pattern digitCasePatten = Pattern.compile("[0-9 ]");

        if (password.length() < 8) {
            statusMessage = "Password lenght must have at least 8 character !!";
            return false;
        }

        if (!specailCharPatten.matcher(password).find()) {
            statusMessage = "Password must have at least one special character !!";
            return false;
        }

        if (!UpperCasePatten.matcher(password).find()) {
            statusMessage = "Password must have at least one uppercase character !!";
            return false;
        }

        if (!lowerCasePatten.matcher(password).find()) {
            statusMessage = "Password must have at least one lowercase character !!";
            return false;
        }

        if (!digitCasePatten.matcher(password).find()) {
            statusMessage = "Password must have at least one numeric number !!";
            return false;
        }

        if (validateContain5ConsChar(empcode,password)) {
            statusMessage = "Password must not have 5 consecutive characters from username !!";
            return false;
        }
        if (validateContainRestrictedPattern(password)) {
            statusMessage = "Password must not contain restricted this patterns list(password,welcome,suguna,QWERTY,12345,ASDFGH,ZXCVBN)";
            return false;
        }
        if (!validateNoRepeats(password)) {
            statusMessage = "Password must not contain any character more than twice consecutively";
            return false;
        }
        /*
         * if (validateContain5ConsChar(empcode, password)) { statusMessage =
         * "Password must not contain any character more than twice consecutively"
         * ; return false; }
         */
        return true;
    }

    public static boolean validateContainRestrictedPattern(String p_password) {
        boolean checkSpecialcharacter = false;
        String[] RestrictedValues = new String[] { "welcome", "suguna",
                "QWERTY", "12345", "ASDFGH", "password", "ZXCVBN" };
        for (int i = 0; i < RestrictedValues.length; i++) {
            if (p_password.toUpperCase().contains(
                    RestrictedValues[i].toUpperCase())) {
                checkSpecialcharacter = true;
                break;
            }
        }
        return checkSpecialcharacter;
    }

    public static boolean validateContain5ConsChar(String p_username,
                                                   String p_password) {
        boolean checkSpecialcharacter = false;
        int length = p_password.length() - 5;
        for (int i = 0; i < length + 1; i++) {
            if (p_username.toUpperCase().contains(
                    p_password.toUpperCase().substring(i, i + 5))) {
                checkSpecialcharacter = true;
                break;
            }
        }
        return checkSpecialcharacter;
    }

    private static boolean validateNoRepeats(String p_password) {
        for (int i = 2; i < p_password.length(); i++) {
            if ((p_password.charAt(i) == p_password.charAt(i - 1))
                    && (p_password.charAt(i) == p_password.charAt(i - 2))) {
                return false;
            }
        }
        return true;
    }



}
