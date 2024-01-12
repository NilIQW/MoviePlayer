package bll;

import be.Category;
import be.Movie;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.MovieDAO;
import dal.IMovieDAO;
import Exceptions.MovieException;


public class MovieManager {
    IMovieDAO movieDao = new MovieDAO();

    public void createMovie (Movie movie) throws MovieException {
        if (!isMovieValid(movie))
            throw new MovieException("Movie is incomplete");
        try {
            movieDao.createMovie(movie);
        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        }

    }
    private boolean isMovieValid (Movie movie){
        return movie != null && !movie.getTitle().isEmpty() && movie.getRating() >= 0 && !movie.getPath().isEmpty();

    }
    public void addMovieToCategory(Category category, Movie movie) throws SQLServerException {
        movieDao.addMovieToCategory(category, movie);
    }

}


