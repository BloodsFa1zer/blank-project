package parser;

import model.Gem;
import model.Preciousness;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StaxGemParserTest {
    private static final String XML_FILE = "src/main/resources/xml/gems.xml";
    private final StaxGemParser parser = new StaxGemParser();

    @Test
    void testParse() throws Exception {
        List<Gem> gems = parser.parse(XML_FILE);
        
        assertNotNull(gems);
        assertFalse(gems.isEmpty());
        assertTrue(gems.size() >= 7);
    }

    @Test
    void testParseGemValues() throws Exception {
        List<Gem> gems = parser.parse(XML_FILE);
        
        Gem diamond = gems.stream()
            .filter(g -> "Diamond".equals(g.getName()))
            .findFirst()
            .orElse(null);
        
        assertNotNull(diamond);
        assertEquals("Diamond", diamond.getName());
        assertEquals(Preciousness.PRECIOUS, diamond.getPreciousness());
        assertEquals(new BigDecimal("2.5"), diamond.getValue());
        assertEquals("South Africa", diamond.getOrigin());
    }

    @Test
    void testAllParsersReturnSameData() throws Exception {
        SaxGemParser saxParser = new SaxGemParser();
        DomGemParser domParser = new DomGemParser();
        StaxGemParser staxParser = new StaxGemParser();
        
        List<Gem> saxGems = saxParser.parse(XML_FILE);
        List<Gem> domGems = domParser.parse(XML_FILE);
        List<Gem> staxGems = staxParser.parse(XML_FILE);
        
        assertEquals(saxGems.size(), domGems.size());
        assertEquals(domGems.size(), staxGems.size());
    }
}

