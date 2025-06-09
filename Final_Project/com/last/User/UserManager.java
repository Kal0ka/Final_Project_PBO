package com.last.User;

import com.last.Data.*;
import com.last.Database.DatabaseConnection;

import java.sql.*;

public class UserManager {

    public static boolean isEmailExist(String email) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE email = ?");
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean registerUser(User user) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO users (name, email, password, role_id, nim_nip) VALUES (?, ?, ?, ?, ?)"
            );
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword()); // sebaiknya hash
            stmt.setInt(4, user.getRole().getId());
            stmt.setString(5, user.getNimNip());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static User login(String email, String password) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT u.*, r.id AS role_id, r.role_name FROM users u JOIN roles r ON u.role_id = r.id WHERE email = ? AND password = ?"
            );
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Role role = new Role(rs.getInt("role_id"), rs.getString("role_name"));
                return new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        role,
                        rs.getString("nim_nip")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
