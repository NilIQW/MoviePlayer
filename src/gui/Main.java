package gui;

import be.Category;
import bll.CategoryManager;
import dal.CategoryDAO;
import gui.controller.MainController;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;

public class Main extends Application {
    public static void main(String[] args) {
        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        CategoryManager categoryManager = new CategoryManager(new CategoryDAO());

        ObservableList<Category> defaultCategories = Category.defaultCategory();
        for (Category category : defaultCategories) {
            categoryManager.addCategory(category);
        }

        // Retrieve categories from the database
        List<Category> allCategories = categoryManager.getAllCategories();

        Model model = Model.getInstance();
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("./view/main.fxml"));
        Parent root = loader.load();
        MainController mainController = loader.getController();
        mainController.setModel(model);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}