package repository;

import model.User;

import java.io.*;
import java.util.*;

public class UserRepository {
    private Map<String, User> users;
    private final String USERS_FILE = "users.csv";

    public UserRepository() {
        users = new HashMap<>();
        loadUsers();
    }

    public void addUser(User user) {
        users.put(user.getId(), user);
        saveUsers();
    }

    public User getUserById(String id) {
        return users.get(id);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public void updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            saveUsers();
        }
    }

    public boolean deleteUser(String id) {
        User removed = users.remove(id);
        if (removed != null) {
            saveUsers();
            return true;
        }
        return false;
    }

    private void loadUsers() {
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            // Create sample data
            createSampleUsers();
            saveUsers();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 4); // Limit to 4 parts
                String id = parts[0];
                String name = parts[1];
                String email = parts[2];

                User user = new User(id, name, email);
                
                // Handle borrowed books if they exist
                if (parts.length > 3 && !parts[3].isEmpty()) {
                    String[] bookIds = parts[3].split(";");
                    for (String bookId : bookIds) {
                        if (!bookId.isEmpty()) {
                            user.borrowBook(bookId);
                        }
                    }
                }
                
                users.put(id, user);
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }

    private void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (User user : users.values()) {
                writer.write(user.toCsvString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    private void createSampleUsers() {
        addUser(new User("U001", "John Doe", "john@example.com"));
        addUser(new User("U002", "Jane Smith", "jane@example.com"));
        addUser(new User("U003", "Bob Johnson", "bob@example.com"));
    }
}

