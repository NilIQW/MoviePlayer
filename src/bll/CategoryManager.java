package bll;

import be.Category;
import dal.CategoryDAO;
import dal.ICategoryDAO;

import java.util.List;

public class CategoryManager {
    private ICategoryDAO categoryDAO;

    public CategoryManager(ICategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public void addCategory(Category category) {
        categoryDAO.saveCategory(category);
    }

    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategories();
    }
    public boolean categoryExists(Category category) {
        List<Category> existingCategories = categoryDAO.getAllCategories();

        for (Category existingCategory : existingCategories) {
            if (existingCategory.getName().equals(category.getName())) {
                return true;
            }
        }
        return false;
    }
}
