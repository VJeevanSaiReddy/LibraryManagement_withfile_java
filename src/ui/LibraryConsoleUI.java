package ui;

import model.Book;
import model.User;
import service.LibraryService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class LibraryConsoleUI {
    private LibraryService libraryService;
    private Scanner scanner;

    public LibraryConsoleUI() {
        this.libraryService = new LibraryService();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    handleBookMenu();
                    break;
                case 2:
                    handleUserMenu();
                    break;
                case 3:
                    handleBorrowReturnMenu();
                    break;
                case 0:
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void displayMainMenu() {
        System.out.println("\n===== Library Management System =====");
        System.out.println("1. Book Management");
        System.out.println("2. User Management");
        System.out.println("3. Borrow/Return Books");
        System.out.println("0. Exit");
    }

    private void handleBookMenu() {
        boolean running = true;
        while (running) {
            displayBookMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    displayAllBooks();
                    break;
                case 2:
                    addNewBook();
                    break;
                case 3:
                    searchBooks();
                    break;
                case 4:
                    updateBook();
                    break;
                case 5:
                    deleteBook();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void displayBookMenu() {
        System.out.println("\n===== Book Management =====");
        System.out.println("1. Display All Books");
        System.out.println("2. Add New Book");
        System.out.println("3. Search Books");
        System.out.println("4. Update Book");
        System.out.println("5. Delete Book");
        System.out.println("0. Back to Main Menu");
    }

    private void displayAllBooks() {
        List<Book> books = libraryService.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }

        System.out.println("\n===== All Books =====");
        System.out.printf("%-5s %-30s %-20s %-15s %-10s %-10s%n", "ID", "Title", "Author", "Genre", "Available", "Publish Date");
        System.out.println("-----------------------------------------------------------------------------------------------------");
        for (Book book : books) {
            System.out.printf("%-5s %-30s %-20s %-15s %-10s %-10s%n",
                    book.getId(),
                    truncateString(book.getTitle(), 30),
                    truncateString(book.getAuthor(), 20),
                    truncateString(book.getGenre(), 15),
                    book.isAvailable() ? "Yes" : "No",
                    book.getPublishDate().format(DateTimeFormatter.ISO_DATE));
        }
    }

    private void addNewBook() {
        System.out.println("\n===== Add New Book =====");
        String id = getStringInput("Enter Book ID (e.g., B006): ");
        if (libraryService.getBookById(id) != null) {
            System.out.println("Book with this ID already exists!");
            return;
        }

        String title = getStringInput("Enter Title: ");
        String author = getStringInput("Enter Author: ");
        String genre = getStringInput("Enter Genre: ");
        LocalDate publishDate = getDateInput("Enter Publish Date (YYYY-MM-DD): ");

        Book book = new Book(id, title, author, genre, publishDate);
        libraryService.addBook(book);
        System.out.println("Book added successfully!");
    }

    private void searchBooks() {
        System.out.println("\n===== Search Books =====");
        System.out.println("1. Search by Title");
        System.out.println("2. Search by Author");
        System.out.println("3. Search by ID");
        int choice = getIntInput("Enter your choice: ");

        List<Book> results;
        switch (choice) {
            case 1:
                String title = getStringInput("Enter Title to search: ");
                results = libraryService.searchBooksByTitle(title);
                break;
            case 2:
                String author = getStringInput("Enter Author to search: ");
                results = libraryService.searchBooksByAuthor(author);
                break;
            case 3:
                String id = getStringInput("Enter Book ID: ");
                Book book = libraryService.getBookById(id);
                results = book != null ? List.of(book) : List.of();
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        if (results.isEmpty()) {
            System.out.println("No books found.");
            return;
        }

        System.out.println("\n===== Search Results =====");
        System.out.printf("%-5s %-30s %-20s %-15s %-10s %-10s%n", "ID", "Title", "Author", "Genre", "Available", "Publish Date");
        System.out.println("-----------------------------------------------------------------------------------------------------");
        for (Book book : results) {
            System.out.printf("%-5s %-30s %-20s %-15s %-10s %-10s%n",
                    book.getId(),
                    truncateString(book.getTitle(), 30),
                    truncateString(book.getAuthor(), 20),
                    truncateString(book.getGenre(), 15),
                    book.isAvailable() ? "Yes" : "No",
                    book.getPublishDate().format(DateTimeFormatter.ISO_DATE));
        }
    }

    private void updateBook() {
        System.out.println("\n===== Update Book =====");
        String id = getStringInput("Enter Book ID to update: ");
        Book book = libraryService.getBookById(id);

        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        System.out.println("Current details: " + book);
        System.out.println("Enter new details (leave blank to keep current value):");

        String title = getStringInputWithDefault("Title", book.getTitle());
        String author = getStringInputWithDefault("Author", book.getAuthor());
        String genre = getStringInputWithDefault("Genre", book.getGenre());
        
        System.out.print("Enter new publish date (YYYY-MM-DD) or leave blank: ");
        String dateInput = scanner.nextLine().trim();
        LocalDate publishDate = dateInput.isEmpty() ? book.getPublishDate() : LocalDate.parse(dateInput);

        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);
        book.setPublishDate(publishDate);

        libraryService.updateBook(book);
        System.out.println("Book updated successfully!");
    }

    private void deleteBook() {
        System.out.println("\n===== Delete Book =====");
        String id = getStringInput("Enter Book ID to delete: ");
        Book book = libraryService.getBookById(id);

        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        System.out.println("Book details: " + book);
        String confirm = getStringInput("Are you sure you want to delete this book? (y/n): ");

        if (confirm.equalsIgnoreCase("y")) {
            if (libraryService.deleteBook(id)) {
                System.out.println("Book deleted successfully!");
            } else {
                System.out.println("Failed to delete book.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    // User Management
    private void handleUserMenu() {
        boolean running = true;
        while (running) {
            displayUserMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    displayAllUsers();
                    break;
                case 2:
                    addNewUser();
                    break;
                case 3:
                    findUser();
                    break;
                case 4:
                    updateUser();
                    break;
                case 5:
                    deleteUser();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void displayUserMenu() {
        System.out.println("\n===== User Management =====");
        System.out.println("1. Display All Users");
        System.out.println("2. Add New User");
        System.out.println("3. Find User");
        System.out.println("4. Update User");
        System.out.println("5. Delete User");
        System.out.println("0. Back to Main Menu");
    }

    private void displayAllUsers() {
        List<User> users = libraryService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users available.");
            return;
        }

        System.out.println("\n===== All Users =====");
        System.out.printf("%-5s %-20s %-30s %-10s%n", "ID", "Name", "Email", "Books Borrowed");
        System.out.println("------------------------------------------------------------------------");
        for (User user : users) {
            System.out.printf("%-5s %-20s %-30s %-10d%n",
                    user.getId(),
                    truncateString(user.getName(), 20),
                    truncateString(user.getEmail(), 30),
                    user.getBorrowedBookIds().size());
        }
    }

    private void addNewUser() {
        System.out.println("\n===== Add New User =====");
        String id = getStringInput("Enter User ID (e.g., U004): ");
        if (libraryService.getUserById(id) != null) {
            System.out.println("User with this ID already exists!");
            return;
        }

        String name = getStringInput("Enter Name: ");
        String email = getStringInput("Enter Email: ");

        User user = new User(id, name, email);
        libraryService.addUser(user);
        System.out.println("User added successfully!");
    }

    private void findUser() {
        System.out.println("\n===== Find User =====");
        String id = getStringInput("Enter User ID: ");
        User user = libraryService.getUserById(id);

        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.println("\n===== User Details =====");
        System.out.println("ID: " + user.getId());
        System.out.println("Name: " + user.getName());
        System.out.println("Email: " + user.getEmail());
        
        List<Book> borrowedBooks = libraryService.getBorrowedBooks(id);
        System.out.println("Books Borrowed: " + borrowedBooks.size());
        
        if (!borrowedBooks.isEmpty()) {
            System.out.println("\nBorrowed Books:");
            for (Book book : borrowedBooks) {
                System.out.println("- " + book.getTitle() + " by " + book.getAuthor() + " (ID: " + book.getId() + ")");
            }
        }
    }

    private void updateUser() {
        System.out.println("\n===== Update User =====");
        String id = getStringInput("Enter User ID to update: ");
        User user = libraryService.getUserById(id);

        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.println("Current details: " + user);
        System.out.println("Enter new details (leave blank to keep current value):");

        String name = getStringInputWithDefault("Name", user.getName());
        String email = getStringInputWithDefault("Email", user.getEmail());

        user.setName(name);
        user.setEmail(email);

        libraryService.updateUser(user);
        System.out.println("User updated successfully!");
    }

    private void deleteUser() {
        System.out.println("\n===== Delete User =====");
        String id = getStringInput("Enter User ID to delete: ");
        User user = libraryService.getUserById(id);

        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        if (!user.getBorrowedBookIds().isEmpty()) {
            System.out.println("This user has borrowed books. Cannot delete.");
            return;
        }

        System.out.println("User details: " + user);
        String confirm = getStringInput("Are you sure you want to delete this user? (y/n): ");

        if (confirm.equalsIgnoreCase("y")) {
            if (libraryService.deleteUser(id)) {
                System.out.println("User deleted successfully!");
            } else {
                System.out.println("Failed to delete user.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    // Borrow/Return Management
    private void handleBorrowReturnMenu() {
        boolean running = true;
        while (running) {
            displayBorrowReturnMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    borrowBook();
                    break;
                case 2:
                    returnBook();
                    break;
                case 3:
                    viewUserBorrowings();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void displayBorrowReturnMenu() {
        System.out.println("\n===== Borrow/Return Management =====");
        System.out.println("1. Borrow a Book");
        System.out.println("2. Return a Book");
        System.out.println("3. View User Borrowings");
        System.out.println("0. Back to Main Menu");
    }

    private void borrowBook() {
        System.out.println("\n===== Borrow a Book =====");
        String userId = getStringInput("Enter User ID: ");
        User user = libraryService.getUserById(userId);

        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        String bookId = getStringInput("Enter Book ID: ");
        Book book = libraryService.getBookById(bookId);

        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        if (!book.isAvailable()) {
            System.out.println("Sorry, this book is currently unavailable.");
            return;
        }

        if (libraryService.borrowBook(userId, bookId)) {
            System.out.println("Book borrowed successfully!");
        } else {
            System.out.println("Failed to borrow book.");
        }
    }

    private void returnBook() {
        System.out.println("\n===== Return a Book =====");
        String userId = getStringInput("Enter User ID: ");
        User user = libraryService.getUserById(userId);

        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        List<Book> borrowedBooks = libraryService.getBorrowedBooks(userId);
        if (borrowedBooks.isEmpty()) {
            System.out.println("This user has no borrowed books.");
            return;
        }

        System.out.println("\nBorrowed Books:");
        for (Book book : borrowedBooks) {
            System.out.println("- " + book.getTitle() + " by " + book.getAuthor() + " (ID: " + book.getId() + ")");
        }

        String bookId = getStringInput("Enter Book ID to return: ");
        if (libraryService.returnBook(userId, bookId)) {
            System.out.println("Book returned successfully!");
        } else {
            System.out.println("Failed to return book. Please check if the user has borrowed this book.");
        }
    }

    private void viewUserBorrowings() {
        System.out.println("\n===== View User Borrowings =====");
        String userId = getStringInput("Enter User ID: ");
        User user = libraryService.getUserById(userId);

        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        List<Book> borrowedBooks = libraryService.getBorrowedBooks(userId);
        if (borrowedBooks.isEmpty()) {
            System.out.println("This user has no borrowed books.");
            return;
        }

        System.out.println("\nBorrowed Books for " + user.getName() + ":");
        System.out.printf("%-5s %-30s %-20s %-15s%n", "ID", "Title", "Author", "Genre");
        System.out.println("--------------------------------------------------------------------");
        for (Book book : borrowedBooks) {
            System.out.printf("%-5s %-30s %-20s %-15s%n",
                    book.getId(),
                    truncateString(book.getTitle(), 30),
                    truncateString(book.getAuthor(), 20),
                    truncateString(book.getGenre(), 15));
        }
    }

    // Helper methods
    private int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private String getStringInputWithDefault(String fieldName, String defaultValue) {
        System.out.print("Enter new " + fieldName + " (current: " + defaultValue + "): ");
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? defaultValue : input;
    }

    private LocalDate getDateInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return LocalDate.parse(scanner.nextLine().trim());
            } catch (DateTimeParseException e) {
                System.out.println("Please enter a valid date in the format YYYY-MM-DD.");
            }
        }
    }

    private String truncateString(String str, int maxLength) {
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }
}
