package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.sql.SQLException;

import databasePart1.*;

/**
 * The SetupAdmin class handles the setup process for creating an administrator account.
 * This is intended to be used by the first user to initialize the system with admin credentials.
 */
public class AdminSetupPage {
	
    private final DatabaseHelper databaseHelper;

    public AdminSetupPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
    	// Input fields for userName and password and email 
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter Admin userName");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);
        
        TextField emailField = new TextField();
        emailField.setPromptText("Enter Admin Email");
        emailField.setMaxWidth(250);

        
     // Label to display error messages
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        Button setupButton = new Button("Setup");
        
        setupButton.setOnAction(a -> {
        	// Retrieve user input
            String userName = userNameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            
         // Clear previous error messages
            errorLabel.setText("");


          
            if (userName.isEmpty()) {
                errorLabel.setText("Username cannot be empty.");
                return;
            }

          //Call username recognizer to validate the new username  from new user
            String usernameError = UserNameRecognizer.checkForValidUserName(userName);
            if (!usernameError.isEmpty()) {
                errorLabel.setText(usernameError);
                return;
            }

          //Call password Evaluator recognizer to validate the new password  from new user
            String passwordError = PasswordEvaluator.evaluatePassword(password);
            if (!passwordError.isEmpty()) {
                errorLabel.setText(passwordError);
                return;
            }

            //Display message if account creation is successful.
            System.out.println("Success! The username and password are valid.");
			
			

            
            
            try {
            	// Create a new User object with admin role and register in the database
            	User user=new User(userName, password, email,"Admin");
                databaseHelper.register(user);
                System.out.println("Administrator setup completed.");
                
                // Navigate to the Welcome Login Page
                new UserLoginPage(databaseHelper).show(primaryStage);
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            }
        });

        VBox layout = new VBox(10, userNameField, emailField, passwordField, setupButton, errorLabel);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Administrator Setup");
        primaryStage.show();
    }
}
