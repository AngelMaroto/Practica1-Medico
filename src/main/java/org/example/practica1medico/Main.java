package org.example.practica1medico;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.practica1medico.UTIL.DBConnection;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/InicioSesion.fxml"));
            Scene scene = new Scene(loader.load());
            primaryStage.setTitle("Inicio de Sesión");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() throws Exception {
        DBConnection.closeConnection();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


