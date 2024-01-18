package bll;

import be.Category;
import be.Movie;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.MovieDAO;
import dal.IMovieDAO;
import Exceptions.MovieException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;


public class MovieManager {
    IMovieDAO movieDao = new MovieDAO() {

    };

    public MovieManager() throws SQLException {

    }

    public void createMovie (Movie movie) throws MovieException {
        if (!isMovieValid(movie))
            throw new MovieException("Movie is incomplete");
        try {
            movieDao.createMovie(movie);
        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        }

    }
    public void updateMovieRating(Movie movie) throws SQLServerException {
        movieDao.updateMovieRating(movie);


    }
    private boolean isMovieValid (Movie movie){
        return movie != null && !movie.getTitle().isEmpty() && movie.getRating() >= 0 && !movie.getPath().isEmpty();

    }
    public void addMovieToCategory(Category category, Movie movie) throws SQLServerException {
        movieDao.addMovieToCategory(category, movie);
    }


    public List<Movie> getAllMoviesInCategory(Category category) throws SQLServerException {
        return movieDao.getAllMoviesInCategory(category);
    }

    public void deleteMovie(int movieId)throws SQLServerException{
        movieDao.deleteMovie(movieId);
    }
    public void updateView(Movie movie, LocalDate date) throws SQLServerException {
        movieDao.updateMovieLastViewDate(movie, date);
    }
}


