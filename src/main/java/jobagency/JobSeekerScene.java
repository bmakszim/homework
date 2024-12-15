package jobagency;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import jobagency.entities.JobOffer;
import jobagency.entities.Training;
import jobagency.tables.JobOffersTable;
import jobagency.tables.TrainingsTable;

import java.sql.Connection;

public class JobSeekerScene {

    private final Connection connection;

    public JobSeekerScene(Connection connection) {
        this.connection = connection;
    }

    public Scene createJobSeekerScene() {
        VBox jobSeekerLayout = new VBox(10);
        jobSeekerLayout.setPadding(new Insets(10));

        Label jobOffersLabel = new Label("Elérhető állásajánlatok");
        TableView<JobOffer> jobOffersTable = new TableView<>();
        new JobOffersTable(connection).loadJobOffers(jobOffersTable);

        Label trainingsLabel = new Label("Elérhető képzések");
        TableView<Training> trainingsTable = new TableView<>();
        new TrainingsTable(connection).loadTrainings(trainingsTable);

        jobSeekerLayout.getChildren().addAll(jobOffersLabel, jobOffersTable, trainingsLabel, trainingsTable);

        return new Scene(jobSeekerLayout, 800, 600);
    }
}
