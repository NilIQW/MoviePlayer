package bll;

import be.Movie;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.ConnectionManager;
import dal.DAOMovie;
import dal.IMovieDAO;
import Exceptions.MovieException;


public class MovieManager {
    IMovieDAO movieDao = new DAOMovie(new ConnectionManager());

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

}


