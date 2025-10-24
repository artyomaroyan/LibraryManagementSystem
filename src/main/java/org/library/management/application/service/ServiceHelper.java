package org.library.management.application.service;

import org.library.management.model.Book;
import org.library.management.model.Loan;
import org.library.management.model.Member;
import org.library.management.persistence.StorageService;

import java.util.*;

/**
 * Author: Artyom Aroyan
 * Date: 21.10.25
 * Time: 23:16:11
 */
public final class ServiceHelper {
    private final StorageService storageService;
    private Map<String, Book> books;
    private Map<String, Loan> loans;
    private Map<String, Member> members;

    public ServiceHelper(StorageService storageService) {
        this.storageService = Objects.requireNonNull(storageService, "Storage Service can not be null!.");
        loadData();
    }

    void loadData() {
        try {
            this.books = Optional.ofNullable(storageService.loadBooks()).orElse(new HashMap<>());
            this.loans = Optional.ofNullable(storageService.loadLoans()).orElse(new HashMap<>());
            this.members = Optional.ofNullable(storageService.loadMembers()).orElse(new HashMap<>());

            IO.println("Data loaded successfully:");
        } catch (Exception ex) {
            IO.println("Error loading data: " + ex.getMessage());
            books = new HashMap<>();
            loans = new HashMap<>();
            members = new HashMap<>();
        }
    }

    String saveData() {
        try {
            String bookStatus = storageService.saveBook(books);
            String loanStatus = storageService.saveLoan(loans);
            String memberStatus = storageService.saveMember(members);
            return "Save completed: Books=" + bookStatus + ", Loans=" + loanStatus + ", Members=" + memberStatus;
        } catch (Exception ex) {
            return "Error saving data: " + ex.getMessage();
        }
    }

    public Map<String, Book> getBooks() {
        return books;
    }

    public Map<String, Loan> getLoans() {
        return loans;
    }

    public Map<String, Member> getMembers() {
        return members;
    }

    String uuidToString(UUID id) {
        return String.valueOf(id);
    }
}