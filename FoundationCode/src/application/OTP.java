package application;

import databasePart1.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.UUID;

public class OTP {
    private final DatabaseHelper databaseHelper;
    private final Stage primaryStage;

    public OTP(DatabaseHelper databaseHelper, Stage primaryStage) {
        this.databaseHelper = databaseHelper;
        this.primaryStage = primaryStage;
    }

    public void show() {
        VBox layout = new VBox();
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label titleLabel = new Label("Generate One-Time Password");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ComboBox<String> userDropdown = new ComboBox<>();
        userDropdown.setPromptText("Select a user");

        List<String> userData = databaseHelper.getAllUsers();
        if (!userData.isEmpty()) {
            userDropdown.setItems(FXCollections.observableArrayList(userData));
        } else {
            userDropdown.setPromptText("No users available");
        }

        Button generateOTPButton = new Button("Generate One-Time Password");
        Label otpLabel = new Label();

        generateOTPButton.setOnAction(e -> {
            String selectedUser = userDropdown.getValue();
            if (selectedUser != null && !selectedUser.isEmpty()) {
                String username = selectedUser.split(",")[0].trim();
                String otp = UUID.randomUUID().toString().substring(0, 4); // Generate 4-character OTP
                databaseHelper.setOTP(username, otp);
                otpLabel.setText("Generated OTP: " + otp);
            } else {
                showAlert("Error", "Please select a user.");
            }
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> new AdminHomePage(databaseHelper).show(primaryStage));

        layout.getChildren().addAll(titleLabel, userDropdown, generateOTPButton, otpLabel, backButton);
        primaryStage.setScene(new Scene(layout, 400, 300));
        primaryStage.setTitle("One-Time Password");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
