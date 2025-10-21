package org.library.management.service;

import org.library.management.model.Book;
import org.library.management.model.Loan;
import org.library.management.model.Member;
import org.library.management.persistence.StorageService;

import java.util.Map;

/**
 * Author: Artyom Aroyan
 * Date: 20.10.25
 * Time: 22:20:45
 */
public class LibraryService {
    private final Map<String, Book> books;
    private final Map<String, Loan> loans;
    private final Map<String, Member> members;
    private final StorageService storageService;

    LibraryService(Map<String, Book> books, Map<String, Loan> loans, Map<String, Member> members, StorageService storageService) {
        this.books = books;
        this.loans = loans;
        this.members = members;
        this.storageService = storageService;
    }


}