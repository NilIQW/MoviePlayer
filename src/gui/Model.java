package gui;

import Exceptions.MovieException;
import be.Category;
import be.Movie;
import bll.MovieManager;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class Model {
    MovieManager movieManager;
    private final static Model instance = new Model();//ensures that by using Singelton all controllers use the same model
    private final static ObservableList<Movie> movieList = FXCollections.observableArrayList();
    private ObservableList<Category> categoryList;

    public Model(){
        categoryList = Category.defaultCategory();
        movieManager = new MovieManager();
    }

    public ObservableList<Category> getCategoryList(){
        return categoryList;
    }


    public static Model getInstance(){
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
}




