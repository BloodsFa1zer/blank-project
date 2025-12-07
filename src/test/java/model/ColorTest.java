package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorTest {

    @Test
    void testAllColorValues() {
        assertEquals("green", Color.GREEN.getValue());
        assertEquals("red", Color.RED.getValue());
        assertEquals("yellow", Color.YELLOW.getValue());
        assertEquals("blue", Color.BLUE.getValue());
        assertEquals("white", Color.WHITE.getValue());
        assertEquals("pink", Color.PINK.getValue());
        assertEquals("purple", Color.PURPLE.getValue());
        assertEquals("orange", Color.ORANGE.getValue());
        assertEquals("black", Color.BLACK.getValue());
    }

    @Test
    void testFromStringValidValues() {
        assertEquals(Color.GREEN, Color.fromString("green"));
        assertEquals(Color.RED, Color.fromString("red"));
        assertEquals(Color.YELLOW, Color.fromString("yellow"));
        assertEquals(Color.BLUE, Color.fromString("blue"));
        assertEquals(Color.WHITE, Color.fromString("white"));
        assertEquals(Color.PINK, Color.fromString("pink"));
        assertEquals(Color.PURPLE, Color.fromString("purple"));
        assertEquals(Color.ORANGE, Color.fromString("orange"));
        assertEquals(Color.BLACK, Color.fromString("black"));
    }

    @Test
    void testFromStringCaseSensitive() {
        // Should be case-sensitive
        assertThrows(IllegalArgumentException.class, () -> {
            Color.fromString("GREEN");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            Color.fromString("Red");
        });
    }

    @Test
    void testFromStringInvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> {
            Color.fromString("invalid");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            Color.fromString("cyan");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            Color.fromString("");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            Color.fromString(null);
        });
    }

    @Test
    void testFromStringWithWhitespace() {
        // Should handle whitespace if needed, but currently doesn't
        assertThrows(IllegalArgumentException.class, () -> {
            Color.fromString(" red ");
        });
    }
}
