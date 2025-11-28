package parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import model.Color;
import model.Gem;
import model.Preciousness;
import model.VisualParameters;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class StaxGemParser implements GemParser {
    private static final Logger logger = LogManager.getLogger(StaxGemParser.class);

    @Override
    public List<Gem> parse(String xmlFilePath) throws Exception {
        logger.info("Starting StAX parsing of file: {}", xmlFilePath);
        
        XMLInputFactory factory = XMLInputFactory.newInstance();
        
        List<Gem> gems = new ArrayList<>();
        Gem currentGem = null;
        VisualParameters currentVisualParams = null;
        List<Color> currentColors = null;
        boolean inVisualParameters = false;
        String currentElement = null;
        
        try (FileInputStream inputStream = new FileInputStream(xmlFilePath);
             XMLStreamReader reader = factory.createXMLStreamReader(inputStream)) {
        
            while (reader.hasNext()) {
                int event = reader.next();
                
                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        String localName = reader.getLocalName();
                        String namespaceURI = reader.getNamespaceURI();
                        
                        if (!ParserConstants.NAMESPACE.equals(namespaceURI)) {
                            break;
                        }
                        
                        currentElement = localName;
                        
                        if ("gem".equals(localName)) {
                            currentGem = new Gem();
                            String id = reader.getAttributeValue(null, "id");
                            if (id != null) {
                                currentGem.setId(id);
                            }
                        } else if ("visualParameters".equals(localName)) {
                            inVisualParameters = true;
                            currentVisualParams = new VisualParameters();
                            currentColors = new ArrayList<>();
                        }
                        break;
                        
                    case XMLStreamConstants.CHARACTERS:
                        if (currentElement == null || currentGem == null) {
                            break;
                        }
                        
                        String text = reader.getText().trim();
                        if (text.isEmpty()) {
                            break;
                        }
                        
                        switch (currentElement) {
                            case "name":
                                currentGem.setName(text);
                                break;
                            case "preciousness":
                                currentGem.setPreciousness(Preciousness.fromString(text));
                                break;
                            case "origin":
                                currentGem.setOrigin(text);
                                break;
                            case "value":
                                currentGem.setValue(new BigDecimal(text));
                                break;
                            case "color":
                                if (inVisualParameters && currentVisualParams != null) {
                                    currentColors.add(Color.fromString(text));
                                }
                                break;
                            case "transparency":
                                if (inVisualParameters && currentVisualParams != null) {
                                    currentVisualParams.setTransparency(new BigDecimal(text));
                                }
                                break;
                            case "facets":
                                if (inVisualParameters && currentVisualParams != null) {
                                    currentVisualParams.setFacets(Integer.parseInt(text));
                                }
                                break;
                        }
                        break;
                        
                    case XMLStreamConstants.END_ELEMENT:
                        localName = reader.getLocalName();
                        namespaceURI = reader.getNamespaceURI();
                        
                        if (!ParserConstants.NAMESPACE.equals(namespaceURI)) {
                            break;
                        }
                        
                        if ("gem".equals(localName)) {
                            if (currentGem != null) {
                                gems.add(currentGem);
                                currentGem = null;
                            }
                        } else if ("visualParameters".equals(localName)) {
                            if (currentVisualParams != null && currentGem != null) {
                                currentVisualParams.setColors(currentColors);
                                if (currentGem.getVisualParameters() == null) {
                                    currentGem.setVisualParameters(new ArrayList<>());
                                }
                                currentGem.getVisualParameters().add(currentVisualParams);
                                currentVisualParams = null;
                                currentColors = null;
                                inVisualParameters = false;
                            }
                        }
                        
                        currentElement = null;
                        break;
                }
            }
        }
        
        logger.info("StAX parsing completed. Found {} gems", gems.size());
        return gems;
    }
}

