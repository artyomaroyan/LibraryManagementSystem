package org.library.management.application.service;

import org.library.management.application.LoanService;
import org.library.management.model.Book;
import org.library.management.model.Loan;
import org.library.management.model.Member;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * Author: Artyom Aroyan
 * Date: 21.10.25
 * Time: 23:11:18
 */
public class LoanServiceImpl implements LoanService {
    private final Map<String, Loan> loans;
    private final Map<String, Book> books;
    private final Map<String, Member> members;
    private final ServiceHelper serviceHelper;

    public LoanServiceImpl(ServiceHelper serviceHelper) {
        this.serviceHelper = serviceHelper;
        this.loans = serviceHelper.getLoans();
        this.books = serviceHelper.getBooks();
        this.members = serviceHelper.getMembers();
    }

    @Override
    public boolean createLoan(UUID memberId, UUID bookId) {
        try {
            String bookKey = serviceHelper.uuidToString(bookId);
            String memberKey = serviceHelper.uuidToString(memberId);

            if (!members.containsKey(memberKey)) {
                IO.println("Member not found:");
                return false;
            }

            Book book = books.get(bookKey);
            if (book == null) {
                IO.println("Book not found");
                return false;
            }

            if (book.availableCopies() <= 0) {
                IO.println("Book is not available!");
                return false;
            }

            boolean hasExistingLoan = loans.values().stream()
                    .anyMatch(loan ->
                            loan.memberId().equals(memberId) &&
                                    loan.bookId().equals(bookId) &&
                                    !loan.returned());

            if (hasExistingLoan) {
                IO.println("Member already has this book on loan!");
                return false;
            }

            UUID id = UUID.randomUUID();
            Instant now = Instant.now();
            Instant dueDate = now.plus(Duration.ofDays(14));

            Loan loan = new Loan(id, bookId, memberId, now, dueDate, false);
            loans.put(serviceHelper.uuidToString(id), loan);

            Book updated = book.withAvailableCopies(book.availableCopies() - 1);
            books.put(bookKey, updated);

            serviceHelper.saveData();
            IO.println("Loan created successfully. Due date: " + dueDate);
            return true;
        } catch (Exception ex) {
            IO.println("Error creating loan: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean returnLoan(UUID loanId) {
        String loanKey = serviceHelper.uuidToString(loanId);
        Loan loan = loans.get(loanKey);
        if (loan == null || loan.returned()) {
            return false;
        }
        Loan updatedLoan = loan.withReturned(true);
        loans.put(loanKey, updatedLoan);

        String bookKey = serviceHelper.uuidToString(loan.bookId());
        Book book = books.get(bookKey);
        if (book != null) {
            Book updatedBook = book.withAvailableCopies(book.availableCopies() + 1);
            books.put(bookKey, updatedBook);
        }
        serviceHelper.saveData();
        return true;
    }
/*
    @Override
    public boolean existsById(UUID loanId) {
        return loans.containsKey(serviceHelper.uuidToString(loanId));
    }
*/
    @Override
    public List<Loan> getAllLoans() {
        return new ArrayList<>(loans.values());
    }

    @Override
    public List<Loan> getOverdueLoans() {
        Instant now = Instant.now();
        return loans.values().stream()
                .filter(loan -> !loan.returned() && loan.dueDate().isBefore(now))
                .toList();
    }
}