module com.github.bmakszim.homework {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens jobagency to javafx.fxml;
    exports jobagency;
    exports jobagency.entities;
    opens jobagency.entities to javafx.fxml;
}