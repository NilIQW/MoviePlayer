package gui.controller;
import be.Category;
import gui.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewCategoryController {
    @FXML
    private Button closeButton;
    @FXML
    private TextField textField;

    private Model model;

    public void addCat(ActionEvent event) {
        SaveCategory( event);
    }
    public void setModel(Model model) {
        this.model = model;
    }
    public void SaveCategory(ActionEvent event) {
        model = Model.getInstance();
       model.createCategory(textField.getText());
       textField.clear();
        closeWindow(event);
    }

    public void closeWindow(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }


   public void setCategories() {this.model = model;

    }
}
