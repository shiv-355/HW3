package application;

// Import JavaFX UI components and layouts.
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// Import necessary database functionality and other utilities.
import java.sql.SQLException;
import databasePart1.*;
import java.util.List;

/**
 * The UserLoginPage class provides a simple login interface.
 * Users enter their credentials, and the system checks if they're valid.
 * Depending on the user's role(s), it navigates them to the appropriate page.
 */
public class UserLoginPage {

    // A reference to our DatabaseHelper for handling DB operations.
    private final DatabaseHelper databaseHelper;

    /**
     * Constructor that accepts a DatabaseHelper instance.
     * @param databaseHelper the helper for database interactions.
     */
    public UserLoginPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Displays the login page on the given stage.
     * @param primaryStage the main window where the UI is shown.
     */
    public void show(Stage primaryStage) {
        // Create a text field for username input with a placeholder.
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter username");
        userNameField.setMaxWidth(250);

        // Create a password field for password input with a placeholder.
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setMaxWidth(250);

        // A label to display error messages (like invalid login attempts).
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        // Create a login button for submitting credentials.
        Button loginButton = new Button("Login");

        // Set an action event for the login button.
        loginButton.setOnAction(a -> {
            // Retrieve the text entered by the user.
            String userName = userNameField.getText();
            String password = passwordField.getText();

            try {
                // Use the database helper to check if the credentials are valid.
                boolean isValidUser = databaseHelper.login(userName, password);
                if (!isValidUser) {
                    // Show an error message if credentials are incorrect.
                    errorLabel.setText("Invalid username or password.");
                    return;
                }

                // Retrieve the user's role(s) from the database.
                String role = databaseHelper.getUserRoles(userName);
                if (role == null) {
                    errorLabel.setText("Error retrieving user role.");
                    return;
                }

                // Create a new User object using the retrieved role.
                User user = new User(userName, password, "", role);


                // If the user has multiple roles (denoted by "_" separator), let them choose.
                if (role.contains("_")) {
                    // Navigate to the Role Selection Page.
                    RoleSelectionPage roleSelectionPage = new RoleSelectionPage(databaseHelper, userName, role);
                    roleSelectionPage.setUser(user);
                    roleSelectionPage.show(primaryStage);
                } else {
                    // For a single role, navigate directly based on the role.
                    switch (role.toLowerCase()) {
                        case "admin":
                            new AdminHomePage(userName, databaseHelper).show(primaryStage);
                            break;
                        case "student":
                            // Create and configure the Student home page.
                            StudentHomePage studentHomePage = new StudentHomePage(userName, databaseHelper);
                            studentHomePage.setUser(user);
                            studentHomePage.show(primaryStage);
                            break;
                        case "teacher":
                            new TeacherHomePage(userName, databaseHelper).show(primaryStage);
                            break;
                        case "reviewer":
                            new ReviewerHomePage(userName, databaseHelper).show(primaryStage);
                            break;
                        default:
                            // Show an error if an unrecognized role is found.
                            errorLabel.setText("Invalid role detected.");
                            return;
                    }
                }
            } catch (SQLException e) {
                // Handle any SQL exceptions that might occur during login.
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            }
        });

        // Create a "Forgot Password?" button to handle password recovery.
        Button forgotPasswordButton = new Button("Forgot Password?");
        forgotPasswordButton.setStyle("-fx-font-size: 12px; -fx-text-fill: blue;");
        forgotPasswordButton.setOnAction(e -> new ForgotPasswordPage(databaseHelper, primaryStage).show());

        // Set up a vertical layout container with spacing and padding.
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        // Add all UI elements to the layout.
        layout.getChildren().addAll(userNameField, passwordField, loginButton, forgotPasswordButton, errorLabel);

        // Create a scene with the layout, specifying the window size.
        primaryStage.setScene(new Scene(layout, 800, 400));
        // Set the window title.
        primaryStage.setTitle("User Login");
        // Show the stage.
        primaryStage.show();
    }
}
