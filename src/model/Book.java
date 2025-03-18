package model;
import java.time.*;

public class Book{
    private String id;
    private String title;
    private String author;
    private String genre;
    private boolean isAvailable;
    private LocalDate publishDate;

    public Book(String id, String title, String author, String genre, LocalDate publishDate){
        this.id=id;
        this.title=title;
        this.author=author;
        this.genre = genre;
        this.publishDate = publishDate;
        this.isAvailable = true;
    }
    public String getId(){
        return id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public boolean isAvailable() {
        return isAvailable;
    }
    public void setAvailable(boolean available) {
        isAvailable = available;
    }
    public LocalDate getPublishDate() {
        return publishDate;
    }
    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", genre='" + genre + '\'' +
                ", isAvailable=" + isAvailable +
                ", publishDate=" + publishDate +
                '}';
    }
    public String toCsvString() {
        return id + "," + title + "," + author + "," + genre + "," + isAvailable + "," + publishDate;
    }
}