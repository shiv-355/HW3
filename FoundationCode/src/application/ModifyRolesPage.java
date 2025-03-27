package application;

import databasePart1.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

public class ModifyRolesPage {

    private final DatabaseHelper databaseHelper;
    private final Stage primaryStage;
    private final List<String> allRoles = Arrays.asList("Admin", "Student", "Teacher", "Reviewer"); // Predefined roles

    public ModifyRolesPage(DatabaseHelper databaseHelper, Stage primaryStage) {
        this.databaseHelper = databaseHelper;
        this.primaryStage = primaryStage;
    }

    public void show() {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label titleLabel = new Label("Select a user to modify:");

        // Dropdown menu to select a user
        ComboBox<String> userDropdown = new ComboBox<>();
        userDropdown.setPromptText("Select a user");

        // Populate the dropdown with users
        List<String> userData = databaseHelper.getAllUsers();
        if (!userData.isEmpty()) {
            userDropdown.setItems(FXCollections.observableArrayList(userData));
        } else {
            userDropdown.setPromptText("No users available");
        }

        // Second dropdown for roles (initially hidden)
        ComboBox<String> roleDropdown = new ComboBox<>();
        roleDropdown.setVisible(false);

        // Add Role Button
        Button addRoleButton = new Button("Add Role");
        addRoleButton.setStyle("-fx-font-size: 14px;");
        addRoleButton.setOnAction(e -> {
            String selectedUser = userDropdown.getValue();
            if (selectedUser == null || selectedUser.isEmpty()) {
                showAlert("Error", "Please select a user first.");
                return;
            }

            String username = selectedUser.split(",")[0].trim(); // Extract username
            String currentRoles = databaseHelper.getUserRoles(username);

            // Filter available roles (exclude already assigned roles)
            ObservableList<String> availableRoles = FXCollections.observableArrayList();
            for (String role : allRoles) {
                if (!currentRoles.contains(role)) {
                    availableRoles.add(role);
                }
            }

            if (availableRoles.isEmpty()) {
                showAlert("Error", "This user already has all possible roles.");
                return;
            }

            roleDropdown.setItems(availableRoles);
            roleDropdown.setPromptText("Select a role to add");
            roleDropdown.setVisible(true);

            // Confirm Add Button
            Button confirmAddButton = new Button("Confirm");
            confirmAddButton.setOnAction(ev -> {
                String selectedRole = roleDropdown.getValue();
                if (selectedRole != null && !selectedRole.isEmpty()) {
                    databaseHelper.addUserRole(username, selectedRole);
                    showAlert("Success", selectedRole + " added to " + username);
                    roleDropdown.setVisible(false);
                } else {
                    showAlert("Error", "Please select a role to add.");
                }
            });

            layout.getChildren().add(confirmAddButton);
        });

        // Remove Role Button
        Button removeRoleButton = new Button("Remove Role");
        removeRoleButton.setStyle("-fx-font-size: 14px;");
        removeRoleButton.setOnAction(e -> {
            String selectedUser = userDropdown.getValue();
            if (selectedUser == null || selectedUser.isEmpty()) {
                showAlert("Error", "Please select a user first.");
                return;
            }

            String username = selectedUser.split(",")[0].trim(); // Extract username
            String currentRoles = databaseHelper.getUserRoles(username);

            int roleCount = currentRoles.split("_").length;
            if (roleCount == 1) {
                showAlert("Error", "This user only has one role and cannot be removed.");
                return;
            }

            // Populate available roles for removal
            roleDropdown.setItems(FXCollections.observableArrayList(currentRoles.split("_")));
            roleDropdown.setPromptText("Select a role to remove");
            roleDropdown.setVisible(true);

            // Confirm Remove Button
            Button confirmRemoveButton = new Button("Confirm");
            confirmRemoveButton.setOnAction(ev -> {
                String selectedRole = roleDropdown.getValue();
                if (selectedRole != null && !selectedRole.isEmpty()) {
                    databaseHelper.removeUserRole(username, selectedRole);
                    showAlert("Success", selectedRole + " removed from " + username);
                    roleDropdown.setVisible(false);
                } else {
                    showAlert("Error", "Please select a role to remove.");
                }
            });




        });



        // Back Button
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 14px;");
        backButton.setOnAction(e -> new AdminHomePage(databaseHelper).show(primaryStage));

        layout.getChildren().addAll(titleLabel, userDropdown, addRoleButton, removeRoleButton, roleDropdown,backButton);

        Scene scene = new Scene(layout, 400, 350);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Modify Roles Page");
    }

    // Helper function to show alert dialogs
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
