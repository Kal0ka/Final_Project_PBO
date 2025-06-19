package com.last.Menu;

import com.last.Action.BookManager;
import com.last.Data.Book;
import com.last.Data.Transaction;
import com.last.User.SessionManager;

import java.util.List;
import java.util.Scanner;

public class MahasiswaMenu {
    public static void show() {
        Scanner scanner = new Scanner(System.in);
        String userId = String.valueOf(SessionManager.getCurrentUser().getId());

        while (true) {
            System.out.println("\n=== Menu Mahasiswa ===");
            System.out.println("1. Lihat Daftar Buku");
            System.out.println("2. Cari Buku");
            System.out.println("3. Pinjam Buku");
            System.out.println("4. Buku yang Sedang Dipinjam");
            System.out.println("5. Perpanjang Peminjaman");
            System.out.println("0. Keluar");
            System.out.print("Pilih opsi: ");

            int opsi = Integer.parseInt(scanner.nextLine());
            switch (opsi) {
                case 1:
                    List<Book> previewBooks = BookManager.getPreviewBooks(5);
                    previewBooks.forEach(MahasiswaMenu::printBook);
                    break;

                case 2:
                    System.out.print("Masukkan kata kunci (judul/kategori): ");
                    String keyword = scanner.nextLine();
                    List<Book> foundBooks = BookManager.searchBooks(keyword);
                    foundBooks.forEach(MahasiswaMenu::printBook);
                    break;

                case 3:
                    System.out.print("Masukkan ISBN buku yang ingin dipinjam: ");
                    int isbnPinjam = Integer.parseInt(scanner.nextLine());
                    boolean success = BookManager.borrowBook(String.valueOf(userId), isbnPinjam);
                    if (success) {
                        System.out.println("Buku berhasil dipinjam.");
                    } else {
                        System.out.println("Peminjaman gagal. Buku tidak ditemukan atau sudah dipinjam.");
                    }
                    break;

                case 4:
                    List<Transaction> transactions = BookManager.getUserTransactions(Integer.parseInt(userId));
                    if (transactions.isEmpty()) {
                        System.out.println("Tidak ada buku yang sedang dipinjam.");
                    } else {
                        for (Transaction t : transactions) {
                            System.out.printf("ISBN: %s | Tanggal Pinjam: %s | Tenggat: %s%s\n",
                                    t.getIsbn(), t.getBorrowDate(), t.getReturnDate(),
                                    t.getReturnDate().minusDays(2).isBefore(java.time.LocalDate.now()) ? " [!! MENDEKATI TENGGAT]" : "");
                        }
                    }
                    break;

                case 5:
                    System.out.print("Masukkan ISBN buku yang ingin diperpanjang: ");
                    int isbnPerpanjang = Integer.parseInt(scanner.nextLine());
                    boolean extended = BookManager.extendLoan(userId, isbnPerpanjang);
                    if (extended) {
                        System.out.println("Peminjaman diperpanjang 7 hari.");
                    } else {
                        System.out.println("Perpanjangan gagal. Buku mungkin sudah diperpanjang sebelumnya atau tidak dipinjam.");
                    }
                    break;

                case 0:
                    System.out.println("Keluar dari menu mahasiswa.");
                    return;

                default:
                    System.out.println("Opsi tidak dikenali.");
            }
        }
    }

    private static void printBook(Book book) {
        System.out.printf("ISBN: %s | Judul: %s | Penulis: %s | Kategori: %s\n",
                book.getIsbn(), book.getTitle(), book.getAuthors(), book.getCategories());
    }
}
