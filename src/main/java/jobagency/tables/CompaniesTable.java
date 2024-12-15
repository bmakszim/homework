package jobagency.tables;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import jobagency.entities.Company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CompaniesTable {
    private final Connection connection;

    public CompaniesTable(Connection connection) {
        this.connection = connection;
    }

    public void loadCompanies(TableView<Company> table) {
        table.getColumns().clear();

        TableColumn<Company, String> nameColumn = new TableColumn<>("Név");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        table.getColumns().add(nameColumn);

        String query = "SELECT name FROM companies";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                table.getItems().add(new Company(rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
