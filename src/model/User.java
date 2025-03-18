package model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id;
    private String name;
    private String email;
    private List<String> borrowedBookIds;
    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.borrowedBookIds = new ArrayList<>();
    }
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public List<String> getBorrowedBookIds() {
        return borrowedBookIds;
    }
    public void borrowBook(String bookId) {
        borrowedBookIds.add(bookId);
    }
    public boolean returnBook(String bookId) {
        return borrowedBookIds.remove(bookId);
    }
    public String toCsvString() {
        return id + "," + name + "," + email + "," + String.join(";", borrowedBookIds);
    }
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", borrowedBookIds=" + borrowedBookIds +
                '}';
    }
}
