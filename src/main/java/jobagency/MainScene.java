package jobagency;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import jobagency.dialogs.AddUser;
import jobagency.dialogs.ApplicantsDialog;
import jobagency.dialogs.AssignUser;
import jobagency.entities.Company;
import jobagency.entities.JobOffer;
import jobagency.entities.Training;
import jobagency.entities.User;
import jobagency.tables.CompaniesTable;
import jobagency.tables.JobOffersTable;
import jobagency.tables.TrainingsTable;
import jobagency.tables.UsersTable;

import java.sql.Connection;

public class MainScene {

    private final Connection connection;

    public MainScene(Connection connection) {
        this.connection = connection;
    }

    public Scene createMainScene() {
        TabPane tabPane = new TabPane();

        Tab usersTab = new Tab("Felhasználók", createUsersTabContent());
        Tab companiesTab = new Tab("Cégek", createCompaniesTabContent());
        Tab jobOffersTab = new Tab("Állásajánlatok", createJobOffersTabContent());
        Tab trainingsTab = new Tab("Képzések", createTrainingsTabContent());

        usersTab.setClosable(false);
        companiesTab.setClosable(false);
        jobOffersTab.setClosable(false);
        trainingsTab.setClosable(false);

        tabPane.getTabs().addAll(usersTab, companiesTab, jobOffersTab, trainingsTab);

        return new Scene(tabPane, 800, 600);
    }

    private VBox createUsersTabContent() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        TableView<User> usersTable = new TableView<>();
        new UsersTable(connection).loadUsers(usersTable);

        Label titleLabel = new Label("Felhasználók kezelése");
        Button addUserButton = new Button("Felhasználó hozzáadása");
        addUserButton.setOnAction(e -> new AddUser(connection).showAddUserDialog(usersTable));

        layout.getChildren().addAll(titleLabel, usersTable, addUserButton);
        return layout;
    }

    private VBox createCompaniesTabContent() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        TableView<Company> companiesTable = new TableView<>();
        new CompaniesTable(connection).loadCompanies(companiesTable);

        Label titleLabel = new Label("Cégek kezelése");
        Button addCompanyButton = new Button("Cég hozzáadása");

        layout.getChildren().addAll(titleLabel, companiesTable, addCompanyButton);
        return layout;
    }

    private VBox createJobOffersTabContent() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        TableView<JobOffer> jobOffersTable = new TableView<>();
        new JobOffersTable(connection).loadJobOffers(jobOffersTable);

        Label titleLabel = new Label("Állásajánlatok kezelése");
        Button addJobOfferButton = new Button("Állásajánlat hozzáadása");

        layout.getChildren().addAll(titleLabel, jobOffersTable, addJobOfferButton);
        return layout;
    }

    private VBox createTrainingsTabContent() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        TableView<Training> trainingsTable = new TableView<>();
        new TrainingsTable(connection).loadTrainings(trainingsTable);

        Label titleLabel = new Label("Képzések kezelése");
        Button addTrainingButton = new Button("Képzés hozzáadása");
        Button assignUserButton = new Button("Felhasználók hozzárendelése");
        Button listApplicantsButton = new Button("Jelentkezők listázása");

        assignUserButton.setOnAction(e -> {
            Training selectedTraining = trainingsTable.getSelectionModel().getSelectedItem();
            if (selectedTraining != null) {
                new AssignUser(connection).showAssignUserDialog(selectedTraining);
            } else {
                showAlert("Figyelmeztetés", "Válasszon ki egy képzést!", Alert.AlertType.WARNING);
            }
        });

        listApplicantsButton.setOnAction(e -> {
            Training selectedTraining = trainingsTable.getSelectionModel().getSelectedItem();
            if (selectedTraining != null) {
                new ApplicantsDialog(connection).showApplicantsDialog(selectedTraining);
            } else {
                showAlert("Figyelmeztetés", "Válasszon ki egy képzést!", Alert.AlertType.WARNING);
            }
        });

        layout.getChildren().addAll(titleLabel, trainingsTable, addTrainingButton, assignUserButton, listApplicantsButton);
        return layout;
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}