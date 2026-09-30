package com.library.ui;

import static com.library.util.InputValidator.*;
import com.library.model.Book;
import com.library.service.BookService;
import java.util.List;

public final class BookMenu {
    private BookMenu() {}
    public static void manage() {
        while (true) {
            System.out.println("\n-- Books --\n1. List all\n2. Search\n3. Add book\n4. Add category\n5. Delete book\n0. Back");
            int c = integer("Choose: ");
            if (c == 0) return;
            attempt(() -> {
                switch (c) {
                    case 1 -> print(BookService.all());
                    case 2 -> search();
                    case 3 -> add();
                    case 4 -> { BookService.addCategory(text("Category name: ")); System.out.println("Category added."); }
                    case 5 -> { BookService.delete(integer("Book id: ")); System.out.println("Book deleted."); }
                    default -> System.out.println("Invalid choice.");
                }
            });
        }
    }
    public static void search() throws Exception { print(BookService.search(text("Title or author: "))); }
    private static void add() throws Exception {
        BookService.categories().forEach(k -> System.out.println("  " + k.id() + ". " + k.name()));
        BookService.add(text("Title: "), text("Author: "), integer("Category id: "), integer("Copies: "));
        System.out.println("Book added.");
    }
    static void print(List<Book> books) {
        if (books.isEmpty()) System.out.println("No books found.");
        books.forEach(b -> System.out.printf("#%d | %s | %s | %d/%d available%n", b.id(), b.title(), b.author(), b.availableCopies(), b.totalCopies()));
    }
}
