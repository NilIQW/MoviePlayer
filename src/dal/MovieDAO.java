package dal;

import be.Category;
import be.Movie;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.*;

public class MovieDAO implements IMovieDAO{
    private final ConnectionManager connectionManager;

    public MovieDAO(){
        this.connectionManager=new ConnectionManager();
    }

    @Override
    public void createMovie(Movie m) throws SQLServerException {
        try (Connection con = connectionManager.getConnection()) {
            String sql = "INSERT INTO Movie (name, rating, filelink, lastview)" + "VALUES (?,?,?,?)";
            PreparedStatement pt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pt.setString(1, m.getTitle());
            pt.setDouble(2, m.getRating());
            pt.setString(3, m.getPath());
            pt.setDate(4, Date.valueOf(java.time.LocalDate.now()));
            pt.execute();

            try (ResultSet keys = pt.getGeneratedKeys()) {
                if (keys.next()) {
                    long generatedKey = keys.getLong(1);
                    m.setId((int)generatedKey);

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addMovieToCategory(Category category, Movie movie) throws SQLServerException {
        try(Connection con = connectionManager.getConnection()){
            String sql = "INSERT INTO MovieCategory ( MovieId, CategoryId) VALUES (?,?)";
            try(PreparedStatement pt = con.prepareStatement(sql)){
                pt.setInt(1, movie.getId());
                pt.setInt(2, category.getId());
                pt.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
