package be;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Category {
    private List<Movie> allMovies;
    private int id;

    private String name;

    public Category(String name){
        this.allMovies = new ArrayList<Movie>();
        this.name = name;
    }

    public void setAllMovies(List<Movie> movies) {
        this.allMovies = movies;
    }
    public List<Movie> getAllMovies(){
        return allMovies;
    }
    public void addMovie (Movie movie){
        allMovies.add(movie);
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



    @Override
    public String toString() {
        return name;
    }

    public void removeMovie(Movie movie) {
        // Assuming you have a list of movies in your Category class
        List<Movie> movies = getAllMovies();

        // Remove the specified movie from the list
        movies.remove(movie);


    }
}
