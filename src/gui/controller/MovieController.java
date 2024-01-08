package gui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MovieController implements Initializable {

    @FXML
    public ListView selectedCategories;
    @FXML
    public ChoiceBox categoryChoice;
    ObservableList<String> selectedCategoriesList = FXCollections.observableArrayList();
    @FXML
    private TextField movieTitle;
    private Stage stage;
    @FXML
    private TextField filePath;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectedCategories.setItems(selectedCategoriesList);

    }

    public void newCategoryButton(ActionEvent actionEvent) {
    }

    public void addCategoryButton(ActionEvent actionEvent) {

        String selectedCategory = (String) categoryChoice.getSelectionModel().getSelectedItem();
        if (selectedCategory != null && !selectedCategoriesList.contains(selectedCategory)) {
            selectedCategoriesList.add(selectedCategory);

        }

    }

    public void removeCategoryButton(ActionEvent actionEvent) {
    }

    public void openFileButton(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser(); //creating instance of fileChooser
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Media Files", "*.mp4", "*.mpeg4"));
        File selectedFile = fileChooser.showOpenDialog(stage); //shows open dialog

        if (selectedFile != null) { //if file is selected add it to textField
            filePath.setText(selectedFile.getAbsolutePath());
            movieTitle.setText(selectedFile.getName());
        }
    }

    public void saveMovieButton(ActionEvent actionEvent) {
    }


}
