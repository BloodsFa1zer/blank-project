package parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import model.Color;
import model.Gem;
import model.Preciousness;
import model.VisualParameters;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SaxGemParser implements GemParser {
    private static final Logger logger = LogManager.getLogger(SaxGemParser.class);

    @Override
    public List<Gem> parse(String xmlFilePath) throws Exception {
        logger.info("Starting SAX parsing of file: {}", xmlFilePath);
        
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        SAXParser saxParser = factory.newSAXParser();
        
        GemHandler handler = new GemHandler();
        saxParser.parse(new File(xmlFilePath), handler);
        
        List<Gem> gems = handler.getGems();
        logger.info("SAX parsing completed. Found {} gems", gems.size());
        
        return gems;
    }

    private static class GemHandler extends org.xml.sax.helpers.DefaultHandler {
        private List<Gem> gems;
        private Gem currentGem;
        private VisualParameters currentVisualParams;
        private StringBuilder currentText;
        private boolean inVisualParameters;
        private List<Color> currentColors;

        public GemHandler() {
            this.gems = new ArrayList<>();
            this.currentText = new StringBuilder();
        }

        @Override
        public void startElement(String uri, String localName, String qName, 
                                 org.xml.sax.Attributes attributes) {
            currentText.setLength(0);
            
            if ("gem".equals(localName) && ParserConstants.NAMESPACE.equals(uri)) {
                currentGem = new Gem();
                String id = attributes.getValue("id");
                if (id != null) {
                    currentGem.setId(id);
                }
            } else if ("visualParameters".equals(localName) && ParserConstants.NAMESPACE.equals(uri)) {
                inVisualParameters = true;
                currentVisualParams = new VisualParameters();
                currentColors = new ArrayList<>();
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            currentText.append(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            String text = currentText.toString().trim();
            
            if (!ParserConstants.NAMESPACE.equals(uri)) {
                return;
            }
            
            switch (localName) {
                case "gem":
                    if (currentGem != null) {
                        gems.add(currentGem);
                        currentGem = null;
                    }
                    break;
                case "name":
                    if (currentGem != null) {
                        currentGem.setName(text);
                    }
                    break;
                case "preciousness":
                    if (currentGem != null) {
                        currentGem.setPreciousness(Preciousness.fromString(text));
                    }
                    break;
                case "origin":
                    if (currentGem != null) {
                        currentGem.setOrigin(text);
                    }
                    break;
                case "visualParameters":
                    if (currentGem != null && currentVisualParams != null) {
                        currentVisualParams.setColors(currentColors);
                        if (currentGem.getVisualParameters() == null) {
                            currentGem.setVisualParameters(new ArrayList<>());
                        }
                        currentGem.getVisualParameters().add(currentVisualParams);
                        currentVisualParams = null;
                        currentColors = null;
                        inVisualParameters = false;
                    }
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
                case "value":
                    if (currentGem != null) {
                        currentGem.setValue(new BigDecimal(text));
                    }
                    break;
            }
        }

        public List<Gem> getGems() {
            return gems;
        }
    }
}

