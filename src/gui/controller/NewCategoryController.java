package gui.controller;
import be.Category;
import gui.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class NewCategoryController implements Initializable {
    @FXML
    private Button closeButton;
    @FXML
    private TextField textField;

    private Model model;

    public void addCat(ActionEvent event) {
        SaveCategory( event);
    }
    public void SaveCategory(ActionEvent event) {
       model.createCategory(textField.getText());
       textField.clear();
       closeWindow(event);
    }

    public void closeWindow(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
    }
}
