package org.library.management;

import org.library.management.application.BookService;
import org.library.management.application.LoanService;
import org.library.management.application.MemberService;
import org.library.management.presentation.panel.BookPanel;
import org.library.management.presentation.panel.LoanPanel;
import org.library.management.presentation.panel.MemberPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Author: Artyom Aroyan
 * Date: 26.10.25
 * Time: 22:29:03
 */
public class MainFrame extends JFrame {
    private final BookService bookService;
    private final LoanService loanService;
    private final MemberService memberService;

    private BookPanel bookPanel;
    private LoanPanel loanPanel;
    private MemberPanel memberPanel;

    private JPanel mainPanel;
    private CardLayout cardLayout;

    public MainFrame(BookService bookService, LoanService loanService, MemberService memberService) {
        this.bookService = bookService;
        this.loanService = loanService;
        this.memberService = memberService;
    }

    private void initializeUI() {
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        // create main panel with card layout.
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // create navigation bar.
        JPanel navigationPanel = createNavigationPanel();

        // create content panels.
        bookPanel = new BookPanel(bookService);
        memberPanel = new MemberPanel(memberService);
        loanPanel = new LoanPanel(loanService, bookService, memberService);

        // add panels to card layout.
        mainPanel.add(bookPanel, "BOOKS");
        mainPanel.add(loanPanel, "LOANS");
        mainPanel.add(memberPanel, "MEMBERS");

        // add components to frame.
        setLayout(new BorderLayout());
        add(navigationPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createNavigationPanel() {
        JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navigationPanel.setBackground(new Color(51, 51, 51));
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton bookButton = createNavigationButton("Manage Books", "BOOKS");
        JButton loanButton = createNavigationButton("Manage Loans", "LOANS");
        JButton memberButton = createNavigationButton("Manage Members", "MEMBERS");

        navigationPanel.add(bookButton);
        navigationPanel.add(loanButton);
        navigationPanel.add(memberButton);

        return navigationPanel;
    }

    private JButton createNavigationButton(String text, String panelName) {
        JButton button = new JButton(text);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFont(new Font("SansSerif", Font.BOLD, 14));

        button.addActionListener(e -> cardLayout.show(mainPanel, panelName));
        return button;
    }
}