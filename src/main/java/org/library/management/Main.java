package org.library.management;

import org.library.management.application.BookService;
import org.library.management.application.LoanService;
import org.library.management.application.MemberService;
import org.library.management.application.service.BookServiceImpl;
import org.library.management.application.service.LoanServiceImpl;
import org.library.management.application.service.MemberServiceImpl;
import org.library.management.application.service.ServiceHelper;
import org.library.management.persistence.JsonStorage;
import org.library.management.presentation.ConsoleUI;

/**
 * Author: Artyom Aroyan
 * Date: 20.10.25
 * Time: 21:34:28
 */
public class Main {
    static void main() {
        try {
            IO.println("🚀 Starting Library Management System...");

            JsonStorage storage = new JsonStorage();
            ServiceHelper serviceHelper = new ServiceHelper(storage);

            BookService bookService = new BookServiceImpl(serviceHelper);
            LoanService loanService = new LoanServiceImpl(serviceHelper);
            MemberService memberService = new MemberServiceImpl(serviceHelper);

            ConsoleUI ui = new ConsoleUI(bookService, loanService, memberService);
            ui.start();

            IO.println("👋 Thank you for using Library Management System!");
        } catch (Exception ex) {
            IO.println("💥 Fatal error: " + ex.getMessage());
        }
    }
}
