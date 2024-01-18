package dal;

import be.Category;
import be.Movie;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.SQLException;
import java.util.List;

public interface IMovieDAO {
    public void createMovie(Movie m) throws SQLServerException;
    public void addMovieToCategory(Category category, Movie movie) throws SQLServerException;
    List<Movie> getAllMoviesInCategory(Category category) throws SQLServerException;
    public void updateMovieRating(Movie m)throws SQLServerException;

}
