package gui.controller;

import Exceptions.MovieException;
import be.Category;

import be.Movie;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import gui.DefaultCategories;

import gui.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.Rating;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;


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
    public MovieController(){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        selectedCategories.setItems(selectedCategoriesList);
        categoryChoice.setItems(DefaultCategories.defaultCategory());
        categoryChoice.setValue(DefaultCategories.defaultCategory().get(0));

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

        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "This category already exists in the selected categories");
            alert.showAndWait();

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
        // Retrieve input values
        String path = filePath.getText();
        String title = movieTitle.getText();
        Double movieRating = rating.getRating();
        LocalDate lastDate = LocalDate.now();

        try{
            // Create a Movie object with the provided details
            Movie myObject = new Movie(title, movieRating, path, lastDate);
            model.createMovie(myObject);
            // Associate the movie with selected categories
            for (String categoryName : selectedCategoriesList) {
                // Retrieve the Category object based on the category name
                Category category = model.getCategoryByName(categoryName);
                if (category != null) {
                    // Add the created movie to the retrieved category
                    model.addMovieToCategory(category, myObject);
                }
            }
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();

        } catch (MovieException e) {
            Alert a = new Alert(Alert.AlertType.ERROR, e.getMessage());
            e.printStackTrace();
            a.show();
        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        }

    }



}