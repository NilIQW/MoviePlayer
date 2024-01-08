package be;

import java.time.LocalDate;

public class Movie {
    private int id;
    private String title;
    private Double rating;
    private String path;
    private LocalDate date;

    public Movie(String title, Double rating, String path, LocalDate date){
        this.title = title;
        this.rating = rating;
        this.path = path;
        this.date = date;
    }

    public Movie(int id, String title, Double rating, String path, LocalDate date){
        this.id = id;
        this.title = title;
        this.rating = rating;
        this.path = path;
        this.date = date;
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
}
