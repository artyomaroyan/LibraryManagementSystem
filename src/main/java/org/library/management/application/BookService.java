package org.library.management.application;

import org.library.management.model.Book;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 21.10.25
 * Time: 23:10:04
 */
public interface BookService {
    String addBook(String title, String author, int year, int totalCopies);
    Collection<Book> listOfBooks();
    Optional<Book> findBookByTitle(String title);
    boolean deleteBookById(UUID id);
}