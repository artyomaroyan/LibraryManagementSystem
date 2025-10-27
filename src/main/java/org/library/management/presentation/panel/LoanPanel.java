package org.library.management.presentation.panel;

import org.library.management.application.BookService;
import org.library.management.application.LoanService;
import org.library.management.application.MemberService;
import org.library.management.model.Loan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 26.10.25
 * Time: 22:31:38
 */
public class LoanPanel extends JPanel {
    private final LoanService loanService;

    private JTable loanTable;
    private DefaultTableModel tableModel;

    public LoanPanel(LoanService loanService, BookService bookService, MemberService memberService) {
        this.loanService = loanService;
        initializeUI();
        loadLoans();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // title.
        JLabel titleLabel = new JLabel("Loan Management");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        // center panel.
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));

        // table.
        String[] columns = {"Loan ID", "Book ID", "Member ID", "Laon Date", "Due Date", "Returned"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        loanTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(loanTable);

        centerPanel.add(tableScrollPane, BorderLayout.CENTER);

        // buttons panel.
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton creatLoanButton = new JButton("Create New Loan");
        JButton returnLoanButton = new JButton("Return Selected Loan");
        JButton overdueLoanButton = new JButton("Show Overdue Loans");
        JButton refreshButton = new JButton("Refresh All Loans");

        buttonPanel.add(creatLoanButton);
        buttonPanel.add(returnLoanButton);
        buttonPanel.add(overdueLoanButton);
        buttonPanel.add(refreshButton);

        centerPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);

        // action listeners.
        creatLoanButton.addActionListener(_ -> showCreateLoanDialog());
        returnLoanButton.addActionListener(_ -> returnSelectedLoan());
        overdueLoanButton.addActionListener(_ -> showOverdueLoans());
        refreshButton.addActionListener(_ -> loadLoans());
    }

    private void loadLoans() {
        tableModel.setRowCount(0);
        loadAllLoans();
    }

    private void loadAllLoans() {
        tableModel.setRowCount(0);
        Collection<Loan> loans = loanService.getAllLoans();
        for (Loan loan : loans) {
            tableModel.addRow(new Object[]{
                    loan.id().toString(),
                    loan.bookId().toString(),
                    loan.memberId().toString(),
                    loan.loanDate().toString(),
                    loan.dueDate().toString(),
                    loan.returned()
            });
        }
    }

    private void showCreateLoanDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Create New Loan", true);
        dialog.setLayout(new GridLayout(0, 2, 10, 10));
        dialog.setSize(400, 150);
        dialog.setLocationRelativeTo(this);

        JTextField memberIdField = new JTextField();
        JTextField bookIdField = new JTextField();

        dialog.add(new JLabel("Member ID:"));
        dialog.add(memberIdField);
        dialog.add(new JLabel("Book ID:"));
        dialog.add(bookIdField);

        JButton saveButton = new JButton("Create Loan");
        JButton cancelButton = new JButton("Cancel");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(new JLabel());
        dialog.add(buttonPanel);

        saveButton.addActionListener(_ -> {
            try {
                UUID memberId = UUID.fromString(memberIdField.getText().trim());
                UUID bookId = UUID.fromString(bookIdField.getText().trim());

                boolean result = loanService.createLoan(memberId, bookId);
                if (result) {
                    JOptionPane.showMessageDialog(
                            dialog,
                            "Loan created successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    dialog.dispose();
                    loadLoans();
                } else {
                    JOptionPane.showMessageDialog(
                            dialog,
                            "Failed to create loan!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(
                        dialog,
                        "Please enter valid UUID's!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        cancelButton.addActionListener(_ -> dialog.dispose());
        dialog.setVisible(true);
    }

    private void returnSelectedLoan() {
        int selectedRow = loanTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a loan to return!",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String loanId = (String) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you shore you want to return this loan?",
                "Confirm Return",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            UUID id = UUID.fromString(loanId);
            boolean returned = loanService.returnLoan(id);
            if (returned) {
                JOptionPane.showMessageDialog(
                        this,
                        "Loan returned successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                loadLoans();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to return loan!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void showOverdueLoans() {
        List<Loan> overdueLoans = loanService.getOverdueLoans();
        if (overdueLoans.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No overdue loans found!",
                    "Overdue Loans",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        // create a dialog to display overdue loans.
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Overdue Loan", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);

        String[] columns = {"Loan ID", "Book ID", "Member ID", "Due Date", "Days Overdue"};
        DefaultTableModel overdueModel = new DefaultTableModel(columns, 0);
        JTable overdueTable = new JTable(overdueModel);

        for (Loan loan : overdueLoans) {
            long dasOverdue = Duration.between(loan.dueDate(), Instant.now()).toDays();
            overdueModel.addRow(new Object[]{
                    loan.id().toString(),
                    loan.bookId().toString(),
                    loan.memberId().toString(),
                    loan.dueDate().toString(),
                    dasOverdue,
                    loan.returned()
            });
        }

        dialog.add(new JScrollPane(overdueTable), BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(_ -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
}