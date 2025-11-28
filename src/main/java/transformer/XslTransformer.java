package transformer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

public class XslTransformer {
    private static final Logger logger = LogManager.getLogger(XslTransformer.class);

    public void transform(String xmlFilePath, String xslFilePath, String outputFilePath) throws Exception {
        logger.info("Starting XSL transformation. Input: {}, XSL: {}, Output: {}", 
                   xmlFilePath, xslFilePath, outputFilePath);
        
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(new StreamSource(new File(xslFilePath)));
        
        transformer.transform(
            new StreamSource(new File(xmlFilePath)),
            new StreamResult(new File(outputFilePath))
        );
        
        logger.info("XSL transformation completed successfully");
    }
}

