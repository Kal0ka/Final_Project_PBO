package com.last.Action;

import com.last.Data.Book;
import com.last.Data.Transaction;
import com.last.Database.CSVService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class BookManager {

    private static final String BOOKS_FILE = "Dbase/books.csv";
    private static final String TX_FILE = "Dbase/transactions.csv";
    private static final String SAVE_TX_FILE = "Dbase/transactions.csv";
    private static final String SAVE_BOOKS_FILE = "Dbase/books.csv";

    public static List<Book> getPreviewBooks(int limit) {
        try {
            return CSVService.loadBooks(BOOKS_FILE).stream()
                    .limit(limit)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static List<Book> searchBooks(String keyword) {
        try {
            return CSVService.loadBooks(BOOKS_FILE).stream()
                    .filter(b -> b.getTitle().toLowerCase().contains(keyword.toLowerCase())
                            || b.getCategories().toLowerCase().contains(keyword.toLowerCase()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static boolean borrowBook(String isbn, int userId) {
        try {
            List<Transaction> txs = CSVService.loadTransactions(TX_FILE);
            List<Book> books = CSVService.loadBooks(BOOKS_FILE);
            LocalDate today = LocalDate.now();

            boolean alreadyBorrowed = txs.stream().anyMatch(t ->
                    t.getIsbn().equals(isbn)
                            && t.getUserId() == userId
                            && !t.getReturnDate().isBefore(today)
            );

            if (alreadyBorrowed) return false;

            for (Book b : books) {
                if (b.getIsbn().equals(isbn)) {
                    if (b.getStock() > 0) {
                        b.setStock(b.getStock() - 1);

                        Transaction tx = new Transaction(
                                txs.size() + 1,
                                isbn,
                                userId,
                                today,
                                today.plusDays(7),
                                false,
                                0 // default fine
                        );

                        txs.add(tx);

                        CSVService.saveBooks(SAVE_BOOKS_FILE, books);
                        CSVService.saveTransactions(SAVE_TX_FILE, txs);
                        return true;
                    } else {
                        return false;
                    }
                }
            }

            return false;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Transaction returnBook(String isbn, int userId) {
        try {
            List<Transaction> txs = CSVService.loadTransactions(TX_FILE);
            List<Book> books = CSVService.loadBooks(BOOKS_FILE);

            Optional<Transaction> txToReturn = txs.stream()
                    .filter(t -> t.getIsbn().equals(isbn) && t.getUserId() == userId && !t.isReturned())
                    .findFirst();

            if (txToReturn.isPresent()) {
                Transaction tx = txToReturn.get();

                LocalDate today = LocalDate.now();
                if (today.isAfter(tx.getReturnDate())) {
                    long overdueDays = ChronoUnit.DAYS.between(tx.getReturnDate(), today);
                    int fine = (int) (overdueDays * 1000);
                    tx.setFine(fine);
                }

                tx.setReturned(true); // ✅ tandai sudah dikembalikan

                for (Book b : books) {
                    if (b.getIsbn().equals(isbn)) {
                        b.setStock(b.getStock() + 1);
                        break;
                    }
                }

                CSVService.saveTransactions(SAVE_TX_FILE, txs);
                CSVService.saveBooks(SAVE_BOOKS_FILE, books);

                return tx; // ✅ return seluruh objek transaksi
            } else {
                return null; // ❌ transaksi tidak ditemukan
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }




    public static List<Transaction> getUserTransactions(int userId) {
        try {
            return CSVService.loadTransactions(TX_FILE).stream()
                    .filter(t -> t.getUserId() == userId && !t.isReturned()) // hanya yang belum dikembalikan
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static boolean extendLoan(String isbn, int userId) {
        try {
            List<Transaction> txs = CSVService.loadTransactions(TX_FILE);
            boolean updated = false;

            for (Transaction t : txs) {
                if (t.getIsbn().equals(isbn) && t.getUserId() == userId && !t.isExtended()) {
                    t.setReturnDate(t.getReturnDate().plusDays(7));
                    t.setExtended(true);
                    updated = true;
                    break;
                }
            }

            if (updated) {
                CSVService.saveTransactions(SAVE_TX_FILE, txs);
            }

            return updated;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
