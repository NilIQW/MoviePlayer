package gui;

import gui.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    public static void main(String[] args) {
        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Model model = Model.getInstance();
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("view/main.fxml"));
        Parent root = loader.load();
        MainController mainController = loader.getController();
        mainController.setModel(model);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}