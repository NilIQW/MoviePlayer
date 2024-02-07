package dal;

import be.Category;
import be.Movie;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface IMovieDAO {
    void createMovie(Movie m) throws SQLServerException;
    void addMovieToCategory(Category category, Movie movie) throws SQLServerException;
    List<Movie> getAllMoviesInCategory(Category category) throws SQLServerException;
     void updateMovieRating(Movie m)throws SQLServerException;
     void updateMovieLastViewDate(Movie movie, LocalDate date)throws SQLServerException;
    void deleteMovie(int movieId, int categoryId) throws SQLServerException;


}
