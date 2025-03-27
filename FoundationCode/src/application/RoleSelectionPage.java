package application;

// Import necessary JavaFX UI classes.
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// Import classes from our database helper package.
import databasePart1.*;

/**
 * RoleSelectionPage displays a page where users can choose a role (like Admin, Student, etc.)
 * to continue using the application. It uses a DatabaseHelper to interact with the database,
 * and shows buttons for each available role.
 */
public class RoleSelectionPage {

    // Reference to the database helper for database operations.
    private final DatabaseHelper databaseHelper;
    // Username of the current logged-in user.
    private final String userName;
    // Array of role names, extracted from a string like "Admin_Student".
    private final String[] roles;
    // The User object representing the current user.
    private User user;

    /**
     * Constructor for RoleSelectionPage.
     * @param databaseHelper a helper for database operations.
     * @param userName       the username of the current user.
     * @param roles          a string containing roles separated by underscores (e.g., "Admin_Student").
     */
    public RoleSelectionPage(DatabaseHelper databaseHelper, String userName, String roles) {
        // Save the database helper and username.
        this.databaseHelper = databaseHelper;
        this.userName = userName;
        // Split the roles string by "_" into an array (e.g., "Admin_Student" -> ["Admin", "Student"]).
        this.roles = roles.split("_");
    }

    /**
     * Setter for the current User object.
     * @param currentUser the user object to set.
     */
    public void setUser(User currentUser) {
        this.user = currentUser;
    }

    /**
     * Displays the role selection page on the given primary stage.
     * @param primaryStage the main stage for this application.
     */
    public void show(Stage primaryStage) {
        // Create a vertical box layout with spacing of 10 pixels.
        VBox layout = new VBox(10);
        // Set some padding and center alignment using inline CSS.
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Create a label that instructs the user to select a role.
        Label titleLabel = new Label("Select a role to continue:");
        layout.getChildren().add(titleLabel);  // Add the label to the layout.

        // Loop over each role from the roles array.
        for (String role : roles) {
            // Create a button for each role.
            Button roleButton = new Button(role);
            // Set the button font size for better visibility.
            roleButton.setStyle("-fx-font-size: 14px;");
            // Set an event handler: when clicked, navigate to the page corresponding to this role.
            roleButton.setOnAction(a -> navigateToRolePage(primaryStage, role));
            // Add the button to the layout.
            layout.getChildren().add(roleButton);
        }

        // Create a "Logout" button in case the user wants to exit.
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-font-size: 14px;");
        // Set an event handler: clicking logout sends the user back to the login page.
        logoutButton.setOnAction(e -> new UserLoginPage(databaseHelper).show(primaryStage));
        layout.getChildren().add(logoutButton);

        // Create a new scene with the layout, size 400x300.
        primaryStage.setScene(new Scene(layout, 400, 300));
        // Set the title of the stage.
        primaryStage.setTitle("Role Selection");
        // Display the stage.
        primaryStage.show();
    }

    /**
     * Navigates the user to the appropriate home page based on the selected role.
     * @param primaryStage the main application stage.
     * @param role         the role selected by the user.
     */
    private void navigateToRolePage(Stage primaryStage, String role) {
        // Use a switch statement to decide which page to navigate to.
        switch (role.toLowerCase()) {
            case "admin":
                // Navigate to the Admin home page.
                new AdminHomePage(userName, databaseHelper).show(primaryStage);
                break;
            case "student":
                // For a student, create the home page, set the current user, and show it.
                StudentHomePage homePage = new StudentHomePage(userName, databaseHelper);
                homePage.setUser(user);
                homePage.show(primaryStage);
                break;
            case "teacher":
                // Navigate to the Teacher home page.
                new TeacherHomePage(userName, databaseHelper).show(primaryStage);
                break;
            case "reviewer":
                // Navigate to the Reviewer home page.
                new ReviewerHomePage(userName, databaseHelper).show(primaryStage);
                break;
            default:
                // Print out a message if the role is not recognized.
                System.out.println("Invalid role: " + role);
                break;
        }
    }
}
