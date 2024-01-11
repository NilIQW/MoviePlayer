package dal;

import be.Movie;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.*;

public class DAOMovie implements IMovieDAO{
    private final ConnectionManager dbConnector = new ConnectionManager();

    @Override
    public void createMovie(Movie m) throws SQLServerException {
        try (Connection con = dbConnector.getConnection()) {
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

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
