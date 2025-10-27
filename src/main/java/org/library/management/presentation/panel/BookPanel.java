package org.library.management.presentation.panel;

import org.library.management.application.BookService;
import org.library.management.model.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 26.10.25
 * Time: 22:30:34
 */
public class BookPanel extends JPanel {
    private final BookService bookService;

    private JTable booksTable;
    private JTextField searchField;
    private DefaultTableModel tableModel;

    public BookPanel(BookService bookService) {
        this.bookService = bookService;
        initializeUI();
        loadBooks();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // title.
        JLabel titleLabel = new JLabel("Book Management");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        // center panel with table and search.
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));

        // search panel.
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton clearSearchButton = new JButton("Clear");

        searchPanel.add(new JLabel("Search by title"));
        searchPanel.add(searchField);
        searchButton.add(searchButton);
        searchButton.add(clearSearchButton);

        // table.
        String[] columns = {"ID", "Title", "Author", "Year", "Total Copies", "Available Copies"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        booksTable = new JTable(tableModel);
        booksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(booksTable);

        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(tableScrollPane, BorderLayout.CENTER);

        // buttons panel.
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add new book");
        JButton deleteButton = new JButton("Delete selected book");
        JButton refreshButton = new JButton("Refresh");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        centerPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);

        // add action listeners.
        addButton.addActionListener(_ -> showAddBookDialog());
        deleteButton.addActionListener(_ -> deleteSelectedBook());
        refreshButton.addActionListener(_ -> loadBooks());
        searchButton.addActionListener(_ -> searchBooks());
        clearSearchButton.addActionListener(_ -> {
            searchField.setText("");
            loadBooks();
        });
    }

    private void loadBooks() {
        tableModel.setRowCount(0);
        Collection<Book> books = bookService.listOfBooks();
        for (Book book : books) {
            tableModel.addRow(new Object[]{
                    book.id().toString(),
                    book.title(),
                    book.author(),
                    book.year(),
                    book.totalCopies(),
                    book.availableCopies()
            });
        }
    }

    private void searchBooks() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            loadBooks();
            return;
        }

        tableModel.setRowCount(0);
        Optional<Book> book = bookService.findBookByTitle(searchText);
        if (book.isPresent()) {
            Book b = book.get();
            tableModel.addRow(new Object[]{
                    b.id().toString(),
                    b.title(),
                    b.author(),
                    b.year(),
                    b.totalCopies(),
                    b.availableCopies()
            });
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "No book found with title: " + searchText,
                    "Search result",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void showAddBookDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add New Book", true);
        dialog.setLayout(new GridLayout(0, 2, 10, 10));
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField yearField = new JTextField();
        JTextField copiesField = new JTextField();

        dialog.add(new JLabel("Title:"));
        dialog.add(titleField);
        dialog.add(new JLabel("Author:"));
        dialog.add(authorField);
        dialog.add(new JLabel("Publication Year:"));
        dialog.add(yearField);
        dialog.add(new JLabel("Total Copies:"));
        dialog.add(copiesField);

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(new JLabel());
        dialog.add(buttonPanel);

        saveButton.addActionListener(_ -> {
            try {
                String title = titleField.getText().trim();
                String author = authorField.getText().trim();
                int year = Integer.parseInt(yearField.getText().trim());
                int copies = Integer.parseInt(copiesField.getText().trim());

                if (title.isEmpty() || author.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            dialog,
                            "Title and author can not be empty!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String result = bookService.addBook(title, author, year, copies);
                JOptionPane.showMessageDialog(
                        dialog,
                        result,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                dialog.dispose();
                loadBooks();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        dialog,
                        "Pleas enter valid number for year and copies",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        cancelButton.addActionListener(_ -> dialog.dispose());
        dialog.setVisible(true);
    }

    private void deleteSelectedBook() {
        int selectedRaw = booksTable.getSelectedRow();
        if (selectedRaw == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a book to delete!",
                    "No selection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String bookId = (String) tableModel.getValueAt(selectedRaw, 0);
        String bookTitle = (String) tableModel.getValueAt(selectedRaw, 1);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you shore you want to delete book: " + bookTitle + " ?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            UUID id = UUID.fromString(bookId);
            boolean deleted = bookService.deleteBookById(id);

            if (deleted) {
                JOptionPane.showMessageDialog(
                        this,
                        "Book deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                loadBooks();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to delete book!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}