// LoginController.java
package com.last.Gui;

import com.last.Action.AuthManager;
import com.last.User.SessionManager;
import com.last.Data.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    @FXML
    public void handleLogin() {
        String email = emailField.getText();
        String pass = passwordField.getText();

        User user = AuthManager.login(email, pass);
        if (user != null) {
            SessionManager.setCurrentUser(user);
            String roleName = user.getRole().getRoleName();

            try {
                Stage stage = (Stage) emailField.getScene().getWindow();
                double width = stage.getWidth();
                double height = stage.getHeight();

                Parent root;
                if (roleName.equalsIgnoreCase("MAHASISWA")) {
                    root = FXMLLoader.load(getClass().getResource("/com/last/fxml/Mahasiswa.fxml"));
                } else if (roleName.equalsIgnoreCase("ADMIN")) {
                    root = FXMLLoader.load(getClass().getResource("/com/last/fxml/Admin.fxml"));
                } else {
                    statusLabel.setText("Role belum didukung.");
                    return;
                }

                stage.setScene(new Scene(root, width, height));
            } catch (Exception e) {
                e.printStackTrace();
                statusLabel.setText("Gagal memuat dashboard.");
            }
        } else {
            statusLabel.setText("Email atau password salah.");
        }
    }

    @FXML
    public void goToSignUp() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/last/fxml/Signup_Verification.fxml"));
            Stage stage = (Stage) emailField.getScene().getWindow();

            double width = stage.getWidth();
            double height = stage.getHeight();

            stage.setScene(new Scene(root, width, height));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

