package com.last.Action;

import com.last.Data.*;
import com.last.Database.DatabaseConnection;
import com.last.User.UserManager;
import com.last.Util.EmailService;

import java.sql.*;

public class AuthManager {

    public static boolean verifyIdentity(String name, String nimNip, int roleId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM pre_registered WHERE name = ? AND nim_nip = ? AND role_id = ?"
            );
            stmt.setString(1, name);
            stmt.setString(2, nimNip);
            stmt.setInt(3, roleId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean register(String name, String email, String password, String nimNip, int roleId) {
        if (UserManager.isEmailExist(email)) return false;
        Role role = new Role(roleId, ""); // Role name tidak dibutuhkan di sini
        User user = new User(0, name, email, password, role, nimNip);

        boolean success = UserManager.registerUser(user);
        if (success) {
            EmailService.sendRegistrationEmail(email, name);
        }

        return success;
    }

    public static User login(String email, String password) {
        return UserManager.login(email, password);
    }
}
