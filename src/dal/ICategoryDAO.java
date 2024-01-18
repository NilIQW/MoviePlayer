package dal;

import be.Category;

import java.util.List;

public interface ICategoryDAO {
    void saveCategory(Category category);

    List<Category> getAllCategories();

    void updateCategory(Category category);

    void deleteCategory(int categoryId);

}
