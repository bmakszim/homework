package jobagency.dialogs;

import javafx.geometry.Insets;
import javafx.scene.Scene;
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

public class ApplicantsDialog {

    private final Connection connection;

    public ApplicantsDialog(Connection connection) {
        this.connection = connection;
    }

    public void showApplicantsDialog(Training training) {
        Stage dialog = new Stage();
        dialog.setTitle("Jelentkezők: " + training.getName());

        VBox dialogLayout = new VBox(10);
        dialogLayout.setPadding(new Insets(10));

        ListView<String> applicantsListView = new ListView<>();
        loadApplicantsForTraining(applicantsListView, training.getName());

        Button closeButton = new Button("Bezárás");
        closeButton.setOnAction(e -> dialog.close());

        dialogLayout.getChildren().addAll(
                new Label("A képzésre jelentkezők listája:"),
                applicantsListView,
                closeButton
        );

        Scene dialogScene = new Scene(dialogLayout, 400, 300);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private void loadApplicantsForTraining(ListView<String> listView, String trainingName) {
        String query = """
        SELECT u.name
        FROM users u
        JOIN training_users tu ON u.id = tu.user_id
        JOIN trainings t ON tu.training_id = t.id
        WHERE t.name = ?
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


}
