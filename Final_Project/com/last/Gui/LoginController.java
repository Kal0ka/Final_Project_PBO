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
            statusLabel.setText("Login berhasil. Selamat datang, " + user.getName());
            // TODO: Load dashboard.fxml sesuai role
        } else {
            statusLabel.setText("Email atau password salah.");
        }
    }

    @FXML
    public void goToSignUp() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/last/Fxml/signup_verification.fxml"));
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
