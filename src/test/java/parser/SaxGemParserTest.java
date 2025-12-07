package parser;

import model.Gem;
import model.Preciousness;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SaxGemParserTest {
    private static final String XML_FILE = "src/main/resources/xml/gems.xml";
    private final SaxGemParser parser = new SaxGemParser();

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
        assertFalse(firstGem.getVisualParameters().isEmpty());
    }

    @Test
    void testParseGemAttributes() throws Exception {
        List<Gem> gems = parser.parse(XML_FILE);
        
        Gem gem = gems.stream()
            .filter(g -> "gem001".equals(g.getId()))
            .findFirst()
            .orElse(null);
        
        assertNotNull(gem);
        assertEquals("gem001", gem.getId());
        assertEquals("Diamond", gem.getName());
        assertEquals(Preciousness.PRECIOUS, gem.getPreciousness());
    }

    @Test
    void testParseWithNonExistentFile() {
        assertThrows(Exception.class, () -> {
            parser.parse("nonexistent.xml");
        });
    }

    @Test
    void testParseWithInvalidPath() {
        assertThrows(Exception.class, () -> {
            parser.parse("/invalid/path/to/file.xml");
        });
    }
}

