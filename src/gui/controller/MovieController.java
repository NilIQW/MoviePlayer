package gui.controller;

import be.Category;
import be.Movie;
import gui.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.Rating;

import java.io.File;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import java.io.IOException;


public class MovieController implements Initializable {
    @FXML
    public Rating rating;
    @FXML
    private ListView selectedCategories;
    @FXML
    private ChoiceBox categoryChoice;
    ObservableList<String> selectedCategoriesList = FXCollections.observableArrayList();
    @FXML
    private TextField movieTitle;
    private Stage stage;
    @FXML
    private TextField filePath;

    private Model model;

    public void setModel(Model model) {
        this.model = model;
        categoryChoice.setItems(model.getCategoryList());
        categoryChoice.setValue(model.getCategoryList().get(0));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectedCategories.setItems(selectedCategoriesList);

    }


    public void newCategoryButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/NewCategoryView.fxml"));
        Parent root = loader.load();
       // ((NewCategoryController) loader.getController()).setCategories();
        Stage newCategoryStage = new Stage();
        newCategoryStage.setTitle("");
        newCategoryStage.setScene(new Scene(root));
        newCategoryStage.setResizable(false);
        newCategoryStage.show();

    }


    public void addCategoryButton(ActionEvent actionEvent) {

        Category selectedCategory = (Category) categoryChoice.getSelectionModel().getSelectedItem();
        if (selectedCategory != null && !selectedCategoriesList.contains(selectedCategory.getName())) {
            selectedCategoriesList.add(selectedCategory.getName());

        }

    }
    public void removeCategoryButton(ActionEvent actionEvent) {

        String selectedCategory = (String) selectedCategories.getSelectionModel().getSelectedItem();
        if (selectedCategory != null){
            selectedCategoriesList.remove(selectedCategory);
        }
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
        String movieTitleText = movieTitle.getText();
        String filePathText = filePath.getText();
        double ratingValue = rating.getRating();
        List<String> selectedCategories = new ArrayList<>(selectedCategoriesList);

        if (!movieTitleText.isEmpty() && !filePathText.isEmpty() && !selectedCategories.isEmpty()) {
            // Create a Movie object with the entered information
            Movie newMovie = new Movie(movieTitleText, ratingValue, filePathText, LocalDate.now());

            // Add the new movie to the model
            model.addMovie(newMovie);

            // Update the categories in the main controller
            //((MainController) categoryListview.getScene().getUserData()).updateModel();

            // Close the current stage (movie stage)
            ((Node) actionEvent.getSource()).getScene().getWindow().hide();
        } else {
            // Handle validation or show an error message if required fields are not filled
            // For example, display an alert or log a message
        }
    }




}