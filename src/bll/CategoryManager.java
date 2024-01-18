package bll;

import be.Category;
import dal.CategoryDAO;
import dal.ICategoryDAO;

import java.sql.SQLException;
import java.util.List;

public class CategoryManager {
    ICategoryDAO categoryDAO = new CategoryDAO();

    public void addCategory(Category category) throws SQLException {
        categoryDAO.saveCategory(category);

    }

    public void deleteCategory(int id) throws SQLException {
        categoryDAO.deleteCategory(id);
    }

    public List<Category> getAllCategories() throws SQLException {
        return categoryDAO.getAllCategories();
    }

}
