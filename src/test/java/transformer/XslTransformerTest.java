package transformer;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class XslTransformerTest {
    private static final String XML_FILE = "src/main/resources/xml/gems.xml";
    private static final String XSL_FILE = "src/main/resources/xsl/transform.xsl";
    private static final String OUTPUT_FILE = "target/test-transformed.xml";
    private final XslTransformer transformer = new XslTransformer();

    @Test
    void testTransform() throws Exception {
        new File("target").mkdirs();
        
        transformer.transform(XML_FILE, XSL_FILE, OUTPUT_FILE);
        
        File outputFile = new File(OUTPUT_FILE);
        assertTrue(outputFile.exists(), "Transformed XML file should be created");
        assertTrue(outputFile.length() > 0, "Transformed XML file should not be empty");
    }

    @Test
    void testTransformWithNonExistentXmlFile() {
        assertThrows(Exception.class, () -> {
            transformer.transform("nonexistent.xml", XSL_FILE, OUTPUT_FILE);
        });
    }

    @Test
    void testTransformWithNonExistentXslFile() {
        assertThrows(Exception.class, () -> {
            transformer.transform(XML_FILE, "nonexistent.xsl", OUTPUT_FILE);
        });
    }

    @Test
    void testTransformWithInvalidPath() {
        assertThrows(Exception.class, () -> {
            transformer.transform("/invalid/path.xml", XSL_FILE, OUTPUT_FILE);
        });
    }
}

