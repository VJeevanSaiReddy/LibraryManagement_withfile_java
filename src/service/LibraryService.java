package service;

import model.Book;
import model.User;
import repository.BookRepository;
import repository.UserRepository;

import java.util.List;

public class LibraryService {
    private BookRepository bookRepository;
    private UserRepository userRepository;

    public LibraryService() {
        this.bookRepository = new BookRepository();
        this.userRepository = new UserRepository();
    }

    public void addBook(Book book) {
        bookRepository.addBook(book);
    }

    public Book getBookById(String id) {
        return bookRepository.getBookById(id);
    }

    public List<Book> getAllBooks() {
        return bookRepository.getAllBooks();
    }

    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.searchBooksByTitle(title);
    }

    public List<Book> searchBooksByAuthor(String author) {
        return bookRepository.searchBooksByAuthor(author);
    }

    public void updateBook(Book book) {
        bookRepository.updateBook(book);
    }

    public boolean deleteBook(String id) {
        return bookRepository.deleteBook(id);
    }

    public void addUser(User user) {
        userRepository.addUser(user);
    }

    public User getUserById(String id) {
        return userRepository.getUserById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public void updateUser(User user) {
        userRepository.updateUser(user);
    }

    public boolean deleteUser(String id) {
        return userRepository.deleteUser(id);
    }

    public boolean borrowBook(String userId, String bookId) {
        User user = userRepository.getUserById(userId);
        Book book = bookRepository.getBookById(bookId);

        if (user == null || book == null) {
            return false;
        }

        if (!book.isAvailable()) {
            return false;
        }

        user.borrowBook(bookId);
        book.setAvailable(false);

        userRepository.updateUser(user);
        bookRepository.updateBook(book);

        return true;
    }

    public boolean returnBook(String userId, String bookId) {
        User user = userRepository.getUserById(userId);
        Book book = bookRepository.getBookById(bookId);

        if (user == null || book == null) {
            return false;
        }

        boolean removed = user.returnBook(bookId);
        if (removed) {
            book.setAvailable(true);
            userRepository.updateUser(user);
            bookRepository.updateBook(book);
            return true;
        }

        return false;
    }

    public List<Book> getBorrowedBooks(String userId) {
        User user = userRepository.getUserById(userId);
        if (user == null) {
            return List.of();
        }

        return user.getBorrowedBookIds().stream()
                .map(bookRepository::getBookById)
                .filter(book -> book != null)
                .toList();
    }
}