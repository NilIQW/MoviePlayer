package gui;

import be.Category;
import be.Movie;

import java.util.List;
import java.util.stream.Collectors;

public class Filter {
    public List<Movie> filterByCategory(List<Movie> movies, Category category){
        return movies.stream()
                .filter(movie -> movie.getCategory().equals(category))
                .collect(Collectors.toList());
    }
}
