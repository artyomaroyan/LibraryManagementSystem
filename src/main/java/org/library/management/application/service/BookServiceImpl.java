package org.library.management.application.service;

import org.library.management.application.BookService;
import org.library.management.model.Book;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 21.10.25
 * Time: 23:10:59
 */
public class BookServiceImpl implements BookService {
    private final Map<String, Book> books;
    private final ServiceHelper serviceHelper;

    public BookServiceImpl(ServiceHelper serviceHelper) {
        this.serviceHelper = serviceHelper;
        this.books = serviceHelper.getBooks();
    }

    @Override
    public String addBook(String title, String author, int year, int totalCopies) {
        try {
            UUID id = UUID.randomUUID();
            Book book = new Book(id, title, author, year, totalCopies, totalCopies);
            books.put(serviceHelper.uuidToString(id), book);
            return serviceHelper.saveData();
        } catch (Exception ex) {
            return "Error creating member" + ex.getMessage();
        }
    }

    @Override
    public boolean existsById(UUID bookId) {
        return books.containsKey(serviceHelper.uuidToString(bookId));
    }

    @Override
    public Collection<Book> listOfBooks() {
        return books.values();
    }

    @Override
    public Optional<Book> findBookByTitle(String title) {
        return books.values().stream()
                .filter(book -> book.title().equalsIgnoreCase(title))
                .findFirst();
    }

    @Override
    public boolean deleteBookById(UUID id) {
        if (books.remove(serviceHelper.uuidToString(id)) != null) {
            serviceHelper.saveData();
            return true;
        }
        return false;
    }
}