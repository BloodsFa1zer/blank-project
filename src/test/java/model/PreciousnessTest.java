package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PreciousnessTest {

    @Test
    void testAllPreciousnessValues() {
        assertEquals("precious", Preciousness.PRECIOUS.getValue());
        assertEquals("semi-precious", Preciousness.SEMI_PRECIOUS.getValue());
    }

    @Test
    void testFromStringValidValues() {
        assertEquals(Preciousness.PRECIOUS, Preciousness.fromString("precious"));
        assertEquals(Preciousness.SEMI_PRECIOUS, Preciousness.fromString("semi-precious"));
    }

    @Test
    void testFromStringCaseSensitive() {
        // Should be case-sensitive
        assertThrows(IllegalArgumentException.class, () -> {
            Preciousness.fromString("PRECIOUS");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            Preciousness.fromString("Precious");
        });
    }

    @Test
    void testFromStringInvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> {
            Preciousness.fromString("invalid");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            Preciousness.fromString("precious-stone");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            Preciousness.fromString("");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            Preciousness.fromString(null);
        });
    }

    @Test
    void testFromStringWithWhitespace() {
        // Should handle whitespace if needed, but currently doesn't
        assertThrows(IllegalArgumentException.class, () -> {
            Preciousness.fromString(" precious ");
        });
    }
}
