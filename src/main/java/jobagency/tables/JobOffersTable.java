package jobagency.tables;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import jobagency.entities.JobOffer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JobOffersTable {
    private final Connection connection;

    public JobOffersTable(Connection connection) {
        this.connection = connection;
    }

    public void loadJobOffers(TableView<JobOffer> table) {
        table.getColumns().clear();

        TableColumn<JobOffer, String> positionColumn = new TableColumn<>("Pozíció");
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));

        table.getColumns().add(positionColumn);

        String query = "SELECT position FROM job_offers";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                table.getItems().add(new JobOffer(rs.getString("position")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
