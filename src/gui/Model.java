package gui;

import Exceptions.MovieException;
import be.Category;
import be.Movie;
import bll.CategoryManager;
import bll.MovieManager;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Model {
    static MovieManager movieManager;
    private CategoryManager categoryManager;
    private final static Model instance;//ensures that by using Singelton all controllers use the same model

    static {
        try {
            instance = new Model();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private final static ObservableList<Movie> movieList = FXCollections.observableArrayList();
    private static ObservableList<Category> categoryList;

    static {
        initializeCategories();
    }
    private Model() throws SQLException {
        categoryList = DefaultCategories.defaultCategory();
        movieManager = new MovieManager();
        categoryManager = new CategoryManager();

    }
    public CategoryManager getCategoryManager() {
        return categoryManager;
    }
    public ObservableList<Category> getCategoryList(){
        return categoryList;
    }
    public static ObservableList<Movie> returnMovieList() throws SQLException {
        loadMovies();
        return movieList;
    }

    public static Model getInstance() {
        return instance;
    }

    public void createMovie(Movie newMovie) throws MovieException {
        movieList.add(newMovie);
        movieManager.createMovie(newMovie);

    }
    public void createCategory(String name){
        Category newCategory = new Category(name);
        if(!categoryExists(newCategory)) {
            categoryList.add(newCategory);
            categoryManager.addCategory(newCategory);
        } else {
            Alert alert = new Alert(Alert.AlertType.NONE, "Category already exists", ButtonType.OK);
            alert.setTitle("Unsuccessful");

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.setStyle("-fx-background-color: linear-gradient(rgb(254, 102, 125), rgb(255, 163, 117));");

            alert.showAndWait();
        }
    }
    public boolean categoryExists(Category category) {
        List<Category> existingCategories = categoryList;

        for (Category existingCategory : existingCategories) {
            if (existingCategory.getName().equals(category.getName())) {
                return true;
            }
        }
        return false;
    }
    public void addMovieToCategory(Category category, Movie movie) throws SQLServerException {
        movieManager.addMovieToCategory(category, movie);
    }
    /**
     * Retrieves a Category from the categoryList based on its name.
     * @param categoryName The name of the Category to be retrieved.
     * @return The Category with the specified name.
     */
    public Category getCategoryByName(String categoryName) throws SQLServerException {
        for (Category category : categoryList) {
            if (category.getName().equals(categoryName)) {
                return category;
            }
        }
        return null;

    }


    private static void initializeCategories() {
        ObservableList<Category> defaultCategories = DefaultCategories.defaultCategory();
        categoryList = FXCollections.observableArrayList(defaultCategories);
    }

    public void loadCategories() throws SQLServerException {
        categoryList.clear();
        categoryList.addAll(categoryManager.getAllCategories());
        //loops through categories and associates them with category, after sets all movies to AllMovies list
        for (Category category : categoryList) {
            List<Movie> moviesInCategory = movieManager.getAllMoviesInCategory(category);
            category.setAllMovies(moviesInCategory);
        }
    }

    public static void loadMovies() throws SQLException {
        movieList.clear();
        movieList.addAll(movieManager.getAllMovies());
    }

    public void updateMovieRating(Movie movie) throws SQLServerException {
        movieManager.updateMovieRating(movie);
    }


    public void deleteMovie(int movieId,int categoryId)throws SQLException{
       movieManager.deleteMovie(movieId,categoryId);
    }
    public void updateView(Movie movie, LocalDate date) throws SQLServerException {
        movieManager.updateView(movie, date);
    }


}





