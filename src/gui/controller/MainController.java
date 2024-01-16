package gui.controller;

import be.Category;
import be.Movie;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import gui.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.Collator;
import java.time.LocalDate;

import java.util.Comparator;

import java.time.format.DateTimeFormatter;

import java.util.List;
import java.util.ResourceBundle;



public class MainController implements Initializable {
    @FXML
    public TableColumn titleColumn;
    @FXML
    public TableColumn ratingColumn;
    @FXML
    public TableColumn lastViewColumn;
    @FXML
    public ChoiceBox<String> sort;
    @FXML
    private ListView<Category> categoryListview;
    private Model model;
    @FXML
    private TableView<Movie> movieTable;
    @FXML
    private TextField filterTextfield;

    private String[] sorting = {"Rating", "Title"};
    private Category selectedCategory;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sort.getItems().addAll(sorting);
        model = Model.getInstance();

        ObservableList<Movie> data;

        //ObservableList<Movie> data;

        categoryListview.setItems(model.getCategoryList());
        try {

            model.loadCategories();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        categoryListview.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedCategory = newSelection;
                updateTable();
            }
        });

        // Initialize TableView columns
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        //lastViewColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        //updateLastViewColumn();


        // Update the playlistSongsView based on the selected playlist
        categoryListview.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateMoviesInCat(newValue);
        });

    }

    private void updateMoviesInCat(Category selectedCategory) {
        if (selectedCategory != null) {
            try {
                List<Movie> moviesInCategory = model.getMoviesInCategory(selectedCategory);
                // Update TableView with movies in the selected category
                updateTableView(moviesInCategory);
            } catch (SQLServerException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void updateTableView(List<Movie> movies) {
        ObservableList<Movie> observableMovies = FXCollections.observableArrayList(movies);
        movieTable.setItems(observableMovies);

    }


    public void addMovieButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/Movie.fxml"));
        Parent root = loader.load();
        MovieController movieController = loader.getController();
        movieController.setMainController(this);
        Stage stage = new Stage();
        stage.setTitle("Media Player");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void deleteMovieButton(ActionEvent actionEvent) {
    }


    public void deleteMovieCategoryButton(ActionEvent actionEvent) {
    }


    public void showAlert() {
        Alert alert = new Alert(Alert.AlertType.NONE, "Please delete the movies under 2.5 rating and/or haven't been opened in 2 years", ButtonType.OK);
        alert.setTitle("Attention");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: linear-gradient(rgb(254, 102, 125), rgb(255, 163, 117));");

        alert.showAndWait();

    }




    public void updateTable() {
        ObservableList<Movie> data = FXCollections.observableArrayList();
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        //lastViewColumn.setCellValueFactory(new PropertyValueFactory<>("lastView"));
        if (selectedCategory != null) {
            for (Movie movie : selectedCategory.getAllMovies()) {
                data.add(movie);
            }
            movieTable.setItems(data);
        }
    }

    public void filterButton(ActionEvent actionEvent) throws SQLServerException {
        String filterText = filterTextfield.getText().toLowerCase().trim();
        ObservableList<Movie> filteredMovies = FXCollections.observableArrayList();

        for (Category category : model.getCategoryList()) {
            String categoryName = category.getName().toLowerCase();

            // Check if the filter text matches the category name
            if (categoryName.contains(filterText)) {
                // Add all movies in the matching category to the filtered list
                filteredMovies.addAll(category.getAllMovies());
            } else {
                for (Movie movie : category.getAllMovies()) {
                String title = movie.getTitle().toLowerCase();
                String rating = String.valueOf(movie.getRating()).toLowerCase();

                if (title.contains(filterText) || rating.contains(filterText)) {
                    filteredMovies.add(movie);
                }}
            }
        }

        // Update the TableView with the filtered movies
        updateTableView(filteredMovies);
    }



    public void playButton(ActionEvent actionEvent) {
        Movie selectedMovie = movieTable.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            String filepath = selectedMovie.getPath();
            playMovie(filepath);
        }
    }

    private void playMovie(String filePath) {
        // TODO: Implement code to open the default media player with the specified file
        // You can use java.awt.Desktop or other platform-specific methods
        // For simplicity, I'll provide a basic example using java.awt.Desktop

        try {
            java.awt.Desktop.getDesktop().open(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteCategory(ActionEvent actionEvent) { //this method might need additional work
        Category selectedCategory = categoryListview.getSelectionModel().getSelectedItem();
        model.getCategoryManager().deleteCategory(selectedCategory.getId());
        model.getCategoryList().remove(selectedCategory);
    }




}


