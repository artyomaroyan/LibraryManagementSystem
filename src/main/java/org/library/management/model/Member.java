package org.library.management.model;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 20.10.25
 * Time: 21:50:27
 */
public record Member(
        UUID id,
        String name,
        String email,
        LocalDate joinedDate) {
}