package org.library.management.application;

import org.library.management.model.Loan;

import java.util.List;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 21.10.25
 * Time: 23:10:15
 */
public interface LoanService {
    boolean createLoan(UUID memberId, UUID bookId);
    boolean returnLoan(UUID loanId);
    boolean existsById(UUID loanId);
    List<Loan> getOverdueLoans();
}