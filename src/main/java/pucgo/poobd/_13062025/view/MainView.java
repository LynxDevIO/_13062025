package pucgo.poobd._13062025.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainView extends Application {
    private Stage st;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/src/main/resources/pucgo/poobd/_13062025/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 600);
        stage.setTitle("Atividade Avaliativa - Parte 2");
        stage.setScene(scene);

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