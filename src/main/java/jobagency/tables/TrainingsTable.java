package jobagency.tables;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import jobagency.entities.Training;
import jobagency.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TrainingsTable {

    private final Connection connection;

    public TrainingsTable(Connection connection) {
        this.connection = connection;
    }

    public void loadTrainings(TableView<Training> table) {
        table.getColumns().clear();

        TableColumn<Training, String> nameColumn = new TableColumn<>("Név");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        table.getColumns().add(nameColumn);

        String query = "SELECT name FROM trainings";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                table.getItems().add(new Training(rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
