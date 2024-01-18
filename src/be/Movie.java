package be;

import java.time.LocalDate;

public class Movie {
    private int id;
    private String title;
    private Double rating;
    private String path;
    private LocalDate date;
    private Category category;
    private LocalDate lastViewDate;

    public Movie(String title, Double rating, String path, LocalDate lastView){
        this.title = title;
        this.rating = rating;
        this.path = path;
        this.date = lastView;
        this.lastViewDate = null;
    }
    public Movie(String title, Double rating, String path){
        this.title = title;
        this.rating = rating;
        this.path = path;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    public LocalDate getLastViewDate() {
        return lastViewDate;
    }

    public void setLastViewDate(LocalDate lastViewDate) {
        this.lastViewDate = lastViewDate;
    }
}

