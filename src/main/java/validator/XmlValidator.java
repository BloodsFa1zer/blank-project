package validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;

public class XmlValidator {
    private static final Logger logger = LogManager.getLogger(XmlValidator.class);

    public boolean validate(String xmlFilePath, String xsdFilePath) {
        try {
            logger.info("Starting validation of XML file: {} against XSD: {}", xmlFilePath, xsdFilePath);
            
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdFilePath));
            Validator validator = schema.newValidator();
            
            Source source = new StreamSource(new File(xmlFilePath));
            validator.validate(source);
            
            logger.info("XML validation successful");
            return true;
        } catch (Exception e) {
            logger.error("XML validation failed: {}", e.getMessage(), e);
            return false;
        }
    }
}

