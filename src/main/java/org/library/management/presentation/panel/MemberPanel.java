package org.library.management.presentation.panel;

import org.library.management.application.MemberService;
import org.library.management.model.Member;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 26.10.25
 * Time: 22:31:10
 */
public class MemberPanel extends JPanel {
    private final MemberService memberService;

    private JTable membersTable;
    private JTextField searchField;
    private DefaultTableModel tableModel;

    public MemberPanel(MemberService memberService) {
        this.memberService = memberService;
        initializeUI();
        loadMembers();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // title.
        JLabel titleLabel = new JLabel("Member MManagement");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        // center panel.
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));

        // search panel.
        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton clearSearchButton = new JButton("Clear");

        searchPanel.add(new JLabel("Search by name"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(clearSearchButton);

        // table.
        String[] columns = {"ID", "Name", "Email", "Join Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        membersTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(membersTable);

        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(tableScrollPane, BorderLayout.CENTER);

        // buttons panel.
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add New Member");
        JButton deleteButton = new JButton("Delete Selected Member");
        JButton refreshButton = new JButton("Refresh");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        centerPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);

        // action listeners.
        addButton.addActionListener(_ -> showAddMemberDialog());
        deleteButton.addActionListener(_ -> deleteSelectedMember());
        refreshButton.addActionListener(_ -> loadMembers());
        searchButton.addActionListener(_ -> searchMember());
        clearSearchButton.addActionListener(_ -> {
            searchField.setText("");
            loadMembers();
        });
    }

    private void loadMembers() {
        tableModel.setRowCount(0);
        Collection<Member> members = memberService.listOfMembers();
        for (Member member : members) {
            tableModel.addRow(new Object[]{
                    member.id().toString(),
                    member.name(),
                    member.email(),
                    member.joinedDate().toString()
            });
        }
    }

    private void searchMember() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            loadMembers();
            return;
        }

        tableModel.setRowCount(0);
        Optional<Member> member = memberService.findMemberByName(searchText);
        if (member.isPresent()) {
            Member m = member.get();
            tableModel.addRow(new Object[]{
                    m.id().toString(),
                    m.name(),
                    m.email(),
                    m.joinedDate().toString()
            });
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "No member found with name: " + searchText,
                    "Search Result",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void showAddMemberDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add New Member", true);
        dialog.setLayout(new GridLayout(0, 2, 10, 10));
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);

        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();

        dialog.add(new JLabel("Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Email:"));
        dialog.add(emailField);

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(new JLabel());
        dialog.add(buttonPanel);

        saveButton.addActionListener(_ -> {
            try {
                String name = nameField.getName().trim();
                String email = emailField.getText().trim();

                if (name.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            dialog,
                            "Name or email can not be empty!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                String result = memberService.addMember(name, email);
                JOptionPane.showMessageDialog(
                        dialog,
                        result,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                dialog.dispose();
                loadMembers();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        dialog,
                        "Error creation new member!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        cancelButton.addActionListener(_ -> dialog.dispose());
        dialog.setVisible(true);
    }

    private void deleteSelectedMember() {
        int selectedRow = membersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a member to delete!",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String memberId = (String) tableModel.getValueAt(selectedRow, 0);
        String memberName = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you shore you want to delete member: " + memberName + " ?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            UUID id = UUID.fromString(memberId);
            boolean deleted = memberService.deleteMemberById(id);
            if (deleted) {
                JOptionPane.showMessageDialog(
                        this,
                        "Member successfully deleted!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                loadMembers();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to delete member!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}