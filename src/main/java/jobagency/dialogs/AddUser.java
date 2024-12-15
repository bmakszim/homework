package jobagency.dialogs;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jobagency.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddUser {
    private final Connection connection;

    public AddUser(Connection connection) {
        this.connection = connection;
    }

    public void showAddUserDialog(TableView<User> usersTable) {
        Stage dialog = new Stage();
        dialog.setTitle("Új felhasználó hozzáadása");

        VBox dialogLayout = new VBox(10);
        dialogLayout.setPadding(new Insets(10));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Felhasználónév");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Jelszó");

        TextField prefixField = new TextField();
        prefixField.setPromptText("Előtag (pl. Dr.)");

        TextField nameField = new TextField();
        nameField.setPromptText("Teljes név");

        TextField emailField = new TextField();
        emailField.setPromptText("E-mail");

        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("iroda", "jelentkezo");
        roleBox.setPromptText("Szerepkör");

        Button saveButton = new Button("Mentés");
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        saveButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String prefix = prefixField.getText();
            String name = nameField.getText();
            String email = emailField.getText();
            String role = roleBox.getValue();

            if (username.isEmpty() || password.isEmpty() || name.isEmpty() || email.isEmpty() || role == null) {
                errorLabel.setText("Minden kötelező mezőt ki kell tölteni!");
                return;
            }

            if (addUserToDatabase(username, password, prefix, name, email, role)) {
                usersTable.getItems().add(new User(name)); // Frissítjük a táblázatot
                dialog.close();
            } else {
                errorLabel.setText("Hiba történt a felhasználó hozzáadása közben.");
            }
        });

        dialogLayout.getChildren().addAll(
                new Label("Új felhasználó adatai"),
                usernameField, passwordField, prefixField, nameField, emailField, roleBox, saveButton, errorLabel
        );

        Scene dialogScene = new Scene(dialogLayout, 400, 400);
        dialog.setScene(dialogScene);
        dialog.show();
    }


    private boolean addUserToDatabase(String username, String password, String prefix, String name, String email, String role) {
        String query = "INSERT INTO users (username, password, prefix, name, email, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, prefix);
            stmt.setString(4, name);
            stmt.setString(5, email);
            stmt.setString(6, role);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
