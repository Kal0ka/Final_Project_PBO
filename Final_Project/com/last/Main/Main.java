package com.last.Main;

import com.last.Action.AuthManager;
import com.last.Data.User;
import com.last.User.SessionManager;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("==== SISTEM PERPUSTAKAAN ====");
        System.out.println("1. Sign Up");
        System.out.println("2. Sign In");
        System.out.print("Pilih opsi: ");
        int opsi = Integer.parseInt(scanner.nextLine());

        if (opsi == 1) {
            System.out.println("--- SIGN UP ---");
            System.out.print("Nama: ");
            String name = scanner.nextLine();
            System.out.print("NIM/NIP: ");
            String nimNip = scanner.nextLine();
            System.out.print("Role (1=Mahasiswa, 2=Staff): ");
            int roleId = Integer.parseInt(scanner.nextLine());

            boolean valid = AuthManager.verifyIdentity(name, nimNip, roleId);
            if (!valid) {
                System.out.println("Identitas tidak valid! Tidak ditemukan dalam daftar praregistrasi.");
                return;
            }

            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            boolean success = AuthManager.register(name, email, password, nimNip, roleId);
            if (success) {
                System.out.println("Registrasi berhasil!");
            } else {
                System.out.println("Registrasi gagal! Mungkin email sudah terdaftar.");
            }

        } else if (opsi == 2) {
            System.out.println("--- SIGN IN ---");
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            User user = AuthManager.login(email, password);
            if (user != null) {
                SessionManager.setCurrentUser(user);
                System.out.println("Login berhasil. Selamat datang, " + user.getName() + "!");
            } else {
                System.out.println("Login gagal. Email atau password salah.");
            }
        }

        scanner.close();
    }
}
