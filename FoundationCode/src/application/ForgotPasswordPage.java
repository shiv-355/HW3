package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ForgotPasswordPage {
    private final DatabaseHelper databaseHelper;
    private final Stage primaryStage;

    public ForgotPasswordPage(DatabaseHelper databaseHelper, Stage primaryStage) {
        this.databaseHelper = databaseHelper;
        this.primaryStage = primaryStage;
    }
    

    public void show() {
        VBox layout = new VBox();
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label titleLabel = new Label("Forgot Password?");
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter your username");

        TextField otpField = new TextField();
        otpField.setPromptText("Enter One-Time Password");

        Button verifyOTPButton = new Button("Verify OTP");
        verifyOTPButton.setOnAction(e -> {
            String username = userNameField.getText().trim();
            String otp = otpField.getText().trim();

            if (databaseHelper.verifyOTP(username, otp)) {
                new ResetPasswordPage(databaseHelper, primaryStage, username).show();
            } else {
                showAlert("Error", "Invalid or expired OTP.");
            }
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> new UserLoginPage(databaseHelper).show(primaryStage));

        layout.getChildren().addAll(titleLabel, userNameField, otpField, verifyOTPButton, backButton);
        primaryStage.setScene(new Scene(layout, 400, 300));
        primaryStage.setTitle("Forgot Password");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
