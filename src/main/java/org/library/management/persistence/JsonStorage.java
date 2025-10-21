package org.library.management.persistence;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.library.management.model.Book;
import org.library.management.model.Loan;
import org.library.management.model.Member;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Artyom Aroyan
 * Date: 20.10.25
 * Time: 22:21:29
 */
public class JsonStorage implements StorageService {
    private static final String BOOKS_FILE = "resources/data/books.json";
    private static final String LOANS_FILE = "resources/data/loans.json";
    private static final String MEMBERS_FILE = "resources/data/members.json";
    private final Gson gson = new Gson();

    @Override
    public Map<String, Book> loadBooks() {
        return readJson(BOOKS_FILE, new TypeToken<Map<String, Book>>() {}.getType());
    }

    @Override
    public Map<String, Loan> loadLoans() {
        return readJson(LOANS_FILE, new TypeToken<Map<String, Loan>>() {}.getType());
    }

    @Override
    public Map<String, Member> loadMembers() {
        return readJson(MEMBERS_FILE, new TypeToken<Map<String, Member>>() {}.getType());
    }

    @Override
    public String saveBook(Map<String, Book> books) {
        return writeJson(BOOKS_FILE, books);
    }

    @Override
    public String saveLoan(Map<String, Loan> loans) {
        return writeJson(LOANS_FILE, loans);
    }

    @Override
    public String saveMember(Map<String, Member> members) {
        return writeJson(MEMBERS_FILE, members);
    }

    private <T> Map<String, T> readJson(String path, Type type) {
        try (Reader reader = new FileReader(path)) {
            return gson.fromJson(reader, type);
        } catch (FileNotFoundException ex) {
            return new HashMap<>();
        } catch (IOException ex) {
            throw new RuntimeException("Error reading " + path, ex);
        }
    }

    private <T> String writeJson(String path, Map<String, T> data) {
        Path pth = Paths.get(path);
        try {
            Files.createDirectories(pth.getParent());
            try (Writer writer = Files.newBufferedWriter(pth)) {
                gson.toJson(data, writer);
            }
            return "Successfully wrote JSON to " + path;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}