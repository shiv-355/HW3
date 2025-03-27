package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * InvitationPage class represents the page where an admin can generate an invitation code.
 * The invitation code is displayed upon clicking a button.
 */
public class InvitationPage {

    private final DatabaseHelper databaseHelper;
    private final Stage primaryStage;

    public InvitationPage(DatabaseHelper databaseHelper, Stage primaryStage) {
        this.databaseHelper = databaseHelper;
        this.primaryStage = primaryStage;
    }

    public void show() {
        VBox layout = new VBox();
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label label = new Label("Generate an Invite Code for:");
        
        // Dropdown menu to select role
        ComboBox<String> roleSelection = new ComboBox<>();
        roleSelection.getItems().addAll("Admin", "Student", "Teacher", "Reviewer", "Instructor", "Staff");
        roleSelection.setPromptText("Select Role");

        Button generateCodeButton = new Button("Generate Code");
        Label inviteCodeLabel = new Label("");

        generateCodeButton.setOnAction(e -> {
            String selectedRole = roleSelection.getValue();
            if (selectedRole == null) {
                inviteCodeLabel.setText("Please select a role.");
                return;
            }
            String inviteCode = databaseHelper.generateInvitationCode(selectedRole);
            inviteCodeLabel.setText("Generated Code: " + inviteCode);
        });

        layout.getChildren().addAll(label, roleSelection, generateCodeButton, inviteCodeLabel);
        Scene scene = new Scene(layout, 400, 300);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Invitation Page");
    }
}
