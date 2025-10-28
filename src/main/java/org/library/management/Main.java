package org.library.management;

import org.library.management.application.BookService;
import org.library.management.application.LoanService;
import org.library.management.application.MemberService;
import org.library.management.application.service.BookServiceImpl;
import org.library.management.application.service.LoanServiceImpl;
import org.library.management.application.service.MemberServiceImpl;
import org.library.management.application.service.ServiceHelper;
import org.library.management.persistence.JsonStorage;

import javax.swing.*;

/**
 * Author: Artyom Aroyan
 * Date: 20.10.25
 * Time: 21:34:28
 */
public class Main {
    static void main() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            IO.println("Error setting look and feel: " + ex.getMessage());
        }

        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JsonStorage storage = new JsonStorage();
        ServiceHelper serviceHelper = new ServiceHelper(storage);

        BookService bookService = new BookServiceImpl(serviceHelper);
        LoanService loanService = new LoanServiceImpl(serviceHelper);
        MemberService memberService = new MemberServiceImpl(serviceHelper);

        MainFrame mainFrame = new MainFrame(bookService, loanService, memberService);
        mainFrame.setVisible(true);
    }
}
