package com.last.Action;

public class ValidationUtil {
    public static boolean isValidNIM(String nim) {
        return nim != null && nim.matches("^\\d{10}$");
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }
}
