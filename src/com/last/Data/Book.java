package com.last.Data;

public class Book {
    private String isbn;
    private String title;
    private String authors;
    private String publisher;
    private String categories;
    private String publishedDate;
    private int stock;

    // Konstruktor lama (tanpa stock), default stock = 0
    public Book(String isbn, String title, String authors, String publisher, String categories, String publishedDate) {
        this(isbn, title, authors, publisher, categories, publishedDate, 0);
    }

    // Konstruktor lengkap (dengan stock)
    public Book(String isbn, String title, String authors, String publisher, String categories, String publishedDate, int stock) {
        this.isbn = isbn;
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.categories = categories;
        this.publishedDate = publishedDate;
        this.stock = stock;
    }

    // Getter
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthors() { return authors; }
    public String getPublisher() { return publisher; }
    public String getCategories() { return categories; }
    public String getPublishedDate() { return publishedDate; }
    public int getStock() { return stock; }

    // Setter
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthors(String authors) { this.authors = authors; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public void setCategories(String categories) { this.categories = categories; }
    public void setPublishedDate(String publishedDate) { this.publishedDate = publishedDate; }
    public void setStock(int stock) { this.stock = stock; }

    public String toCSVString() {
        return String.join(",",
                isbn, title, authors, publisher, categories, publishedDate, String.valueOf(stock)
        );
    }

    private String quote(String field) {
        if (field.contains(",") || field.contains("\"")) {
            field = field.replace("\"", "\"\""); // escape tanda kutip
            return "\"" + field + "\"";
        }
        return field;
    }
}
