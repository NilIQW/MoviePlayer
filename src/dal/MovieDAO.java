package dal;

import be.Category;
import be.Movie;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public  class MovieDAO implements IMovieDAO {
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

            String sql = "INSERT INTO Movie (name, rating, filelink)" + "VALUES (?,?,?)";
            PreparedStatement pt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            // Step 3: Set parameters for the prepared statement using Movie object values
            pt.setString(1, m.getTitle());
            pt.setDouble(2, m.getRating());
            pt.setString(3, m.getPath());
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
    /**
     * Retrieves a list of movies in a specific category from the database.
     *
     * @param category The Category for which movies are to be retrieved.
     * @return A List of Movie objects in the specified category.
     */
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
                        if(sqlDate != null) {
                            LocalDate lastView = sqlDate.toLocalDate();
                            Movie m = new Movie(title, rating, path, lastView);
                            m.setId(movieId);
                            MoviesInCategory.add(m);
                        }else {
                            Movie m = new Movie(title, rating, path);
                            m.setId(movieId);
                            MoviesInCategory.add(m);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return MoviesInCategory;
    }
    /**
     * Updates the last viewed date of a specific movie in the database.
     *
     * @param movie The Movie object whose last view date needs to be updated.
     * @param date The new last viewed date to be set for the movie.
     * @throws SQLServerException If there is an issue with the SQL Server database connectivity.
     */
    @Override
    public void updateMovieLastViewDate(Movie movie, LocalDate date) throws SQLServerException {
        try (Connection con = connectionManager.getConnection()) {
            // SQL query to update the 'lastview' column for a specific movie based on its ID
            String sql = "UPDATE Movie SET lastview = ? WHERE id = ?";
            try (PreparedStatement pt = con.prepareStatement(sql)) {
                // Setting the date in the prepared statement
                pt.setDate(1, Date.valueOf(date));
                // Setting the movie ID in the prepared statement
                pt.setInt(2, movie.getId());
                // Executing the update query
                pt.executeUpdate();
            }
        } catch (SQLException e) {
            // Throwing a RuntimeException if there's an SQL error, with a message for clarity
            throw new RuntimeException("An error occurred when updating the last time you watched a movie: " + e.getMessage(), e);
        }
    }
    @Override
    public void updateMovieRating(Movie m) throws SQLServerException {
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
                    // Extract the updated rating from the result set
                    Double updatedRating = rs.getDouble("rating");
                    // Update the rating in the Movie object
                    m.setRating(updatedRating);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    /**
     * Deletes a movie from a specific category. If the movie is not associated with any other categories,
     * it is also removed from the Movie table.
     *
     * @param movieId The ID of the movie to be deleted.
     * @param categoryId The ID of the category from which the movie should be removed.
     * @throws SQLServerException If there is an issue with the SQL Server database connectivity.
     */
    public void deleteMovie(int movieId, int categoryId) throws SQLServerException {
        try (Connection con = connectionManager.getConnection()) {
            // SQL query to delete the movie from the MovieCategory table
            String sql = "DELETE FROM MovieCategory WHERE MovieId = ? AND CategoryId = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, movieId);
            statement.setInt(2, categoryId);
            statement.executeUpdate();

            // SQL query to check if the movie is associated with any other categories
            String checkSql = "SELECT COUNT(*) FROM MovieCategory WHERE MovieId = ?";
            PreparedStatement checkStatement = con.prepareStatement(checkSql);
            checkStatement.setInt(1, movieId);
            ResultSet resultSet = checkStatement.executeQuery();

            // If the movie is not associated with any other categories, delete it from the Movie table
            if (resultSet.next() && resultSet.getInt(1) == 0) {
                String deleteMovieSql = "DELETE FROM Movie WHERE id = ?";
                PreparedStatement deleteMovieStatement = con.prepareStatement(deleteMovieSql);
                deleteMovieStatement.setInt(1, movieId);
                deleteMovieStatement.executeUpdate();
            }
        } catch (SQLException e) {
            // Throwing a RuntimeException if there's an SQL error, with a message for clarity
            throw new RuntimeException(e);
        }
    }
}