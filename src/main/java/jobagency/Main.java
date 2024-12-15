package jobagency;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.*;

public class Main extends Application {

    private Connection connection;

    @Override
    public void start(Stage primaryStage) {
        // Initialize database connection
        connectToDatabase();

        primaryStage.setTitle("Munkaközvetítő Iroda");

        // Show login screen first
        Scene loginScene = new LoginScreen(connection).createLoginScene(primaryStage);
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    private void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/job_agency"; // Update with your DB URL
            String user = "root"; // Update with your DB username
            String password = "password"; // Update with your DB password
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Adatbázis kapcsolat sikeresen létrejött.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
