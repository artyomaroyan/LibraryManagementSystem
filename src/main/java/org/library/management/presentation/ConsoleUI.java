package org.library.management.presentation;

import org.library.management.application.BookService;
import org.library.management.application.LoanService;
import org.library.management.application.MemberService;
import org.library.management.model.Book;
import org.library.management.model.Loan;
import org.library.management.model.Member;

import java.time.Year;
import java.util.Collection;
import java.util.Objects;
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
            IO.println("\n--- Books Management ---");
            IO.println("1. Add new book");
            IO.println("2. List all books");
            IO.println("3. Find book by title");
            IO.println("4. Delete book by ID");
            IO.println("0. Back to main menu");
            IO.print("Enter your choose: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> addBook();
                case "2" -> listBooks();
                case "3" -> findByTitle();
                case "4" -> deleteBook();
                case "0" -> {
                    return;
                }
                default -> IO.println("Invalid choice. Please try again");
            }
        }
    }

    private void addBook() {
        try {
            String title = readNonEmptyString("Title: ");
            String author = readNonEmptyString("Author: ");
            int year = readInt("Publication year: ");
            int totalCopies = readInt("Total copies: ");

            if (year < 0 || year > Year.now().getValue()) {
                IO.println("Invalid publication year.");
                return;
            }

            if (totalCopies <= 0) {
                IO.println("Total copies must be positive");
                return;
            }

            String result = bookService.addBook(title, author, year, totalCopies);
            IO.println("Added: " + result);
        } catch (Exception ex) {
            IO.println("Error adding book: " + ex.getMessage());
        }
    }

    private void listBooks() {
        Collection<Book> books = bookService.listOfBooks();
        if (books.isEmpty()) {
            IO.println("No books found in the library.");
        } else {
            IO.println("\n--- All books ---");
            books.forEach(book -> IO.println(book.toString()));
        }
    }

    private void findByTitle() {
        String title = readNonEmptyString("Enter book title to search: ");
        bookService.findBookByTitle(title).ifPresentOrElse(book -> {
                    IO.println("Book found:");
                    IO.println(book.toString());
                },
                () -> IO.println("Book not found: "));
    }

    private void deleteBook() {
        IO.println("Current book: ");
        bookService.listOfBooks().forEach(book ->
                IO.println("ID: " + book.id() + " - " + book.title()));
        UUID id = readUUID("Enter book ID to delete: ");
        if (id == null) return;

        boolean deleted = bookService.deleteBookById(id);
        IO.println(deleted ? "Book deleted successfully" : "Book not found or could not be deleted");
    }

    private void manageMembers() {
        while (true) {
            IO.println("\n--- Members Management ---");
            IO.println("1. Add new member");
            IO.println("2. List of members");
            IO.println("3. Find member by name");
            IO.println("4. Delete member by ID");
            IO.println("0. Back to main menu");
            IO.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> addMember();
                case "2" -> listMembers();
                case "3" -> findByName();
                case "4" -> deleteMember();
                case "0" -> {
                    return;
                }
                default -> IO.println("Invalid choice. Please try again");
            }
        }
    }

    private void addMember() {
        try {
            String name = readNonEmptyString("Name: ");
            String email = readNonEmptyString("Email: ");

            memberService.findMemberByName(name).ifPresent(m -> {
                if (Objects.equals(m.name(), name) || Objects.equals(m.email(), email)) {
                    IO.println("You already have an account: ");
                }
            });

            String result = memberService.addMember(name, email);
            IO.println("Added: " + result);
        } catch (Exception ex) {
            IO.println("Error adding member: " + ex.getMessage());
        }
    }

    private void listMembers() {
        Collection<Member> members = memberService.listOfMembers();
        if (members.isEmpty()) {
            IO.println("No members found. ");
        } else {
            IO.println("\n--- All members ---");
            members.forEach(member -> IO.println(member.toString()));
        }
    }

    private void findByName() {
        String name = readNonEmptyString("Enter member name to search: ");
        memberService.findMemberByName(name).ifPresentOrElse(member -> {
                    IO.println("Member found: ");
                    IO.println(member.toString());
                },
                () -> IO.println("Member not found: "));
    }

    private void deleteMember() {
        IO.println("Current member: ");
        memberService.listOfMembers().forEach(member ->
                IO.println("ID: " + member.id() + " - " + member.name()));
        UUID id = readUUID("Enter member ID to delete: ");
        if (id == null) return;

        boolean deleted = bookService.deleteBookById(id);
        IO.println(deleted ? "Member deleted successfully" : "Member not found or could not be deleted");
    }

    private void manageLoans() {
        while (true) {
            IO.println("\n--- Loans ---");
            IO.println("1. Crate new loan");
            IO.println("2. Return loan");
            IO.println("3. Check overdue loans");
            IO.println("0. Back to main menu");
            IO.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> createLoan();
                case "2" -> returnLoan();
                case "3" -> overdueLoans();
                case "0" -> {
                    return;
                }
                default -> IO.println("Invalid choice. Please try again");
            }
        }
    }

    private void createLoan() {
        try {
            UUID bookId = readUUID("Book ID: ");
            UUID memberId = readUUID("Member ID: ");

            if (!bookService.existsById(bookId) || !memberService.existsById(memberId)) {
                IO.println("Error creating loan. Book or member does not exist. ");
            } else {
                boolean result = loanService.createLoan(memberId, bookId);
                IO.println(result ? "Loan created" : "Failed to create loan");
            }
        } catch (Exception ex) {
            IO.println("Error creating loan: " + ex.getMessage());
        }
    }

    private void returnLoan() {
        try {
            UUID loanId = readUUID("LoanId: ");

            if (!loanService.existsById(loanId)) {
                IO.println("Loan does not found. Please try again. ");
            } else {
                boolean result = loanService.returnLoan(loanId);
                IO.println(result ? "Loan returned" : "Failed to return laon");
            }
        } catch (Exception ex) {
            IO.println("Failed to return loan: " + ex.getMessage());
        }
    }

    private void overdueLoans() {
        Collection<Loan> loans = loanService.getOverdueLoans();
        if (loans.isEmpty()) {
            IO.println("No overdue loans found. ");
        } else {
            IO.println("\n--- All overdue loans ---");
            loans.forEach(loan -> IO.println(loan.toString()));
        }
    }

    private int readInt(String str) {
        while (true) {
            try {
                IO.print(str);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException ex) {
                IO.println("Invalid number: Please try again.");
            }
        }
    }

    private UUID readUUID(String str) {
        while (true) {
            try {
                IO.print(str);
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    return null;
                }
                return UUID.fromString(input);
            } catch (IllegalArgumentException ex) {
                IO.println("Invalid UUID format. Please try again.");
            }
        }
    }

    private String readNonEmptyString(String str) {
        while (true) {
            IO.print(str);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            IO.println("This filed can not be empty. Please try again.");
        }
    }
}