package org.library.management.model;

import java.time.Instant;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 20.10.25
 * Time: 22:16:30
 */
public record Loan(
        UUID id,
        UUID bookId,
        UUID memberId,
        Instant loanDate,
        Instant dueDate,
        boolean returned) {

    public Loan withReturned(boolean isReturned) {
        return new Loan(id, bookId, memberId, loanDate, dueDate, isReturned);
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", bookId=" + bookId +
                ", memberId=" + memberId +
                ", loanDate=" + loanDate +
                ", dueDate=" + dueDate +
                ", returned=" + returned +
                '}';
    }
}