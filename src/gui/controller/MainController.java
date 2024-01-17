package gui.controller;

import be.Category;
import be.Movie;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import gui.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.controlsfx.control.Rating;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.Collator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
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
    public Label selectedMovie;
    @FXML
    public Rating selectedMovieRating;
    @FXML
    private ListView<Category> categoryListview;
    private Model model;
    @FXML
    private TableView<Movie> movieTable;
    @FXML
    private TextField filterTextfield;

    private String[] sorting = {"Rating", "Title"};
    @FXML
    private Category selectedCategory;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();

        //initializeTableviewColumns();
        initializeCategorySelection();
        setupSortChangeListener();
        loadCategoriesToListView();
        initializeSelectedSong();
    }
    private void loadCategoriesToListView(){
        categoryListview.setItems(model.getCategoryList());
        try {

            model.loadCategories();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void setupSortChangeListener() {
        sort.getItems().addAll(sorting);

        sort.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                handleSort(newValue);
            }
        });
    }
    public void initializeCategorySelection(){
        categoryListview.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedCategory = newSelection;
                updateTable();
            }
        });
    }
    public void initializeSelectedSong(){
        movieTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                editMovieRating();
            }
        });

    }


   /*public void initializeTableviewColumns(){
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        lastViewColumn.setCellValueFactory(new PropertyValueFactory<>("lastViewDate"));

        updateLastViewColumn();
    }

    private void updateTableView(List<Movie> movies) {
        ObservableList<Movie> observableMovies = FXCollections.observableArrayList(movies);
        movieTable.setItems(observableMovies);

    }*/
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
        if (selectedCategory != null) {
            for (Movie movie : selectedCategory.getAllMovies()) {
                data.add(movie);
            }
            movieTable.setItems(data);
        }
    }

    private void updateLastViewColumn() {
        lastViewColumn.setCellValueFactory(new PropertyValueFactory<>("lastViewDate"));
        lastViewColumn.setCellFactory(column -> new TableCell<Movie, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    // Format the date for display
                    setText(item.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                }
            }
        });
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
        if(sort.getValue()!= null){
        handleSort(sort.getValue(),filteredMovies);}
        // Update the TableView with the filtered movies
       // updateTableView(filteredMovies);
        movieTable.setItems(filteredMovies);
    }
    public void playButton(ActionEvent actionEvent) {
        Movie selectedMovie = movieTable.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            String filepath = selectedMovie.getPath();
            playMovie(filepath);
        }
    }
    private void playMovie(String filePath) {
        try {
            Movie selectedMovie = movieTable.getSelectionModel().getSelectedItem();
            if (selectedMovie != null) {
                selectedMovie.setLastViewDate(LocalDate.now()); // Update last view date
                java.awt.Desktop.getDesktop().open(new File(filePath));

                // Update the TableView if necessary
                movieTable.refresh();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void deleteCategory(ActionEvent actionEvent) { //this method might need additional work
        Category selectedCategory = categoryListview.getSelectionModel().getSelectedItem();
        model.getCategoryManager().deleteCategory(selectedCategory.getId());
        model.getCategoryList().remove(selectedCategory);
    }
    private void handleSort (String selectedSort){
        if (selectedSort.equals("Rating")) {
            sortMoviesByRating(movieTable.getItems());
        } else if (selectedSort.equals("Title")) {
            sortMoviesByTitle(movieTable.getItems());
        }
    }
    private void handleSort (String selectedSort, ObservableList<Movie> Sorting){

        if (selectedSort.equals("Rating")) {
            sortMoviesByRating(Sorting);
        } else if (selectedSort.equals("Title")) {
            sortMoviesByTitle(Sorting);
        }
    }

    public void sortMoviesByRating (ObservableList<Movie> ratingSorting) {
        ratingSorting.sort(Comparator.comparingDouble(Movie::getRating));
        movieTable.setItems(ratingSorting);

    }
    public void sortMoviesByTitle (ObservableList<Movie> titleSorting) {
        Collator collator = Collator.getInstance();
        titleSorting.sort(Comparator.comparing(Movie::getTitle, collator));
        movieTable.setItems(titleSorting);

    }

    public void editMovieRating(){
        Movie selectedM = movieTable.getSelectionModel().getSelectedItem();
        selectedMovie.setText(selectedM.getTitle());
        selectedMovieRating.setRating(selectedM.getRating());
    }


    public void changeRatingButton(ActionEvent actionEvent) throws SQLServerException {
        Movie selectedM = movieTable.getSelectionModel().getSelectedItem();

        if (selectedM != null) {
            System.out.println(selectedM.getId());
            System.out.println(selectedM.getRating());

            double newRating = selectedMovieRating.getRating();
            selectedM.setRating(newRating);
            model.updateMovieRating(selectedM);
            movieTable.refresh();

        }

    }
    public void test(){

    }

}


