package com.last.Action;

import com.last.Data.User;
import com.last.Database.CSVService;
import com.last.User.UserManager;
import com.last.Util.EmailService;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class AuthManager {

    // File CSV berada di folder Dbase (read-write)
    private static final String USERS_FILE = "Dbase/users.csv";
    private static final String PRE_REGISTERED_FILE = "Dbase/pre_registered.csv";

    public static boolean verifyIdentity(String name, String nimNip, int roleId) {
        try (BufferedReader reader = new BufferedReader(new FileReader(PRE_REGISTERED_FILE))) {
            List<String> lines = reader.lines().collect(Collectors.toList());

            for (String line : lines.subList(1, lines.size())) { // Skip header
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String prName = parts[1].trim();       // name
                    String prNimNip = parts[2].trim();     // nim_nip
                    int prRoleId = Integer.parseInt(parts[3].trim()); // role_id

                    if (prName.equalsIgnoreCase(name) &&
                            prNimNip.equalsIgnoreCase(nimNip) &&
                            prRoleId == roleId) {
                        return true;
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean register(String name, String email, String password, String nimNip, int roleId) {
        try {
            if (UserManager.isEmailExist(email)) return false;

            List<User> users = CSVService.loadUsers(USERS_FILE);
            int nextId = users.stream().mapToInt(User::getId).max().orElse(0) + 1;

            User newUser = new User(
                    String.valueOf(nextId),
                    name,
                    email,
                    password,
                    String.valueOf(roleId),
                    nimNip // tetap String
            );

            users.add(newUser);
            CSVService.saveUsers(USERS_FILE, users); // Simpan langsung ke file utama
            EmailService.sendRegistrationEmail(email, name);
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