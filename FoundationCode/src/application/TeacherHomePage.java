package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TeacherHomePage {
    private final DatabaseHelper databaseHelper;
    private final String role;

    public TeacherHomePage(DatabaseHelper databaseHelper) {
        this.role = "";
        this.databaseHelper = databaseHelper;
    }
    
    public TeacherHomePage(String role, DatabaseHelper databaseHelper) {
        this.role = role;
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox();
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label teacherLabel = new Label("Hello, Teacher" + "!");
        teacherLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        layout.getChildren().add(teacherLabel);
        Scene teacherScene = new Scene(layout, 800, 400);

        primaryStage.setScene(teacherScene);
        primaryStage.setTitle(role + " Home Page");
        
     // Logout button
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-font-size: 14px;");
        logoutButton.setOnAction(e -> {
            new UserLoginPage(databaseHelper).show(primaryStage);
        });
        
        
     // Back Button (Returns to HomePage)    
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 14px;");
        backButton.setOnAction(e -> new TeacherHomePage(databaseHelper).show(primaryStage));

        layout.getChildren().addAll(backButton);
        Scene scene = new Scene(layout, 400, 250);
    }
}
