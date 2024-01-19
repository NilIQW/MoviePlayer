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
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Controller for the New Category view.
 * This class is responsible for handling user interactions when adding a new category.
 */
public class NewCategoryController implements Initializable {
    @FXML
    private Button closeButton; // Button to close the New Category window
    @FXML
    private TextField textField; // TextField to enter the name of the new category
    private Model model; // Model instance for interacting with the business logic

    /**
     * Called when the 'Add Category' button is pressed.
     * Delegates to the SaveCategory method to save the new category.
     *
     * @param event ActionEvent triggered by clicking the button.
     */
    public void addCat(ActionEvent event) throws SQLException {
        SaveCategory(event);
    }

    /**
     * Saves the new category entered in the textField to the database and clears the textField.
     * Closes the window after saving the category.
     *
     * @param event ActionEvent triggered by clicking the 'Save' button.
     */
    public void SaveCategory(ActionEvent event) throws SQLException {
        model.createCategory(textField.getText());
        textField.clear();
        closeWindow(event);
    }

    /**
     * Closes the New Category window.
     *
     * @param event ActionEvent triggered by clicking the 'Close' button.
     */
    public void closeWindow(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Initializes the controller class. This method is automatically called after the FXML file has been loaded.
     * Retrieves the Model instance.
     *
     * @param location The location used to resolve relative paths for the root object, or null if unknown.
     * @param resources The resources used to localize the root object, or null if unknown.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
    }
}