package org.library.management.application.service;

import org.library.management.model.Book;
import org.library.management.model.Loan;
import org.library.management.model.Member;
import org.library.management.persistence.StorageService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 21.10.25
 * Time: 23:16:11
 */
public final class ServiceHelper {
    private final StorageService storageService;
    private Map<String, Book> books = new HashMap<>();
    private Map<String, Loan> loans = new HashMap<>();
    private Map<String, Member> members = new HashMap<>();

    public ServiceHelper(StorageService storageService) {
        this.storageService = storageService;
        loadData();
    }

    void loadData() {
        this.books = Optional.ofNullable(storageService.loadBooks()).orElse(new HashMap<>());
        this.loans = Optional.ofNullable(storageService.loadLoans()).orElse(new HashMap<>());
        this.members = Optional.ofNullable(storageService.loadMembers()).orElse(new HashMap<>());
    }

    String saveData() {
        String bookStatus = storageService.saveBook(books == null ? new HashMap<>() : books);
        String loanStatus = storageService.saveLoan(loans == null ? new HashMap<>() : loans);
        String memberStatus = storageService.saveMember(members == null ? new HashMap<>() : members);
        return String.format("Save completed: Books=%s, Loans=%s, Members=%s",
                bookStatus, loanStatus, memberStatus);
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