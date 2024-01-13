package gui;

import Exceptions.MovieException;
import be.Category;
import be.Movie;
import bll.CategoryManager;
import bll.MovieManager;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.CategoryDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;

public class Model {
    MovieManager movieManager;
    private final static Model instance = new Model();//ensures that by using Singelton all controllers use the same model
    private final static ObservableList<Movie> movieList = FXCollections.observableArrayList();
    private static ObservableList<Category> categoryList;

    static {
        initializeCategories();
    }
    private Model() {
        categoryList = DefaultCategories.defaultCategory();
        movieManager = new MovieManager();

    }   //getAllCategories();


    public ObservableList<Category> getCategoryList(){
        return categoryList;
    }


    public static Model getInstance() {
        return instance;
    }

    public void createMovie(String title, Double rating, String path, LocalDate date) throws MovieException {
        Movie newMovie = new Movie(title, rating, path, date); //creating movie object
        movieList.add(newMovie);
        movieManager.createMovie(newMovie);

    }

    public void createCategory(String name){
        Category newCategory = new Category(name);
        categoryList.add(newCategory);
    }
    public void addMovieToCategory(Category category, Movie movie) throws SQLServerException {
        movieManager.addMovieToCategory(category, movie);
    }


    private static void initializeCategories() {
        ObservableList<Category> defaultCategories = DefaultCategories.defaultCategory();
        categoryList = FXCollections.observableArrayList(defaultCategories);
    }



    public List<Category> getAllCategories() {
        CategoryManager categoryManager = new CategoryManager(new CategoryDAO());
        return categoryManager.getAllCategories();
    }
}




