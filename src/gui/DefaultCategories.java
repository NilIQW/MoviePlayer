package gui;

import be.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DefaultCategories {
    private DefaultCategories(){

    }
    public static ObservableList<Category> defaultCategory(){
        ObservableList<Category> categories = FXCollections.observableArrayList();
        categories.add(new Category("Action"));
        categories.add(new Category("Adventure"));
        categories.add(new Category("Drama"));
        categories.add(new Category("Comedy"));
        categories.add(new Category("Sci-Fi"));
        categories.add(new Category("Fantasy"));
        categories.add(new Category("Mystery"));
        categories.add(new Category("Romance"));
        categories.add(new Category("Thriller"));
        categories.add(new Category("Horror"));
        return categories;
    };
}
