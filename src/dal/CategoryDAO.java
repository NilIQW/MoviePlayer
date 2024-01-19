package dal;

import be.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO implements ICategoryDAO {
    private final ConnectionManager connectionManager;
    public CategoryDAO(){
        this.connectionManager = new ConnectionManager();
    }
    public void saveCategory(Category category) throws SQLException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO Category (name) VALUES (?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, category.getName());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    category.setId(id);
                }
            }

        } catch (SQLException e) {
            throw new SQLException("Category wasn't saved, try again");
        }
    }
    @Override
    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Category");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");

                Category category = new Category(name);
                category.setId(id);

                categories.add(category);
            }

        } catch (SQLException e) {
            throw new SQLException("Couldn't get all categories from the SQL database", e);
        }

        return categories;
    }
    public void deleteCategory(int categoryId) throws SQLException {

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM Category WHERE id = ?")) {

            statement.setInt(1, categoryId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new SQLException("Category wasn't deleted, try again", e);
        }
    }

}
