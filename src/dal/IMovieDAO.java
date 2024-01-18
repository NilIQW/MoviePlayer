package dal;

import be.Category;
import be.Movie;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface IMovieDAO {
    public void createMovie(Movie m) throws SQLServerException;
    public void addMovieToCategory(Category category, Movie movie) throws SQLServerException;
    List<Movie> getAllMovies() throws SQLException;
    List<Movie> getAllMoviesInCategory(Category category) throws SQLServerException;
    public void updateMovieRating(Movie m)throws SQLServerException;
    public void updateMovieLastViewDate(Movie movie, LocalDate date)throws SQLServerException;

}
