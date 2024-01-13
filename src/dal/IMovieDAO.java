package dal;

import be.Category;
import be.Movie;
import com.microsoft.sqlserver.jdbc.SQLServerException;

public interface IMovieDAO {
    public void createMovie(Movie m) throws SQLServerException;
    public void addMovieToCategory(Category category, Movie movie) throws SQLServerException;
}
