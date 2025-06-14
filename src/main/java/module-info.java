module pucgo.poobd._13062025 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires org.xerial.sqlitejdbc;


    opens pucgo.poobd._13062025 to javafx.fxml;
    exports pucgo.poobd._13062025;
    exports pucgo.poobd._13062025.database;
    opens pucgo.poobd._13062025.database to javafx.fxml;
    exports pucgo.poobd._13062025.controller;
    opens pucgo.poobd._13062025.controller to javafx.fxml;
    exports pucgo.poobd._13062025.view;
    opens pucgo.poobd._13062025.view to javafx.fxml;
}