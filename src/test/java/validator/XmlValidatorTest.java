package validator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class XmlValidatorTest {
    private static final String VALID_XML = "src/main/resources/xml/gems.xml";
    private static final String XSD_FILE = "src/main/resources/xsd/gems.xsd";
    private final XmlValidator validator = new XmlValidator();

    @Test
    void testValidateValidXml() {
        boolean isValid = validator.validate(VALID_XML, XSD_FILE);
        assertTrue(isValid, "Valid XML should pass validation");
    }

    @Test
    void testValidateWithInvalidXsdPath() {
        boolean isValid = validator.validate(VALID_XML, "nonexistent.xsd");
        assertFalse(isValid, "Validation should fail with invalid XSD path");
    }

    @Test
    void testValidateWithNonExistentXmlFile() {
        boolean isValid = validator.validate("nonexistent.xml", XSD_FILE);
        assertFalse(isValid, "Validation should fail with non-existent XML file");
    }

    @Test
    void testValidateWithInvalidXmlPath() {
        boolean isValid = validator.validate("/invalid/path/to/file.xml", XSD_FILE);
        assertFalse(isValid, "Validation should fail with invalid XML path");
    }
}

