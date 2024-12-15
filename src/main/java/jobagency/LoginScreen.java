package jobagency;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginScreen {

    private final Connection connection;

    public LoginScreen(Connection connection) {
        this.connection = connection;
    }

    public Scene createLoginScene(Stage primaryStage) {
        VBox loginLayout = new VBox(10);
        loginLayout.setPadding(new Insets(10));

        Label loginLabel = new Label("Bejelentkezés");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Felhasználónév");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Jelszó");
        Button loginButton = new Button("Bejelentkezés");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            // Authenticate user using the database
            String role = authenticateUser(username, password);
            if ("iroda".equals(role)) {
                primaryStage.setScene(new MainScene(connection).createMainScene());
            } else if ("jelentkezo".equals(role)) {
                primaryStage.setScene(new JobSeekerScene(connection).createJobSeekerScene());
            } else {
                errorLabel.setText("Hibás felhasználónév vagy jelszó!");
            }
        });

        loginLayout.getChildren().addAll(loginLabel, usernameField, passwordField, loginButton, errorLabel);
        return new Scene(loginLayout, 400, 300);
    }

    private String authenticateUser(String username, String password) {
        String query = "SELECT role FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
