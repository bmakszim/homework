package jobagency.tables;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import jobagency.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersTable {

    private final Connection connection;

    public UsersTable(Connection connection) {
        this.connection = connection;
    }

    public void loadUsers(TableView<User> table) {
        table.getColumns().clear();

        TableColumn<User, String> nameColumn = new TableColumn<>("Név");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        table.getColumns().add(nameColumn);

        String query = "SELECT name FROM users";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                table.getItems().add(new User(rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
