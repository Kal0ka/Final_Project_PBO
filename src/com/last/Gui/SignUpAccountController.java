package com.last.Gui;

import com.last.Action.AuthManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class SignUpAccountController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label statusLabel;

    private String name;
    private String nimNip;
    private int roleId;

    public void setVerificationData(String name, String nimNip, int roleId) {
        this.name = name;
        this.nimNip = nimNip;
        this.roleId = roleId;
    }

    @FXML
    private void handleRegister() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Email dan password tidak boleh kosong.");
            return;
        }

        boolean success = AuthManager.register(name, email, password, nimNip, roleId);
        if (success) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/last/fxml/Login.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) emailField.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setWidth(780);
                stage.setHeight(500);
                stage.centerOnScreen();

            } catch (Exception e) {
                statusLabel.setText("Akun berhasil dibuat, tapi gagal membuka login.");
                e.printStackTrace();
            }
        } else {
            statusLabel.setText("Pendaftaran gagal. Email mungkin sudah digunakan.");
        }
    }
}
