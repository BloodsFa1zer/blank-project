package model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GemTest {

    @Test
    void testGemCreation() {
        Gem gem = new Gem();
        gem.setId("test001");
        gem.setName("Test Gem");
        gem.setPreciousness(Preciousness.PRECIOUS);
        gem.setOrigin("Test Origin");
        gem.setValue(new BigDecimal("1.5"));
        gem.setVisualParameters(new ArrayList<>());
        
        assertEquals("test001", gem.getId());
        assertEquals("Test Gem", gem.getName());
        assertEquals(Preciousness.PRECIOUS, gem.getPreciousness());
        assertEquals("Test Origin", gem.getOrigin());
        assertEquals(new BigDecimal("1.5"), gem.getValue());
    }

    @Test
    void testGemEquals() {
        Gem gem1 = new Gem();
        gem1.setId("test001");
        
        Gem gem2 = new Gem();
        gem2.setId("test001");
        
        Gem gem3 = new Gem();
        gem3.setId("test002");
        
        assertEquals(gem1, gem2);
        assertNotEquals(gem1, gem3);
    }

    @Test
    void testPreciousnessEnum() {
        Preciousness precious = Preciousness.fromString("precious");
        Preciousness semiPrecious = Preciousness.fromString("semi-precious");
        
        assertEquals(Preciousness.PRECIOUS, precious);
        assertEquals(Preciousness.SEMI_PRECIOUS, semiPrecious);
        
        assertThrows(IllegalArgumentException.class, () -> {
            Preciousness.fromString("invalid");
        });
    }

    @Test
    void testColorEnum() {
        Color red = Color.fromString("red");
        Color green = Color.fromString("green");
        
        assertEquals(Color.RED, red);
        assertEquals(Color.GREEN, green);
        
        assertThrows(IllegalArgumentException.class, () -> {
            Color.fromString("invalid");
        });
    }

    @Test
    void testGemHashCode() {
        Gem gem1 = new Gem();
        gem1.setId("test001");
        
        Gem gem2 = new Gem();
        gem2.setId("test001");
        
        Gem gem3 = new Gem();
        gem3.setId("test002");
        
        assertEquals(gem1.hashCode(), gem2.hashCode());
        assertNotEquals(gem1.hashCode(), gem3.hashCode());
    }

    @Test
    void testGemToString() {
        Gem gem = new Gem();
        gem.setId("test001");
        gem.setName("Test Gem");
        gem.setPreciousness(Preciousness.PRECIOUS);
        gem.setOrigin("Test Origin");
        gem.setValue(new BigDecimal("1.5"));
        gem.setVisualParameters(new ArrayList<>());
        
        String toString = gem.toString();
        
        assertNotNull(toString);
        assertTrue(toString.contains("Gem"));
        assertTrue(toString.contains("test001"));
        assertTrue(toString.contains("Test Gem"));
        assertTrue(toString.contains("PRECIOUS"));
    }

    @Test
    void testGemEqualsWithNull() {
        Gem gem = new Gem();
        gem.setId("test001");
        
        assertNotEquals(gem, null);
    }

    @Test
    void testGemEqualsWithDifferentClass() {
        Gem gem = new Gem();
        gem.setId("test001");
        
        assertNotEquals(gem, "not a gem");
    }

    @Test
    void testGemWithNullId() {
        Gem gem1 = new Gem();
        gem1.setId(null);
        
        Gem gem2 = new Gem();
        gem2.setId(null);
        
        // Two gems with null IDs should be equal
        assertEquals(gem1, gem2);
    }

    @Test
    void testGemWithEmptyId() {
        Gem gem1 = new Gem();
        gem1.setId("");
        
        Gem gem2 = new Gem();
        gem2.setId("");
        
        assertEquals(gem1, gem2);
    }
}

