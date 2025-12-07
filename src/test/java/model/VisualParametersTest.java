package model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VisualParametersTest {

    @Test
    void testVisualParametersCreation() {
        VisualParameters params = new VisualParameters();
        
        assertNotNull(params.getColors());
        assertTrue(params.getColors().isEmpty());
        assertNull(params.getTransparency());
        assertNull(params.getFacets());
    }

    @Test
    void testVisualParametersWithConstructor() {
        List<Color> colors = Arrays.asList(Color.RED, Color.BLUE);
        BigDecimal transparency = new BigDecimal("0.95");
        Integer facets = 58;
        
        VisualParameters params = new VisualParameters(colors, transparency, facets);
        
        assertEquals(colors, params.getColors());
        assertEquals(transparency, params.getTransparency());
        assertEquals(facets, params.getFacets());
    }

    @Test
    void testVisualParametersWithNullColors() {
        VisualParameters params = new VisualParameters(null, new BigDecimal("0.9"), 57);
        
        assertNotNull(params.getColors());
        assertTrue(params.getColors().isEmpty());
    }

    @Test
    void testSettersAndGetters() {
        VisualParameters params = new VisualParameters();
        
        List<Color> colors = Arrays.asList(Color.GREEN, Color.YELLOW);
        BigDecimal transparency = new BigDecimal("0.88");
        Integer facets = 60;
        
        params.setColors(colors);
        params.setTransparency(transparency);
        params.setFacets(facets);
        
        assertEquals(colors, params.getColors());
        assertEquals(transparency, params.getTransparency());
        assertEquals(facets, params.getFacets());
    }

    @Test
    void testEquals() {
        VisualParameters params1 = new VisualParameters();
        params1.setColors(Arrays.asList(Color.RED));
        params1.setTransparency(new BigDecimal("0.95"));
        params1.setFacets(58);
        
        VisualParameters params2 = new VisualParameters();
        params2.setColors(Arrays.asList(Color.RED));
        params2.setTransparency(new BigDecimal("0.95"));
        params2.setFacets(58);
        
        VisualParameters params3 = new VisualParameters();
        params3.setColors(Arrays.asList(Color.BLUE));
        params3.setTransparency(new BigDecimal("0.95"));
        params3.setFacets(58);
        
        assertEquals(params1, params2);
        assertNotEquals(params1, params3);
        assertNotEquals(params1, null);
        assertEquals(params1, params1); // reflexive
    }

    @Test
    void testHashCode() {
        VisualParameters params1 = new VisualParameters();
        params1.setColors(Arrays.asList(Color.RED));
        params1.setTransparency(new BigDecimal("0.95"));
        params1.setFacets(58);
        
        VisualParameters params2 = new VisualParameters();
        params2.setColors(Arrays.asList(Color.RED));
        params2.setTransparency(new BigDecimal("0.95"));
        params2.setFacets(58);
        
        assertEquals(params1.hashCode(), params2.hashCode());
    }

    @Test
    void testToString() {
        VisualParameters params = new VisualParameters();
        params.setColors(Arrays.asList(Color.RED, Color.BLUE));
        params.setTransparency(new BigDecimal("0.95"));
        params.setFacets(58);
        
        String toString = params.toString();
        
        assertNotNull(toString);
        assertTrue(toString.contains("VisualParameters"));
        assertTrue(toString.contains("0.95"));
        assertTrue(toString.contains("58"));
    }

    @Test
    void testMultipleColors() {
        VisualParameters params = new VisualParameters();
        List<Color> colors = Arrays.asList(Color.RED, Color.BLUE, Color.GREEN);
        params.setColors(colors);
        
        assertEquals(3, params.getColors().size());
        assertTrue(params.getColors().contains(Color.RED));
        assertTrue(params.getColors().contains(Color.BLUE));
        assertTrue(params.getColors().contains(Color.GREEN));
    }

    @Test
    void testEmptyColorsList() {
        VisualParameters params = new VisualParameters();
        params.setColors(new ArrayList<>());
        
        assertNotNull(params.getColors());
        assertTrue(params.getColors().isEmpty());
    }
}
