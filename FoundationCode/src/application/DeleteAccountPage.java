package application;

import databasePart1.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;


import java.util.List;

/**
 * DeleteAccountPage allows an admin to select a user from a dropdown menu and delete them.
 * The admin is required to confirm the deletion before proceeding.
 */
public class DeleteAccountPage {
    private final DatabaseHelper databaseHelper;
    private final Stage primaryStage;

    public DeleteAccountPage(DatabaseHelper databaseHelper, Stage primaryStage) {
        this.databaseHelper = databaseHelper;
        this.primaryStage = primaryStage;
    }

    /**
     * Displays the Delete Account page.
     */
    public void show() {
        VBox layout = new VBox();
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label titleLabel = new Label("Delete a User Account");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Dropdown  for selecting a user
        ComboBox<String> userDropdown = new ComboBox<>();
        userDropdown.setPromptText("Select a user");

        List<String> userData = databaseHelper.getAllUsers();
        if (!userData.isEmpty()) {
            userDropdown.setItems(FXCollections.observableArrayList(userData));
        } else {
            userDropdown.setPromptText("No users available");
        }

        // Delete User Button
        Button deleteUserButton = new Button("Delete Selected User");
        deleteUserButton.setStyle("-fx-font-size: 14px;");
        ComboBox<String> userList = new ComboBox<>();
        ObservableList<String> users = FXCollections.observableArrayList();
        userList.setItems(users);
        users.setAll(databaseHelper.getAllUsers()); // Populate dropdown initially

        deleteUserButton.setOnAction(e -> {
        	String[] userParts = userDropdown.getValue().split(",");
        	String selectedUser = userParts[0].trim(); // Always gets the username
        	String selectedRole = userParts.length > 2 ? userParts[2].trim() : ""; // If no role exists, assign an empty string

        	if (selectedRole.contains("Admin")) {
        	    showAlert("Error", "The Admin cannot be deleted.");
        	} else if (!selectedUser.isEmpty()) {
        	    confirmDeletion(selectedUser);
        	} else {
        	    showAlert("Error", "Please select a valid user to delete.");
        	}

        	if (selectedRole.contains("Admin")) showAlert("Error", "the Admin can not be deleted.");
        	else if (selectedUser != null && !selectedUser.isEmpty()) {
            	String username = selectedUser.split(",")[0].trim();
                confirmDeletion(selectedUser);
            } else {
                showAlert("Error", "Please select a user to delete.");
            }
        });

        // Back Button (Returns to AdminHomePage)
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 14px;");
        backButton.setOnAction(e -> new AdminHomePage(databaseHelper).show(primaryStage));

        layout.getChildren().addAll(titleLabel, userDropdown, deleteUserButton, backButton);
        Scene scene = new Scene(layout, 400, 250);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Delete Account");
    }

    /**
     * Shows a confirmation alert before deleting the user.
     * @param user The selected user to be deleted.
     */
    private void confirmDeletion(String username) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, 
            "Are you sure you want to delete " + username + "?", 
            ButtonType.YES, ButtonType.NO);
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
            	databaseHelper.deleteUser(username);
            	//users.setAll(databaseHelper.getAllUsers()); // Refresh user list
                showAlert("Success", "User deleted successfully.");
            }
        });
    }

    /**
     * Deletes the selected user from the database.
     * @param user The selected user to delete.
     */
    private void deleteUser(String username) {
    	databaseHelper.deleteUser(username);  // Call the deletion method in DatabaseHelper

        // Verify if user is actually deleted before redirecting
        if (!databaseHelper.doesUserExist(username)) {
            showAlert("Success", "User " + username + " has been deleted.");
            new AdminHomePage(databaseHelper).show(primaryStage); // Redirect after successful deletion
        } else {
            showAlert("Error", "Failed to delete the user. Please try again.");
        }
    }

    /**
     * Displays an alert with a given title and message.
     * @param title The title of the alert.
     * @param content The message inside the alert.
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
