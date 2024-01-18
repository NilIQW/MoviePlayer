package dal;

import be.Category;

import java.sql.SQLException;
import java.util.List;

public interface ICategoryDAO {
    void saveCategory(Category category) throws SQLException;

    List<Category> getAllCategories() throws SQLException;

    void deleteCategory(int categoryId) throws SQLException;

}
