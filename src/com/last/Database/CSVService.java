package com.last.Database;

import com.last.Data.Book;
import com.last.Data.Transaction;
import com.last.Data.User;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class CSVService {

    public static String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    sb.append('"'); // double quote escape
                    i++; // skip next
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                result.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        result.add(sb.toString()); // add last

        return result.toArray(new String[0]);
    }


    public static List<User> loadUsers(String path) throws IOException {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = parseCSVLine(line);
                if (parts.length >= 6) {
                    users.add(new User(
                            parts[0],
                            parts[1],
                            parts[2],
                            parts[3],
                            parts[4],
                            parts[5]
                    ));
                }
            }
        }
        return users;
    }

    public static void saveUsers(String path, List<User> users) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write("id,name,email,password,role_id,nim_nip\n");
            for (User u : users) {
                writer.write(u.toCSVString() + "\n");
            }
        }
    }

    public static List<Book> loadBooks(String path) throws IOException {
        List<Book> books = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = parseCSVLine(line);

                if (parts.length >= 6) {
                    String isbn = parts[0];
                    String title = parts[1];
                    String authors = parts[2];
                    String publisher = parts[3];
                    String categories = parts[4];
                    String publishedDate = parts[5];
                    int stock = 0;

                    if (parts.length >= 7) {
                        String stockStr = parts[6].trim();
                        if (!stockStr.isEmpty()) {
                            try {
                                stock = Integer.parseInt(stockStr);
                            } catch (NumberFormatException e) {
                                System.err.println("⚠️ Invalid stock value on line: " + Arrays.toString(parts));
                                stock = 0; // fallback
                            }
                        }
                    }

                    books.add(new Book(isbn, title, authors, publisher, categories, publishedDate, stock));
                } else {
                    System.err.println("⚠️ Invalid book line (too short): " + Arrays.toString(parts));
                }
            }
        }
        return books;
    }

    public static void saveBooks(String path, List<Book> books) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write("ISBN,Title,Authors,Publisher,Categories,PublishedDate,Stock\n");
            for (Book b : books) {
                writer.write(b.toCSVString() + "\n");
            }
        }
    }

    public static List<Transaction> loadTransactions(String path) throws IOException {
        List<Transaction> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // ⛔️ Skip baris kosong
                String[] parts = parseCSVLine(line);
                try {
                    int fine = (parts.length >= 7) ? Integer.parseInt(parts[6]) : 0;
                    boolean returned = (parts.length >= 8) ? Boolean.parseBoolean(parts[7]) : false;

                    list.add(new Transaction(
                            Integer.parseInt(parts[0]),
                            parts[1],
                            Integer.parseInt(parts[2]),
                            LocalDate.parse(parts[3]),
                            LocalDate.parse(parts[4]),
                            Boolean.parseBoolean(parts[5]),
                            fine,
                            returned
                    ));
                } catch (Exception e) {
                    System.err.println("⚠️ Invalid transaction line: " + Arrays.toString(parts));
                }
            }
        }
        return list;
    }

    public static void saveTransactions(String path, List<Transaction> txs) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write("id,isbn,user_id,borrow_date,return_date,extended,fine,returned\n");
            for (Transaction t : txs) {
                writer.write(t.toCSVString() + "\n");
            }
        }
    }
}
