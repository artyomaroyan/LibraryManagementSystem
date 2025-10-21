package org.library.management.model;

import java.time.LocalDate;
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
        LocalDate loanDate,
        LocalDate dueDate,
        LocalDate returnedDate) {
}