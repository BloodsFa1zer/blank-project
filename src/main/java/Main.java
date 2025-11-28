package main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import comparator.GemComparator;
import model.Gem;
import parser.DomGemParser;
import parser.GemParser;
import parser.SaxGemParser;
import parser.StaxGemParser;
import transformer.XslTransformer;
import validator.XmlValidator;

import java.util.Collections;
import java.util.List;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    
    private static final String XML_FILE = "src/main/resources/xml/gems.xml";
    private static final String XSD_FILE = "src/main/resources/xsd/gems.xsd";
    private static final String XSL_FILE = "src/main/resources/xsl/transform.xsl";
    private static final String OUTPUT_XML = "target/transformed-gems.xml";

    public static void main(String[] args) {
        logger.info("Starting Diamond Fund Laboratory Application");
        
        try {
            XmlValidator validator = new XmlValidator();
            boolean isValid = validator.validate(XML_FILE, XSD_FILE);
            if (!isValid) {
                logger.error("XML validation failed. Exiting.");
                return;
            }
            
            parseWithSax();
            parseWithDom();
            parseWithStax();
            
            transformXml();
            
            logger.info("Application completed successfully");
        } catch (Exception e) {
            logger.error("Error during application execution", e);
        }
    }
    
    private static void parseWithSax() throws Exception {
        logger.info("=== SAX Parser ===");
        GemParser parser = new SaxGemParser();
        List<Gem> gems = parser.parse(XML_FILE);
        displayAndSortGems(gems, "SAX");
    }
    
    private static void parseWithDom() throws Exception {
        logger.info("=== DOM Parser ===");
        GemParser parser = new DomGemParser();
        List<Gem> gems = parser.parse(XML_FILE);
        displayAndSortGems(gems, "DOM");
    }
    
    private static void parseWithStax() throws Exception {
        logger.info("=== StAX Parser ===");
        GemParser parser = new StaxGemParser();
        List<Gem> gems = parser.parse(XML_FILE);
        displayAndSortGems(gems, "StAX");
    }
    
    private static void displayAndSortGems(List<Gem> gems, String parserName) {
        logger.info("Parsed {} gems using {} parser", gems.size(), parserName);
        
        Collections.sort(gems, GemComparator.byName());
        logger.info("Gems sorted by name:");
        gems.forEach(gem -> logger.info("  - {}", gem.getName()));
        
        Collections.sort(gems, GemComparator.byValue());
        logger.info("Gems sorted by value (carats):");
        gems.forEach(gem -> logger.info("  - {}: {} carats", gem.getName(), gem.getValue()));
    }
    
    private static void transformXml() throws Exception {
        logger.info("=== XSL Transformation ===");
        XslTransformer transformer = new XslTransformer();
        transformer.transform(XML_FILE, XSL_FILE, OUTPUT_XML);
        logger.info("Transformed XML saved to: {}", OUTPUT_XML);
    }
}

