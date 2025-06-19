// SignUpVerificationController.java
package com.last.Gui;

import com.last.Action.AuthManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class SignUpVerificationController {

    @FXML private TextField nameField, nimNipField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll("Mahasiswa", "Staff");
    }

    @FXML
    private void handleVerify() {
        String name = nameField.getText().trim();
        String nimNip = nimNipField.getText().trim();
        String selectedRole = roleComboBox.getValue();

        if (name.isEmpty() || nimNip.isEmpty() || selectedRole == null) {
            errorLabel.setText("Semua field harus diisi.");
            return;
        }

        int roleId = selectedRole.equals("Mahasiswa") ? 1 : 2;

        if (AuthManager.verifyIdentity(name, nimNip, roleId)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/last/fxml/Signup_Account.fxml"));
                Parent root = loader.load();

                SignUpAccountController controller = loader.getController();
                controller.setVerificationData(name, nimNip, roleId);

                Stage stage = (Stage) nameField.getScene().getWindow();
                double width = stage.getWidth();
                double height = stage.getHeight();

                stage.setScene(new Scene(root, width, height));
            } catch (Exception e) {
                errorLabel.setText("Gagal memuat halaman pembuatan akun.");
                e.printStackTrace();
            }
        } else {
            errorLabel.setText("Data tidak terverifikasi. Coba periksa kembali.");
        }
    }

    @FXML
    private void goToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/last/fxml/Login.fxml"));
            Stage stage = (Stage) nameField.getScene().getWindow();

            double width = stage.getWidth();
            double height = stage.getHeight();

            stage.setScene(new Scene(root, width, height));
        } catch (Exception e) {
            errorLabel.setText("Gagal membuka halaman login.");
            e.printStackTrace();
        }
    }
}
