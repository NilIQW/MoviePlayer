package gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    public void addMovieButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/Movie.fxml"));
        Parent root = loader.load();
        MovieController movieController = loader.getController();
        Stage stage = new Stage();
        stage.setTitle("Media Player");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
