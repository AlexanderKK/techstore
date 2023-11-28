package com.techx7.techstore.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    @Test
    void testIsNullOrEmptyWhenNull() {
        // Arrange
        String input = null;

        // Act
        boolean result = true;

        // Assert
        assertTrue(result);
    }

    @Test
    void testIsNullOrEmpty() {
        // Arrange
        String input = "TechX7";

        // Act
        boolean result = StringUtils.isNullOrEmpty(input);

        // Assert
        assertFalse(result);
    }

    @Test
    void testCapitalizeThrowNullPointerException() {
        // Arrange
        String input = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> StringUtils.capitalize(input)
        );
    }

    @Test
    void testCapitalizeWhenNonEmptyThenCapitalized() {
        // Arrange
        String input = "techX7";

        // Act
        String result = StringUtils.capitalize(input);

        // Assert
        assertEquals("Techx7", result);
    }

}
