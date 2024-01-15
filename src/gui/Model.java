package gui;

import Exceptions.MovieException;
import be.Category;
import be.Movie;
import bll.CategoryManager;
import bll.MovieManager;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Model {
    MovieManager movieManager;
    CategoryManager categoryManager;
    private final static Model instance = new Model();//ensures that by using Singelton all controllers use the same model
    private final static ObservableList<Movie> movieList = FXCollections.observableArrayList();
    private static ObservableList<Category> categoryList;

    static {
        initializeCategories();
    }
    private Model() {
        categoryList = DefaultCategories.defaultCategory();
        movieManager = new MovieManager();
        categoryManager = new CategoryManager();
    }   //getAllCategories();


    public ObservableList<Category> getCategoryList(){
        return categoryList;
    }
    public ObservableList<Movie> returnMovieList() throws SQLException {
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
        categoryList.add(newCategory);
        categoryManager.addCategory(newCategory);

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

    public void getAllCategories() {
        CategoryManager categoryManager = new CategoryManager();
        categoryManager.getAllCategories();
    }
    public void loadMovies() throws SQLException {
        movieList.clear();
        movieList.addAll(movieManager.getAllMovies());
    }



}




