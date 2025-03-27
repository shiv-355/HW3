package application;

// Import necessary JavaFX and database helper classes.
import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The StudentHomePage class represents the home page for a student user.
 * It displays various buttons (Logout, Back, Q&A System) and greets the student.
 */
public class StudentHomePage {

    // Reference to our database helper for any DB-related operations.
    private final DatabaseHelper databaseHelper;
    // A role string that might be used to tailor the page (e.g., "Student").
    private final String role;
    // The full User object for the currently logged-in student.
    private User user;



    /**
     * Setter to update the User object.
     * @param currentUser the current User object.
     */
    public void setUser(User currentUser) {
        this.user = currentUser;
        // Debug print to confirm the user is set.
        System.out.println("User set in HW2JavaFXExtended: " + user.getUserName());
    }

    /**
     * Constructor that only takes a DatabaseHelper.
     * Role is set to an empty string by default.
     * @param databaseHelper the database helper instance.
     */
    public StudentHomePage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
        this.role = "";
    }

    /**
     * Constructor that takes a role and a DatabaseHelper.
     * @param role the role (for instance, "Student").
     * @param databaseHelper the database helper instance.
     */
    public StudentHomePage(String role, DatabaseHelper databaseHelper) {
        this.role = role;
        this.databaseHelper = databaseHelper;
    }

    /**
     * Displays the Student Home Page on the given stage.
     * @param primaryStage the primary Stage of the application.
     */
    public void show(Stage primaryStage) {
        // Create a vertical box layout with some padding and center alignment.
        VBox layout = new VBox();
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        // Create a greeting label for the student.
        Label studentLabel = new Label("Hello, Student!");
        studentLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Create a Logout button to return to the login page.
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-font-size: 14px;");
        logoutButton.setOnAction(e -> new UserLoginPage(databaseHelper).show(primaryStage));

        // Create a Back button to return to the welcome/login page.
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 14px;");
        // For the back button, we create a new WelcomeLoginPage passing a new empty User along with role.
        backButton.setOnAction(e -> new WelcomeLoginPage(databaseHelper).show(primaryStage, new User("", "", "", role)));

        // Create a button to launch the Q&A System.
        Button qA = new Button("Q&A System");
        qA.setStyle("-fx-font-size: 14px;");
        qA.setOnAction(e -> {
            // Instantiate the Q&A application.
            HW2JavaFXExtended qaApp = new HW2JavaFXExtended();

            // Set current user and user object in the Q&A app.


            qaApp.setCurrentUser(user.getUserName());
            qaApp.setUser(user);
            // Start the Q&A system on the primary stage.
            qaApp.start(primaryStage);
        });

        // Add the label and buttons to the layout.
        layout.getChildren().addAll(studentLabel, backButton, logoutButton, qA);

        // Create a new scene with the layout; set size to 800x400.
        Scene studentScene = new Scene(layout, 800, 400);
        // Set the scene on the primary stage.
        primaryStage.setScene(studentScene);
        // Set the window title using the role.
        primaryStage.setTitle(role + " Home Page");

        primaryStage.show();
    }
}
