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
    public void saveCategory(Category category) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO Category (name) VALUES (?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, category.getName());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);  // Extract the generated ID from the result set
                    category.setId(id);
                }
            }



        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
    @Override
    public List<Category> getAllCategories() {
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
            e.printStackTrace();
        }

        return categories;
    }

    public void updateCategory(Category category) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE Category SET name = ? WHERE id = ?")) {

            statement.setString(1, category.getName());
            statement.setInt(2, category.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    public void deleteCategory(int categoryId) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM categories WHERE id = ?")) {

            statement.setInt(1, categoryId);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }


}
