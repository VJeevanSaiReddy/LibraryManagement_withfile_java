package repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Book;

public class BookRepository {
    private Map<String, Book> books;

    public BookRepository(){
        books = new HashMap<>();
        loadBooks();
    }
    public void addBook(Book book) {
        books.put(book.getId(), book);
        saveBooks();
    }
    public Book getBookById(String id) {
        return books.get(id);
    }
    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }
    public List<Book> searchBooksByTitle(String title) {
        return books.values().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .toList();
    }
    public List<Book> searchBooksByAuthor(String author){
        return books.values().stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .toList();
    }
    public void updateBook(Book book) {
        if (books.containsKey(book.getId())) {
            books.put(book.getId(), book);
            saveBooks();
        }
    }
    public boolean deleteBook(String id) {
        Book removed = books.remove(id);
        if (removed != null) {
            saveBooks();
            return true;
        }
        return false;
    }
    private void loadBooks() {
        File file = new File("books.csv");
        if (!file.exists()) {
            createSampleBooks();
            saveBooks();
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String id = parts[0];
                String title = parts[1];
                String author = parts[2];
                String genre = parts[3];
                boolean isAvailable = Boolean.parseBoolean(parts[4]);
                LocalDate publishDate = LocalDate.parse(parts[5]);

                Book book = new Book(id, title, author, genre, publishDate);
                book.setAvailable(isAvailable);
                books.put(id, book);
            }
        } catch (IOException e) {
            System.err.println("Error loading books: " + e.getMessage());
        }
    }
    private void saveBooks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("books.csv"))) {
            for (Book book : books.values()) {
                writer.write(book.toCsvString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving books: " + e.getMessage());
        }
    }
    private void createSampleBooks() {
        addBook(new Book("B001", "To Kill a Mockingbird", "Harper Lee", "Classic", LocalDate.of(1960, 7, 11)));
        addBook(new Book("B002", "1984", "George Orwell", "Dystopian", LocalDate.of(1949, 6, 8)));
        addBook(new Book("B003", "The Great Gatsby", "F. Scott Fitzgerald", "Classic", LocalDate.of(1925, 4, 10)));
        addBook(new Book("B004", "Pride and Prejudice", "Jane Austen", "Romance", LocalDate.of(1813, 1, 28)));
        addBook(new Book("B005", "The Hobbit", "J.R.R. Tolkien", "Fantasy", LocalDate.of(1937, 9, 21)));
    }
}
