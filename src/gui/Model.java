package gui;

import be.Category;
import be.Movie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class Model {
    private final static Model instance = new Model();//ensures that by using Singelton all controllers use the same model
    private final static ObservableList<Movie> movieList = FXCollections.observableArrayList();
    private final static ObservableList<Category> categoryList = FXCollections.observableArrayList();


    public static Model getInstance(){
        return instance;
    }

    public void createMovie(String title, Double rating, String path, LocalDate date){
        Movie newMovie = new Movie(title, rating, path, date); //creating movie object
        movieList.add(newMovie);
    }

    public void createCategory(String name){
        Category newCategory = new Category(name);
        categoryList.add(newCategory);
    }
}




