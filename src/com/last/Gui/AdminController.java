package com.last.Gui;

import com.last.Data.*;
import com.last.Database.CSVService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class AdminController {

    private static final String BOOKS_FILE = "Dbase/books.csv";
    private static final String USERS_FILE = "Dbase/users.csv";
    private static final String TX_FILE = "Dbase/transactions.csv";
    private static final String SAVE_BOOKS_FILE = BOOKS_FILE;
    private static final String SAVE_USERS_FILE = USERS_FILE;

    // Buku
    @FXML private TableView<Book> bookTable;
    @FXML private TableColumn<Book, String> titleCol, categoryCol, authorCol, publisherCol, isbnCol;
    @FXML private TableColumn<Book, String> publishedDateCol;
    @FXML private TextField titleField, categoryField, authorField, publisherField, isbnField, publishedDatefield, stockField;

    // Pengguna
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> nameCol, emailCol, nimNipCol;
    @FXML private TableColumn<User, Integer> idCol;
    @FXML private TableColumn<User, String> roleCol;
    @FXML private TextField nameField, emailField, idField, passwordField, nimNipField;
    @FXML private ComboBox<String> roleBox;

    // Transaksi
    @FXML private TableView<Transaction> transactionTable;
    @FXML private TableColumn<Transaction, String> borrowedIsbnCol, borrowerCol, borrowedTitleCol;
    @FXML private TableColumn<Transaction, LocalDate> dateCol, dueCol;
    @FXML private TableColumn<Transaction, Boolean> returnedCol;
    @FXML private TableColumn<Transaction, Integer> fineCol;

    private List<Book> books;
    private final Map<Integer, String> userMap = new HashMap<>();

    @FXML
    public void initialize() throws IOException {
        // Buku
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("authors"));
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("categories"));
        publishedDateCol.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));

        titleCol.setPrefWidth(150);
        categoryCol.setPrefWidth(100);
        authorCol.setPrefWidth(120);
        publisherCol.setPrefWidth(120);
        isbnCol.setPrefWidth(100);
        publishedDateCol.setPrefWidth(120);

        bookTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Pengguna
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        nimNipCol.setCellValueFactory(new PropertyValueFactory<>("nimNip"));
        roleCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getRole().getRoleName())
        );

        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        ObservableList<User> users = loadUsers();
        userTable.setItems(users);
        for (User user : users) {
            userMap.put(user.getId(), user.getName());
        }

        // Transaksi
        borrowerCol.setCellValueFactory(cellData -> {
            int userId = cellData.getValue().getUserId();
            String name = userMap.getOrDefault(userId, "Unknown");
            return new javafx.beans.property.SimpleStringProperty(name);
        });

        borrowedIsbnCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getIsbn())
        );

        borrowedTitleCol.setCellValueFactory(cellData -> {
            String isbn = cellData.getValue().getIsbn();
            Optional<Book> book = books.stream().filter(b -> b.getIsbn().equals(isbn)).findFirst();
            return new javafx.beans.property.SimpleStringProperty(book.map(Book::getTitle).orElse("Tidak ditemukan"));
        });

        dateCol.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        dueCol.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        returnedCol.setCellValueFactory(new PropertyValueFactory<>("returned"));
        fineCol.setCellValueFactory(new PropertyValueFactory<>("fine"));

        transactionTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        roleBox.setItems(FXCollections.observableArrayList("ADMIN", "MAHASISWA", "STAFF"));

        books = CSVService.loadBooks(BOOKS_FILE);
        bookTable.setItems(FXCollections.observableArrayList(books));
        transactionTable.setItems(loadTransactions());
    }

    @FXML
    public void onAddBook() {
        try {
            int stock = Integer.parseInt(stockField.getText());
            Book newBook = new Book(
                    isbnField.getText(),
                    titleField.getText(),
                    authorField.getText(),
                    publisherField.getText(),
                    categoryField.getText(),
                    publishedDatefield.getText(),
                    stock
            );

            ObservableList<Book> booksList = bookTable.getItems();
            booksList.add(newBook);
            CSVService.saveBooks(SAVE_BOOKS_FILE, booksList);
            bookTable.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onEditBook() {
        Book selected = bookTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setTitle(titleField.getText());
            selected.setCategories(categoryField.getText());
            selected.setAuthors(authorField.getText());
            selected.setPublisher(publisherField.getText());
            selected.setIsbn(isbnField.getText());
            selected.setPublishedDate(publishedDatefield.getText());

            try {
                CSVService.saveBooks(SAVE_BOOKS_FILE, bookTable.getItems());
                bookTable.refresh();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void onDeleteBook() {
        Book selected = bookTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            bookTable.getItems().remove(selected);
            try {
                CSVService.saveBooks(SAVE_BOOKS_FILE, bookTable.getItems());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void onAddUser() {
        try {
            List<User> currentUsers = CSVService.loadUsers(USERS_FILE);
            int nextId = currentUsers.stream().mapToInt(User::getId).max().orElse(0) + 1;

            Role role = Role.fromString(roleBox.getValue());

            User newUser = new User(
                    String.valueOf(nextId),
                    nameField.getText(),
                    emailField.getText(),
                    passwordField.getText(),
                    String.valueOf(role.getId()),
                    nimNipField.getText()
            );

            userTable.getItems().add(newUser);
            CSVService.saveUsers(SAVE_USERS_FILE, userTable.getItems());
            userTable.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onDeleteUser() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            userTable.getItems().remove(selected);
            try {
                CSVService.saveUsers(SAVE_USERS_FILE, userTable.getItems());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private ObservableList<User> loadUsers() {
        try {
            return FXCollections.observableArrayList(CSVService.loadUsers(USERS_FILE));
        } catch (IOException e) {
            e.printStackTrace();
            return FXCollections.observableArrayList();
        }
    }

    private ObservableList<Transaction> loadTransactions() {
        try {
            return FXCollections.observableArrayList(CSVService.loadTransactions(TX_FILE));
        } catch (IOException e) {
            e.printStackTrace();
            return FXCollections.observableArrayList();
        }
    }

    @FXML
    private void onLogout() {
        try {
            javafx.stage.Stage stage = (javafx.stage.Stage) bookTable.getScene().getWindow();
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/com/last/Fxml/login.fxml"));
            javafx.scene.Scene scene = new javafx.scene.Scene(loader.load());

            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}