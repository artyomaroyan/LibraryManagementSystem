package org.library.management.model;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 20.10.25
 * Time: 21:45:52
 */
public record Book(
        UUID id,
        String title,
        String author,
        int year,
        int totalCopies,
        int availableCopies) {
}