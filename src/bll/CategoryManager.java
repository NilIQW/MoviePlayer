package bll;

import be.Category;
import dal.CategoryDAO;
import dal.ICategoryDAO;

import java.util.List;

public class CategoryManager {
    ICategoryDAO categoryDAO = new CategoryDAO();

    public void addCategory(Category category) {
        categoryDAO.saveCategory(category);

    }

    public void deleteCategory(int id){
        categoryDAO.deleteCategory(id);
    }
    public  void updateCategory(Category category){
    }
    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategories();
    }

}
