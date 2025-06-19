package com.last.Data;

import java.time.LocalDate;

public class Transaction {
    private int id;
    private String isbn;
    private int userId;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private boolean extended;
    private int fine;
    private boolean returned;

    public Transaction(int id, String isbn, int userId, LocalDate borrowDate, LocalDate returnDate, boolean extended) {
        this(id, isbn, userId, borrowDate, returnDate, extended, 0, false);
    }

    public Transaction(int id, String isbn, int userId, LocalDate borrowDate, LocalDate returnDate, boolean extended, int fine) {
        this(id, isbn, userId, borrowDate, returnDate, extended, fine, false);
    }

    public Transaction(int id, String isbn, int userId, LocalDate borrowDate, LocalDate returnDate, boolean extended, int fine, boolean returned) {
        this.id = id;
        this.isbn = isbn;
        this.userId = userId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.extended = extended;
        this.fine = fine;
        this.returned = returned;
    }

    public int getId() { return id; }
    public String getIsbn() { return isbn; }
    public int getUserId() { return userId; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public boolean isExtended() { return extended; }
    public int getFine() { return fine; }
    public boolean isReturned() { return returned; }

    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public void setExtended(boolean extended) { this.extended = extended; }
    public void setFine(int fine) { this.fine = fine; }
    public void setReturned(boolean returned) { this.returned = returned; }

    public String toCSVString() {
        return id + "," + isbn + "," + userId + "," + borrowDate + "," + returnDate + "," + extended + "," + fine + "," + returned;
    }
}
