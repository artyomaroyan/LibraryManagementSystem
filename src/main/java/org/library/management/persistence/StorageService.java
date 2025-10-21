package org.library.management.persistence;

import org.library.management.model.Book;
import org.library.management.model.Loan;
import org.library.management.model.Member;

import java.util.Map;

/**
 * Author: Artyom Aroyan
 * Date: 20.10.25
 * Time: 22:21:19
 */
public interface StorageService {
    Map<String, Book> loadBooks();
    Map<String, Loan> loadLoans();
    Map<String, Member> loadMembers();

    String saveBook(Map<String, Book> books);
    String saveLoan(Map<String, Loan> loans);
    String saveMember(Map<String, Member> members);
}