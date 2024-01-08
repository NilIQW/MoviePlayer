package be;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Category {
    private int id;

    private String name;

    public Category(String name){
        this.name = name;
    }

    public Category(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static final ObservableList<Category> defaultCategory(){
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

    @Override
    public String toString() {
        return name;
    }
}
