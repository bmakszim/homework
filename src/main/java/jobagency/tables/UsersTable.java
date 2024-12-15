package jobagency.tables;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import jobagency.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UsersTable {

    private final Connection connection;

    public UsersTable(Connection connection) {
        this.connection = connection;
    }

    public void loadUsers(TableView<User> table) {
        table.getColumns().clear();

        TableColumn<User, String> nameColumn = new TableColumn<>("Név");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<User, String> prefixColumn = new TableColumn<>("Előtag");
        prefixColumn.setCellValueFactory(new PropertyValueFactory<>("prefix"));

        TableColumn<User, String> usernameColumn = new TableColumn<>("Felhasználónév");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        table.getColumns().add(nameColumn);
        table.getColumns().add(prefixColumn);
        table.getColumns().add(usernameColumn);
        table.getColumns().add(emailColumn);

        String query = "SELECT name, prefix, username, email FROM users";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                table.getItems().add(
                        new User(rs.getString("name"),
                                rs.getString("prefix"),
                                rs.getString("username"),
                                rs.getString("email")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
