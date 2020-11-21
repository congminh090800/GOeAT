package com.example.goeat.auth;

import java.util.regex.Pattern;

public class Validator {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9.a-zA-Z0-9.!#$%&'*+-/=?^_`{|}~]+@[a-zA-Z0-9]+\\.[a-zA-Z]+$";
    private static final String USERNAME_REGEX = "^[a-zA-Z0-9]+$";
    private static final String PASSWORD_REGEX = "^(?=.*?[A-Z])(?=(.*[a-z])+)(?=(.*[\\d])+)(?=(.*[\\W])+)(?!.*\\s).{8,}$";

    public static final String PASSWORD_RULE = "* At least one upper case English letter\n" +
            "* At least one lower case English letter\n" +
            "* At least one digit\n" +
            "* At least one special character\n" +
            "* Minimum eight in length";

    public static final String USERNAME_RULE = "* At least one letter or number\n" +
            "* Every character from the start to the end is a letter or number\n";

    static Pattern emailPattern = Pattern.compile(EMAIL_REGEX);
    static Pattern passwordPattern = Pattern.compile(PASSWORD_REGEX);
    static Pattern usernamePattern = Pattern.compile(USERNAME_REGEX);

    public static boolean isValidEmail(String email){
        return emailPattern.matcher(email).matches();
    }

    public static boolean isValidPassword(String password){
        return passwordPattern.matcher(password).matches();
    }

    public static boolean isValidUsername(String username){
        return usernamePattern.matcher(username).matches();
    }
}
