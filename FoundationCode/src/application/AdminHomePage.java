package application;

import java.util.List;

import databasePart1.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * AdminPage class represents the user interface for the admin user.
 * This page displays a simple welcome message for the admin.
 */

public class AdminHomePage {
	/**
     * Displays the admin page in the provided primary stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     */
	
	private final DatabaseHelper databaseHelper;
	private final String role;
    
	public AdminHomePage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
		this.role = "";
    }
	
    public AdminHomePage(String role, DatabaseHelper databaseHelper) {
        this.role = role;
        this.databaseHelper = databaseHelper;
    }
    
    public void show(Stage primaryStage) {
        VBox layout = new VBox();
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        // Label to display the welcome message for the admin
        Label adminLabel = new Label("Hello, Admin!");
        adminLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Logout button
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-font-size: 14px;");
        logoutButton.setOnAction(e -> {
            new UserLoginPage(databaseHelper).show(primaryStage);
        });

        // List all users button
        Button listUsersButton = new Button("List All Users");
        listUsersButton.setStyle("-fx-font-size: 14px;");
        listUsersButton.setOnAction(e -> listUsers());

        
        ComboBox<String> userDropdown = new ComboBox<>();
        // Modify roles button
        Button modifyRolesButton = new Button("Modify Roles");
        modifyRolesButton.setStyle("-fx-font-size: 14px;");
        modifyRolesButton.setOnAction(a -> {
        	new ModifyRolesPage(databaseHelper, primaryStage).show();
        });

        // Invite button for generating invitation codes
        Button inviteButton = new Button("Invite");
        inviteButton.setStyle("-fx-font-size: 14px;");
        inviteButton.setOnAction(a -> {
            new InvitationPage(databaseHelper, primaryStage).show();
        });

        //DELETE USER BUTTON
        Button deleteButton = new Button("Delete Accounts");
        deleteButton.setStyle("-fx-font-size: 14px;");
        deleteButton.setOnAction(a -> {
            new DeleteAccountPage(databaseHelper, primaryStage).show();
        });
        
        // ONE TIME PASSWORD GENERATION
        Button otpButton = new Button("One-time Password");
        otpButton.setStyle("-fx-font-size: 14px;");
        otpButton.setOnAction(a -> {
            new OTP(databaseHelper, primaryStage).show();
        });

        // Add all elements to layout
        layout.getChildren().addAll(adminLabel, logoutButton, listUsersButton, modifyRolesButton, deleteButton, otpButton);
        
        // Add some spacing before the invite button
        layout.getChildren().add(new Label("")); // Adds an empty space
        layout.getChildren().add(inviteButton);

        Scene adminScene = new Scene(layout, 800, 400);
        primaryStage.setScene(adminScene);
        primaryStage.setTitle("Admin Page");
    }

    
    private void listUsers() {
        List<String> users = databaseHelper.getAllUsers();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User List");
        alert.setHeaderText("All Users");
        alert.setContentText(String.join("\n", users));
        alert.showAndWait();	
    }
    
    private void modifyRoles(String user) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Modify Roles");
        dialog.setHeaderText("Enter new role for " + user);
        dialog.setContentText("New Role:");

        dialog.showAndWait().ifPresent(role -> {
            databaseHelper.updateUserRole(user, role);
            showAlert("Role Updated", "Role updated successfully for " + user);
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

//test
   
}