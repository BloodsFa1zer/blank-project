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
}

