package dal;

import be.Category;
import be.Movie;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO implements IMovieDAO{
    private final ConnectionManager connectionManager;

    public MovieDAO(){
        this.connectionManager=new ConnectionManager();
    }


    /**
     * Creates a new movie record in the database.
     *
     * @param m The Movie object containing details to be stored in the database.
     * */
     @Override
    public void createMovie(Movie m) throws SQLServerException {
        try (Connection con = connectionManager.getConnection()) {
            // Step 1: Define SQL query for inserting a new movie record
            String sql = "INSERT INTO Movie (name, rating, filelink, lastview)" + "VALUES (?,?,?,?)";
            PreparedStatement pt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            // Step 3: Set parameters for the prepared statement using Movie object values
            pt.setString(1, m.getTitle());
            pt.setDouble(2, m.getRating());
            pt.setString(3, m.getPath());
            pt.setDate(4, Date.valueOf(java.time.LocalDate.now()));
            pt.execute();
            // Step 5: Retrieve the generated keys (auto-generated primary key)
            try (ResultSet keys = pt.getGeneratedKeys()) {
                if (keys.next()) {
                    // Step 7: Retrieve the generated key and set it in the Movie object
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
    @Override
    public List<Movie> getAllMovies() throws SQLException {
        List <Movie> movies = new ArrayList<>();
        try(Connection con = connectionManager.getConnection()){
            String sql = "SELECT * FROM Movie";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){

                String title = rs.getString("name");
                Double rating = rs.getDouble("rating");
                String path = rs.getString("filelink");
                // Retrieve the Date from the ResultSet
                Date sqlDate = rs.getDate("lastview");

                // Convert java.sql.Date to java.time.LocalDate
                LocalDate lastView = sqlDate.toLocalDate();


                Movie m = new Movie(title,rating,path, lastView);
                movies.add(m);

            }
            return movies;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public List<Movie> getAllMoviesInCategory(Category category) throws SQLServerException {
        ArrayList<Movie> MoviesInCategory = new ArrayList<>();
        try (Connection con = connectionManager.getConnection()) {
            //Retrieve movies from a specific category
            String sql = "SELECT * FROM Movie " +
                    "JOIN MovieCategory ON Movie.ID = MovieCategory.MovieId " +
                    "WHERE MovieCategory.CategoryId = ?";
            try (PreparedStatement pt = con.prepareStatement(sql)) {
                // Set the MovieID parameter in the SQL query to the ID of the current playlist
                pt.setInt(1, category.getId());

                try (ResultSet rs = pt.executeQuery()) {
                    while (rs.next()) {
                        String title = rs.getString("name");
                        Double rating = rs.getDouble("rating");
                        String path = rs.getString("filelink");
                        Date sqlDate = rs.getDate("lastview");

                        // Convert java.sql.Date to java.time.LocalDate
                        LocalDate lastView = sqlDate.toLocalDate();


                        Movie m = new Movie(title,rating,path, lastView);
                        MoviesInCategory.add(m);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return MoviesInCategory;
    }
}
