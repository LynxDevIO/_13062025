package pucgo.poobd._13062025.view;

import java.io.IOException;
import java.sql.Connection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pucgo.poobd._13062025.controller.MainController;

public class MainView extends Application {
    private Stage st;
    private static Connection connection;

    public static void setConnection(Connection conn) {
        connection = conn;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/pucgo/poobd/_13062025/view/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        stage.setTitle("Atividade Avaliativa - Parte 2");
        stage.setScene(scene);

        MainController controller = fxmlLoader.getController();
        controller.setConnection(connection);

        st = stage;

        stage.show();
    }

    public static void run(String[] args) {
        launch();
    }

    public Stage getSt() {
        return st;
    }

    public void setSt(Stage st) {
        this.st = st;
    }
}