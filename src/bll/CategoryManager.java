package bll;

import be.Category;
import dal.CategoryDAO;
import dal.ICategoryDAO;

import java.util.List;

public class CategoryManager {
    ICategoryDAO categoryDAO = new CategoryDAO();

    public void addCategory(Category category) {
        if(!categoryExists(category)) {
            categoryDAO.saveCategory(category);
        }else{
            System.out.println("Category already exists" + category.getName());
    }
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
