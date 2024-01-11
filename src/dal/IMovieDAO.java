package dal;

import be.Movie;
import com.microsoft.sqlserver.jdbc.SQLServerException;

public interface IMovieDAO {
    public void createMovie(Movie m) throws SQLServerException;
}
