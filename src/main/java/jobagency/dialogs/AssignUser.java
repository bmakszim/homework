package jobagency.dialogs;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jobagency.entities.Training;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AssignUser {

    private final Connection connection;

    public AssignUser(Connection connection) {
        this.connection = connection;
    }

    public void showAssignUserDialog(Training training) {
        Stage dialog = new Stage();
        dialog.setTitle("Felhasználók hozzárendelése a képzéshez: " + training.getName());

        VBox dialogLayout = new VBox(10);
        dialogLayout.setPadding(new Insets(10));

        ListView<String> userListView = new ListView<>();
        loadAvailableUsers(userListView, training.getName());

        Button assignButton = new Button("Hozzárendelés");
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        assignButton.setOnAction(e -> {
            String selectedUser = userListView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                if (assignUserToTraining(selectedUser, training.getName())) {
                    showAlert("Siker", "Felhasználó sikeresen hozzárendelve!", Alert.AlertType.INFORMATION);
                    dialog.close();
                } else {
                    errorLabel.setText("Hiba történt a hozzárendelés során.");
                }
            } else {
                errorLabel.setText("Válasszon ki egy felhasználót!");
            }
        });

        dialogLayout.getChildren().addAll(
                new Label("Felhasználók listája"),
                userListView,
                assignButton,
                errorLabel
        );

        Scene dialogScene = new Scene(dialogLayout, 400, 300);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private void loadAvailableUsers(ListView<String> listView, String trainingName) {
        String query = """
        SELECT u.name
        FROM users u
        WHERE u.id NOT IN (
            SELECT tu.user_id
            FROM training_users tu
            JOIN trainings t ON tu.training_id = t.id
            WHERE t.name = ?
        )
    """;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, trainingName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                listView.getItems().add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean assignUserToTraining(String userName, String trainingName) {
        String query = """
        INSERT INTO training_users (training_id, user_id)
        SELECT t.id, u.id
        FROM trainings t, users u
        WHERE t.name = ? AND u.name = ?
    """;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, trainingName);
            stmt.setString(2, userName);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
