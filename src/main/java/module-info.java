module com.example.marchchainsv2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.marchchainsv2 to javafx.fxml;
    exports com.example.marchchainsv2;
}