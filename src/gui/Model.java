package gui;

import Exceptions.MovieException;
import be.Category;
import be.Movie;
import bll.CategoryManager;
import bll.MovieManager;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
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


    public static Model getInstance() {
        return instance;
    }

    public void createMovie(Movie newMovie) throws MovieException {
        //Movie newMovie = new Movie(title, rating, path, date); //creating movie object
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

    public void loadCategories(){
        categoryList.clear();
        categoryList.addAll(categoryManager.getAllCategories());
    }

    public void getAllCategories() {
        CategoryManager categoryManager = new CategoryManager();
        categoryManager.getAllCategories();
    }

}




