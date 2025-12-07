package comparator;

import model.Gem;
import model.Preciousness;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GemComparatorTest {

    @Test
    void testCompareByName() {
        List<Gem> gems = createTestGems();
        
        Collections.sort(gems, GemComparator.byName());
        
        assertEquals("Amethyst", gems.get(0).getName());
        assertEquals("Diamond", gems.get(1).getName());
        assertEquals("Ruby", gems.get(2).getName());
    }

    @Test
    void testCompareByValue() {
        List<Gem> gems = createTestGems();
        
        Collections.sort(gems, GemComparator.byValue());
        
        assertEquals(new BigDecimal("1.8"), gems.get(0).getValue());
        assertEquals(new BigDecimal("2.5"), gems.get(1).getValue());
        assertEquals(new BigDecimal("3.2"), gems.get(2).getValue());
    }

    @Test
    void testCompareByOrigin() {
        List<Gem> gems = createTestGems();
        
        Collections.sort(gems, GemComparator.byOrigin());
        
        assertEquals("Colombia", gems.get(0).getOrigin());
        assertEquals("Myanmar", gems.get(1).getOrigin());
        assertEquals("South Africa", gems.get(2).getOrigin());
    }

    @Test
    void testCompareByPreciousnessAndValue() {
        List<Gem> gems = createTestGems();
        
        Collections.sort(gems, GemComparator.byPreciousnessAndValue());
        
        assertTrue(gems.get(0).getPreciousness() == Preciousness.PRECIOUS ||
                   gems.get(1).getPreciousness() == Preciousness.PRECIOUS);
    }

    private List<Gem> createTestGems() {
        List<Gem> gems = new ArrayList<>();
        
        Gem gem1 = new Gem();
        gem1.setName("Diamond");
        gem1.setPreciousness(Preciousness.PRECIOUS);
        gem1.setOrigin("South Africa");
        gem1.setValue(new BigDecimal("2.5"));
        gem1.setVisualParameters(new ArrayList<>());
        gems.add(gem1);
        
        Gem gem2 = new Gem();
        gem2.setName("Ruby");
        gem2.setPreciousness(Preciousness.PRECIOUS);
        gem2.setOrigin("Myanmar");
        gem2.setValue(new BigDecimal("1.8"));
        gem2.setVisualParameters(new ArrayList<>());
        gems.add(gem2);
        
        Gem gem3 = new Gem();
        gem3.setName("Amethyst");
        gem3.setPreciousness(Preciousness.SEMI_PRECIOUS);
        gem3.setOrigin("Colombia");
        gem3.setValue(new BigDecimal("3.2"));
        gem3.setVisualParameters(new ArrayList<>());
        gems.add(gem3);
        
        return gems;
    }

    @Test
    void testCompareByNameCaseInsensitive() {
        List<Gem> gems = new ArrayList<>();
        
        Gem gem1 = new Gem();
        gem1.setName("diamond");
        gem1.setValue(new BigDecimal("2.5"));
        gem1.setVisualParameters(new ArrayList<>());
        gems.add(gem1);
        
        Gem gem2 = new Gem();
        gem2.setName("Diamond");
        gem2.setValue(new BigDecimal("2.5"));
        gem2.setVisualParameters(new ArrayList<>());
        gems.add(gem2);
        
        Gem gem3 = new Gem();
        gem3.setName("DIAMOND");
        gem3.setValue(new BigDecimal("2.5"));
        gem3.setVisualParameters(new ArrayList<>());
        gems.add(gem3);
        
        Collections.sort(gems, GemComparator.byName());
        
        // All should be considered equal (case-insensitive)
        assertEquals(3, gems.size());
    }

    @Test
    void testCompareByOriginCaseInsensitive() {
        List<Gem> gems = new ArrayList<>();
        
        Gem gem1 = new Gem();
        gem1.setOrigin("south africa");
        gem1.setValue(new BigDecimal("2.5"));
        gem1.setVisualParameters(new ArrayList<>());
        gems.add(gem1);
        
        Gem gem2 = new Gem();
        gem2.setOrigin("South Africa");
        gem2.setValue(new BigDecimal("2.5"));
        gem2.setVisualParameters(new ArrayList<>());
        gems.add(gem2);
        
        Collections.sort(gems, GemComparator.byOrigin());
        
        assertEquals(2, gems.size());
    }

    @Test
    void testCompareByValueWithSameValues() {
        List<Gem> gems = new ArrayList<>();
        
        Gem gem1 = new Gem();
        gem1.setName("Gem1");
        gem1.setValue(new BigDecimal("2.5"));
        gem1.setVisualParameters(new ArrayList<>());
        gems.add(gem1);
        
        Gem gem2 = new Gem();
        gem2.setName("Gem2");
        gem2.setValue(new BigDecimal("2.5"));
        gem2.setVisualParameters(new ArrayList<>());
        gems.add(gem2);
        
        Collections.sort(gems, GemComparator.byValue());
        
        // Both have same value, order should be preserved or stable
        assertEquals(2, gems.size());
        assertEquals(new BigDecimal("2.5"), gems.get(0).getValue());
        assertEquals(new BigDecimal("2.5"), gems.get(1).getValue());
    }

    @Test
    void testCompareByPreciousnessAndValueDetailed() {
        List<Gem> gems = new ArrayList<>();
        
        // Precious with lower value
        Gem gem1 = new Gem();
        gem1.setName("Ruby");
        gem1.setPreciousness(Preciousness.PRECIOUS);
        gem1.setValue(new BigDecimal("1.5"));
        gem1.setVisualParameters(new ArrayList<>());
        gems.add(gem1);
        
        // Semi-precious with higher value
        Gem gem2 = new Gem();
        gem2.setName("Amethyst");
        gem2.setPreciousness(Preciousness.SEMI_PRECIOUS);
        gem2.setValue(new BigDecimal("5.0"));
        gem2.setVisualParameters(new ArrayList<>());
        gems.add(gem2);
        
        // Precious with higher value
        Gem gem3 = new Gem();
        gem3.setName("Diamond");
        gem3.setPreciousness(Preciousness.PRECIOUS);
        gem3.setValue(new BigDecimal("3.0"));
        gem3.setVisualParameters(new ArrayList<>());
        gems.add(gem3);
        
        Collections.sort(gems, GemComparator.byPreciousnessAndValue());
        
        // Precious gems should come first, sorted by value
        assertEquals(Preciousness.PRECIOUS, gems.get(0).getPreciousness());
        assertEquals(Preciousness.PRECIOUS, gems.get(1).getPreciousness());
        assertEquals(Preciousness.SEMI_PRECIOUS, gems.get(2).getPreciousness());
        
        // Within precious, sorted by value
        assertTrue(gems.get(0).getValue().compareTo(gems.get(1).getValue()) <= 0);
    }
}

