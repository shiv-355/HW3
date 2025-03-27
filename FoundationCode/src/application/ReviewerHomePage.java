package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ReviewerHomePage {
    private final DatabaseHelper databaseHelper;
    private final String role;

    public ReviewerHomePage(DatabaseHelper databaseHelper) {
        this.role = "";
        this.databaseHelper = databaseHelper;
    }
    
    public ReviewerHomePage(String role, DatabaseHelper databaseHelper) {
        this.role = role;
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox();
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label reviewerLabel = new Label("Hello, Reviewer" + "!");
        reviewerLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        layout.getChildren().add(reviewerLabel);
        Scene reviewerScene = new Scene(layout, 800, 400);

        primaryStage.setScene(reviewerScene);
        primaryStage.setTitle(role + " Home Page");
        
        
    }
}
