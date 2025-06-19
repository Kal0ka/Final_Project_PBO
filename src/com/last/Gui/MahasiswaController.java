package com.last.Gui;

import com.last.Action.BookManager;
import com.last.Data.Book;
import com.last.Data.Transaction;
import com.last.Database.CSVService;
import com.last.User.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MahasiswaController {

    @FXML private TableView<Book> bookTable;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> categoryColumn;
    @FXML private TableColumn<Book, Integer> stockColumn;

    @FXML private TextField searchField;

    @FXML private TableView<Transaction> borrowedTable;
    @FXML private TableColumn<Transaction, String> borrowedIsbnColumn;
    @FXML private TableColumn<Transaction, String> borrowedTitleColumn;
    @FXML private TableColumn<Transaction, String> dueDateColumn;

    @FXML private Button returnButton;

    @FXML private Button searchButton;
    @FXML private Button borrowButton;
    @FXML private Button extendButton;
    @FXML private Button logoutButton;

    private ObservableList<Book> booksData = FXCollections.observableArrayList();
    private ObservableList<Transaction> borrowedData = FXCollections.observableArrayList();
    private Map<String, String> isbnToTitleMap = new HashMap<>();


    @FXML
    public void initialize() {
        // Inisialisasi kolom buku
        titleColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getTitle()));
        categoryColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getCategories()));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        // Inisialisasi kolom peminjaman
        borrowedTitleColumn.setCellValueFactory(cell -> {
            String isbn = cell.getValue().getIsbn();
            Book book = findBookByIsbn(isbn);
            String title = (book != null) ? book.getTitle() : "(Judul tidak ditemukan)";
            return new javafx.beans.property.SimpleStringProperty(title);
        });

        borrowedIsbnColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getIsbn()));
        dueDateColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getReturnDate().toString()));

        loadBooks();
        loadBorrowed();
    }


    private void loadBooks() {
        List<Book> books = BookManager.getPreviewBooks(1000);
        booksData.setAll(books);
        bookTable.setItems(booksData);

        // Isi ulang mapping ISBN ke Judul
        isbnToTitleMap.clear();
        for (Book book : books) {
            isbnToTitleMap.put(book.getIsbn(), book.getTitle());
        }
    }


    private void loadBorrowed() {
        List<Transaction> tx = BookManager.getUserTransactions(SessionManager.getCurrentUser().getId());
        borrowedData.setAll(tx);
        borrowedTable.setItems(borrowedData);
    }

    @FXML
    public void onSearch() {
        String keyword = searchField.getText().trim();
        if (!keyword.isEmpty()) {
            List<Book> result = BookManager.searchBooks(keyword);
            booksData.setAll(result);
        }
    }

    @FXML
    public void onBorrow() {
        Book selected = bookTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            boolean success = BookManager.borrowBook(selected.getIsbn(), SessionManager.getCurrentUser().getId());
            if (success) {
                showAlert("Berhasil", "Buku berhasil dipinjam");
                loadBorrowed();
                loadBooks(); // Tambahkan ini agar stock buku terupdate di UI
            } else {
                showAlert("Gagal", "Buku sudah dipinjam atau terjadi kesalahan");
            }
        }
    }

    @FXML
    public void onExtend() {
        Transaction selected = borrowedTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            boolean success = BookManager.extendLoan(selected.getIsbn(), SessionManager.getCurrentUser().getId());
            if (success) {
                showAlert("Perpanjangan", "Masa pinjam diperpanjang");
                loadBorrowed();
            } else {
                showAlert("Gagal", "Sudah pernah diperpanjang atau kesalahan lainnya");
            }
        }
    }

    @FXML
    public void onLogout() {
        SessionManager.logout();
        try {
            // Ambil stage saat ini dari komponen mana pun (misalnya tombol logout)
            javafx.stage.Stage stage = (javafx.stage.Stage) logoutButton.getScene().getWindow();

            // Load ulang login.fxml
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/com/last/Fxml/login.fxml"));
            javafx.scene.Scene scene = new javafx.scene.Scene(loader.load());

            // Set dan tampilkan scene login
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.showAndWait();
    }
    @FXML
    public void onReturn() {
        Transaction selected = borrowedTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Transaction returnedTx = BookManager.returnBook(selected.getIsbn(), SessionManager.getCurrentUser().getId());
            if (returnedTx != null) {
                StringBuilder message = new StringBuilder("Buku berhasil dikembalikan.");

                if (returnedTx.getFine() > 0) {
                    long overdueDays = returnedTx.getFine() / 1000;
                    message.append("\n\n⚠️ Terlambat: ").append(overdueDays).append(" hari");
                    message.append("\n💸 Denda: Rp").append(returnedTx.getFine());
                }

                showAlert("Pengembalian", message.toString());
                loadBorrowed();
                loadBooks();
            } else {
                showAlert("Gagal", "Pengembalian gagal");
            }

        }
    }

    private Book findBookByIsbn(String isbn) {
        for (Book book : bookTable.getItems()) {
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }
        return null;
    }

}
