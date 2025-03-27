package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ResetPasswordPage {
    private final DatabaseHelper databaseHelper;
    private final Stage primaryStage;
    private final String username;

    public ResetPasswordPage(DatabaseHelper databaseHelper, Stage primaryStage, String username) {
        this.databaseHelper = databaseHelper;
        this.primaryStage = primaryStage;
        this.username = username;
    }

    public void show() {
        VBox layout = new VBox();
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label titleLabel = new Label("Reset Your Password");
        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Enter new password");

        Button resetPasswordButton = new Button("Reset Password");
        resetPasswordButton.setOnAction(e -> {
            String newPassword = newPasswordField.getText().trim();
            if (!newPassword.isEmpty()) {
                databaseHelper.updatePassword(username, newPassword);
                databaseHelper.clearOTP(username);
                showAlert("Success", "Password reset successful. Please log in again.");
                new UserLoginPage(databaseHelper).show(primaryStage);
            } else {
                showAlert("Error", "Password cannot be empty.");
            }
        });

        layout.getChildren().addAll(titleLabel, newPasswordField, resetPasswordButton);
        primaryStage.setScene(new Scene(layout, 400, 300));
        primaryStage.setTitle("Reset Password");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
