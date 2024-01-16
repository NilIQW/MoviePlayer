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

    private String[] sorting = {"Rating", "Title"};
    private Category selectedCategory;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sort.getItems().addAll(sorting);
        model = Model.getInstance();
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
        sort.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                handleSort(newValue);
            }
        });
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

    private void updateLastViewColumn() {
        lastViewColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        lastViewColumn.setCellFactory(column -> new TableCell<Movie, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {

                    setText(item.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                }
            }
        });
    }

    public void updateTable() {

        ObservableList<Movie> data = FXCollections.observableArrayList();
        titleColumn.setCellValueFactory(new PropertyValueFactory<Movie, String>("title")); //connects movie data with song table view

        ratingColumn.setCellValueFactory(new PropertyValueFactory<Movie, Integer>("rating"));

        //lastViewColumn.setCellValueFactory(new PropertyValueFactory<Movie, LocalDate>("lastView"));
        if (selectedCategory != null) {
            for (Movie movie : selectedCategory.getAllMovies()) {
                data.add(movie);
            }
            movieTable.setItems(data);
        }
    }


        public void sortMoviesByRating () {
            ObservableList<Movie> ratingSorting = movieTable.getItems();
            ratingSorting.sort(Comparator.comparingDouble(Movie::getRating));
            movieTable.setItems(ratingSorting);

        }
        public void sortMoviesByTitle () {
            ObservableList<Movie> titleSorting = movieTable.getItems();
            Collator collator = Collator.getInstance();
            titleSorting.sort(Comparator.comparing(Movie::getTitle, collator));
            movieTable.setItems(titleSorting);

        }
        private void handleSort (String selectedSort){
            if (selectedSort.equals("Rating")) {
                sortMoviesByRating();
            } else if (selectedSort.equals("Title")) {
                sortMoviesByTitle();
            }
        }



}

