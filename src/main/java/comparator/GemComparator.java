package comparator;

import model.Gem;

import java.util.Comparator;

public class GemComparator {
    
    public static Comparator<Gem> byName() {
        return Comparator.comparing(Gem::getName, String.CASE_INSENSITIVE_ORDER);
    }
    
    public static Comparator<Gem> byValue() {
        return Comparator.comparing(Gem::getValue);
    }
    
    public static Comparator<Gem> byOrigin() {
        return Comparator.comparing(Gem::getOrigin, String.CASE_INSENSITIVE_ORDER);
    }
    
    public static Comparator<Gem> byPreciousnessAndValue() {
        return Comparator
            .comparing((Gem g) -> g.getPreciousness().getValue())
            .thenComparing(Gem::getValue);
    }
}

