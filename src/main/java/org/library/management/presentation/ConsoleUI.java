package org.library.management.presentation;

import org.library.management.application.BookService;
import org.library.management.application.LoanService;
import org.library.management.application.MemberService;

import java.util.Scanner;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 20.10.25
 * Time: 22:20:28
 */
public class ConsoleUI {
    private final BookService bookService;
    private final LoanService loanService;
    private final MemberService memberService;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleUI(BookService bookService, LoanService loanService, MemberService memberService) {
        this.bookService = bookService;
        this.loanService = loanService;
        this.memberService = memberService;
    }

    public void start() {
        boolean running = true;
        while (running) {
            IO.println("\\n=== Library Management System ===");
            IO.println("1. Manage Books");
            IO.println("2. Manage Members");
            IO.println("3. Manage Loans");
            IO.println("0. Exit");
            IO.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> manageBooks();
                case "2" -> manageMembers();
                case "3" -> manageLoans();
                case "0" -> running = false;
                default -> IO.println("Invalid choice. Try again!");
            }
        }
    }

    private void manageBooks() {
        while (true) {
            IO.println("\\n--- Books ---");
            IO.println("1. Add new book");
            IO.println("2. List of books");
            IO.println("3. Find book by title");
            IO.println("4. Delete book by id");
            IO.print("0. Back");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> {
                    IO.print("Title: ");
                    String title = scanner.nextLine();
                    IO.print("Author: ");
                    String author = scanner.nextLine();
                    IO.print("Year: ");
                    int year = Integer.parseInt(scanner.nextLine());
                    IO.print("Total copies: ");
                    int totalCopies = Integer.parseInt(scanner.nextLine());
                    String result = bookService.addBook(title, author, year, totalCopies);
                    IO.println(result);
                }
                case "2" -> bookService.listOfBooks().forEach(book -> IO.println(book.toString()));
                case "3" -> {
                    IO.print("Search by title: ");
                    String title = scanner.nextLine();
                    bookService.findBookByTitle(title).ifPresentOrElse(
                            book -> IO.println(book.toString()),
                            () -> IO.println("Book not found"));
                }
                case "4" -> {
                    IO.print("Delete by ID: ");
                    UUID id = UUID.fromString(scanner.nextLine());
                    boolean deleted = bookService.deleteBookById(id);
                    IO.println(deleted ? "Deleted" : "Not found / could not be delete");
                }
                case "0" -> {
                    return;
                }
                default -> IO.println("Invalid");
            }
        }
    }

    private void manageMembers() {

    }

    private void manageLoans() {
        // implement this method
    }
}