package application;

import databasePart1.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;

/**
 * DeleteUserPage class allows admins to select and delete a user account.
 */
public class DeleteUserPage {

    private final DatabaseHelper databaseHelper;
    private final Stage primaryStage;

    public DeleteUserPage(DatabaseHelper databaseHelper, Stage primaryStage) {
        this.databaseHelper = databaseHelper;
        this.primaryStage = primaryStage;
    }

    public void show() {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label titleLabel = new Label("Delete User Account");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Dropdown menu to select a user
        ComboBox<String> userDropdown = new ComboBox<>();
        List<String> users = databaseHelper.getAllUsernames();
        ObservableList<String> userList = FXCollections.observableArrayList(users);
        userDropdown.setItems(userList);
        userDropdown.setPromptText("Select a user");

        Button deleteButton = new Button("Delete User");
        deleteButton.setStyle("-fx-font-size: 14px; -fx-background-color: red; -fx-text-fill: white;");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        deleteButton.setOnAction(e -> {
            String selectedUser = userDropdown.getValue();

            if (selectedUser == null || selectedUser.isEmpty()) {
                messageLabel.setText("Please select a user to delete.");
                return;
            }

            // Show confirmation alert
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirm Deletion");
            confirmation.setHeaderText("Are you sure you want to delete this user?");
            confirmation.setContentText("Type 'Yes' to confirm.");

            TextField inputField = new TextField();
            confirmation.getDialogPane().setContent(inputField);
            confirmation.showAndWait();

            if ("Yes".equalsIgnoreCase(inputField.getText().trim())) {
                boolean success = true;
                        databaseHelper.deleteUser(selectedUser);
                if (success) {
                    messageLabel.setText("User deleted successfully.");
                    userDropdown.getItems().remove(selectedUser); // Remove from dropdown
                } else {
                    messageLabel.setText("Failed to delete user.");
                }
            } else {
                messageLabel.setText("User deletion canceled.");
            }
        });

        Button backButton = new Button("Back to Admin Page");
        backButton.setOnAction(e -> new AdminHomePage("admin", databaseHelper).show(primaryStage));

        layout.getChildren().addAll(titleLabel, userDropdown, deleteButton, messageLabel, backButton);
        Scene scene = new Scene(layout, 400, 250);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Delete User");
    }
}
