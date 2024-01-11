package be;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private ArrayList<Movie> allMovies;
    private int id;

    private String name;

    public Category(String name){
        this.allMovies = new ArrayList<Movie>();
        this.name = name;
    }

    public void setAllMovies(ArrayList<Movie> songs) {
        this.allMovies = songs;
    }
    public List<Movie> getAllMovies(){
        return allMovies;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static ObservableList<Category> defaultCategory(){
        ObservableList<Category> categories = FXCollections.observableArrayList();
        categories.add(new Category("Action"));
        categories.add(new Category("Adventure"));
        categories.add(new Category("Drama"));
        categories.add(new Category("Comedy"));
        categories.add(new Category("Sci-Fi"));
        categories.add(new Category("Fantasy"));
        categories.add(new Category("Mystery"));
        categories.add(new Category("Romance"));
        categories.add(new Category("Thriller"));
        categories.add(new Category("Horror"));
        return categories;
    };

    @Override
    public String toString() {
        return name;
    }
}
