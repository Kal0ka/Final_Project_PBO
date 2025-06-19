package com.last.Util;

import com.last.Data.Role;

public class Connector {
    private static int currentUserId;
    private static String currentUserName;
    private static Role currentUserRole;

    public static void login(int userId, String userName, Role role) {
        currentUserId = userId;
        currentUserName = userName;
        currentUserRole = role;
    }

    public static int getCurrentUserId() {
        return currentUserId;
    }

    public static String getCurrentUserName() {
        return currentUserName;
    }

    public static Role getCurrentUserRole() {
        return currentUserRole;
    }

    public static void logout() {
        currentUserId = 0;
        currentUserName = null;
        currentUserRole = null;
    }
}
