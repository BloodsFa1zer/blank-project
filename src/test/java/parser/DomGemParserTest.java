package parser;

import model.Gem;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DomGemParserTest {
    private static final String XML_FILE = "src/main/resources/xml/gems.xml";
    private final DomGemParser parser = new DomGemParser();

    @Test
    void testParse() throws Exception {
        List<Gem> gems = parser.parse(XML_FILE);
        
        assertNotNull(gems);
        assertFalse(gems.isEmpty());
        assertTrue(gems.size() >= 7);
        
        Gem firstGem = gems.get(0);
        assertNotNull(firstGem.getId());
        assertNotNull(firstGem.getName());
        assertNotNull(firstGem.getPreciousness());
        assertNotNull(firstGem.getOrigin());
        assertNotNull(firstGem.getValue());
        assertNotNull(firstGem.getVisualParameters());
    }

    @Test
    void testParseVisualParameters() throws Exception {
        List<Gem> gems = parser.parse(XML_FILE);
        
        Gem gem = gems.stream()
            .filter(g -> "gem005".equals(g.getId()))
            .findFirst()
            .orElse(null);
        
        assertNotNull(gem);
        assertFalse(gem.getVisualParameters().isEmpty());
        assertTrue(gem.getVisualParameters().get(0).getColors().size() >= 1);
    }
}

