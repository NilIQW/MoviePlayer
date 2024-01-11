package dal;

import be.Category;
import dal.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CategoryDao {
    private ConnectionManager connectionManager;
    public CategoryDao(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public void saveCategory(Category category) {
        try (Connection connection = connectionManager.getConnection()) {
            String sql = "INSERT INTO category (name) VALUES (?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, category.getName());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
