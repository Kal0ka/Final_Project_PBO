package com.last.User;

import com.last.Data.User;
import com.last.Database.CSVService;

import java.io.IOException;
import java.util.List;

public class UserManager {

    // Path ke file user yang sesungguhnya (di folder Dbase, bisa dibaca dan ditulis)
    private static final String USERS_FILE = "Dbase/users.csv";

    public static boolean isEmailExist(String email) {
        try {
            List<User> users = CSVService.loadUsers(USERS_FILE);
            for (User u : users) {
                if (email.equalsIgnoreCase(u.getEmail())) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean registerUser(User user) {
        try {
            List<User> users = CSVService.loadUsers(USERS_FILE);
            int nextId = users.stream()
                    .mapToInt(u -> Integer.parseInt(String.valueOf(u.getId())))
                    .max()
                    .orElse(0) + 1;

            // Buat user baru dengan ID
            User newUser = new User(
                    String.valueOf(nextId),
                    user.getName(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getRole().getRoleName(),
                    user.getNimNip()
            );

            users.add(newUser);

            // Simpan ke lokasi yang sama (Dbase/users.csv)
            CSVService.saveUsers(USERS_FILE, users);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static User login(String email, String password) {
        try {
            List<User> users = CSVService.loadUsers(USERS_FILE);
            for (User u : users) {
                if (email.equalsIgnoreCase(u.getEmail()) && password.equals(u.getPassword())) {
                    return u;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
