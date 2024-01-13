package gui.controller;

import be.Category;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import gui.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private ListView<Category> categoryListview;
    private Model model;

    public final void updateModel(){
        categoryListview.setItems(model.getCategoryList());
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        categoryListview.setItems(model.getCategoryList());
        try {
            model.loadCategories();
        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        }
    }

    public void addMovieButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/Movie.fxml"));
        Parent root = loader.load();
        MovieController movieController = loader.getController();
        Stage stage = new Stage();
        stage.setTitle("Media Player");
        stage.setScene(new Scene(root));
        stage.show();
    }
    public void deleteMovieButton(ActionEvent actionEvent) {
    }
    public void deleteMovieCategoryButton(ActionEvent actionEvent) {
    }

    public void showAlert(){
        Alert alert = new Alert(Alert.AlertType.NONE, "Please delete the movies under 2.5 rating and/or haven't been opened in 2 years", ButtonType.OK);
        alert.setTitle("Attention");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: linear-gradient(rgb(254, 102, 125), rgb(255, 163, 117));");

        alert.showAndWait();
    }


}
