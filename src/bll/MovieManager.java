package bll;

import be.Movie;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.DAOMovie;
import dal.IMovieDAO;
import Exceptions.MovieException;


public class MovieManager {
    IMovieDAO movieDao = new DAOMovie();

    public void createMovie (Movie m) throws MovieException {
        if (!isMovieValid(m))
            throw new MovieException("Movie is incomplete");
        try {
            movieDao.createMovie(m);
        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        }

    }


    private boolean isMovieValid (Movie movie){
        return movie != null && !movie.getTitle().isEmpty() && movie.getRating() >= 0 && !movie.getPath().isEmpty();

    }

}


