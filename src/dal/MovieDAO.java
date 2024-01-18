package dal;

import be.Category;
import be.Movie;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public  class MovieDAO implements IMovieDAO{
    private final ConnectionManager connectionManager;

    public MovieDAO() throws SQLException {
        this.connectionManager = new ConnectionManager();

    }


    /**
     * Creates a new movie record in the database.
     *
     * @param m The Movie object containing details to be stored in the database.
     */
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
                    m.setId((int) generatedKey);

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void addMovieToCategory(Category category, Movie movie) throws SQLServerException {
        try (Connection con = connectionManager.getConnection()) {
            String sql = "INSERT INTO MovieCategory ( MovieId, CategoryId) VALUES (?,?)";
            try (PreparedStatement pt = con.prepareStatement(sql)) {
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
        List<Movie> movies = new ArrayList<>();
        try (Connection con = connectionManager.getConnection()) {
            String sql = "SELECT * FROM Movie";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {

                String title = rs.getString("name");
                Double rating = rs.getDouble("rating");
                String path = rs.getString("filelink");
                // Retrieve the Date from the ResultSet
                Date sqlDate = rs.getDate("lastview");

                // Convert java.sql.Date to java.time.LocalDate
                LocalDate lastView = sqlDate.toLocalDate();


                Movie m = new Movie(title, rating, path, lastView);
                movies.add(m);

            }
            return movies;
        } catch (SQLException e) {
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
                // Set the MovieID parameter in the SQL query to the ID of the current
                pt.setInt(1, category.getId());

                try (ResultSet rs = pt.executeQuery()) {
                    while (rs.next()) {
                        int movieId = rs.getInt("MovieId");
                        String title = rs.getString("name");
                        Double rating = rs.getDouble("rating");
                        String path = rs.getString("filelink");
                        Date sqlDate = rs.getDate("lastview");

                        // Convert java.sql.Date to java.time.LocalDate
                        LocalDate lastView = sqlDate.toLocalDate();


                        Movie m = new Movie(title, rating, path, lastView);
                        m.setId(movieId);
                        MoviesInCategory.add(m);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return MoviesInCategory;
    }
    @Override
    public void updateMovieLastViewDate(Movie movie, LocalDate date) throws SQLServerException {
        try (Connection con = connectionManager.getConnection()) {
            String sql = "UPDATE Movie SET lastview = ? WHERE id = ?";
            try (PreparedStatement pt = con.prepareStatement(sql)) {
                pt.setDate(1, Date.valueOf(date));
                pt.setInt(2, movie.getId());
                pt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("An error occurred when updating the last time you watched a movie: " + e.getMessage(), e);
        }
    }
    @Override
    public void updateMovieRating(Movie m)throws SQLServerException {
        try (Connection con = connectionManager.getConnection()) {
            String sql = "UPDATE Movie SET rating = ? WHERE id = ?";
            try (PreparedStatement pt = con.prepareStatement(sql)) {
                pt.setDouble(1, m.getRating());
                pt.setInt(2, m.getId());
                pt.executeUpdate();
            }

            // Fetch the updated movie with the correct rating
            String selectSql = "SELECT * FROM Movie WHERE id = ?";
            try (PreparedStatement selectPt = con.prepareStatement(selectSql)) {
                selectPt.setInt(1, m.getId());
                ResultSet rs = selectPt.executeQuery();

                if (rs.next()) {
                    Double updatedRating = rs.getDouble("rating");
                    // Update the rating in the Movie object
                    m.setRating(updatedRating);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void deleteMovie(int movieId, int categoryId) {
        System.out.println(movieId);
        try (Connection con = connectionManager.getConnection()) {
            // Delete the movie from the specific category
            String sql = "DELETE FROM MovieCategory WHERE MovieId = ? AND CategoryId = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, movieId);
            statement.setInt(2, categoryId);
            statement.executeUpdate();

            // Check if the movie is associated with any other categories
            String checkSql = "SELECT COUNT(*) FROM MovieCategory WHERE MovieId = ?";
            PreparedStatement checkStatement = con.prepareStatement(checkSql);
            checkStatement.setInt(1, movieId);
            ResultSet resultSet = checkStatement.executeQuery();

            // If the movie is not associated with any other categories, delete from the Movie table
            if (resultSet.next() && resultSet.getInt(1) == 0) {
                String deleteMovieSql = "DELETE FROM Movie WHERE id = ?";
                PreparedStatement deleteMovieStatement = con.prepareStatement(deleteMovieSql);
                deleteMovieStatement.setInt(1, movieId);
                deleteMovieStatement.executeUpdate();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
