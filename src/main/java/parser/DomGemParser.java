package parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import model.Color;
import model.Gem;
import model.Preciousness;
import model.VisualParameters;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DomGemParser implements GemParser {
    private static final Logger logger = LogManager.getLogger(DomGemParser.class);

    @Override
    public List<Gem> parse(String xmlFilePath) throws Exception {
        logger.info("Starting DOM parsing of file: {}", xmlFilePath);
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(xmlFilePath));
        
        document.getDocumentElement().normalize();
        
        List<Gem> gems = new ArrayList<>();
        NodeList gemNodes = document.getElementsByTagNameNS(ParserConstants.NAMESPACE, "gem");
        
        for (int i = 0; i < gemNodes.getLength(); i++) {
            Node gemNode = gemNodes.item(i);
            if (gemNode.getNodeType() == Node.ELEMENT_NODE) {
                Gem gem = parseGemElement((Element) gemNode);
                gems.add(gem);
            }
        }
        
        logger.info("DOM parsing completed. Found {} gems", gems.size());
        return gems;
    }

    private Gem parseGemElement(Element gemElement) {
        Gem gem = new Gem();
        
        gem.setId(gemElement.getAttribute("id"));
        
        gem.setName(getElementText(gemElement, "name"));
        gem.setPreciousness(Preciousness.fromString(getElementText(gemElement, "preciousness")));
        gem.setOrigin(getElementText(gemElement, "origin"));
        gem.setValue(new BigDecimal(getElementText(gemElement, "value")));
        
        NodeList visualParamsNodes = gemElement.getElementsByTagNameNS(ParserConstants.NAMESPACE, "visualParameters");
        List<VisualParameters> visualParamsList = new ArrayList<>();
        
        for (int i = 0; i < visualParamsNodes.getLength(); i++) {
            Element visualParamsElement = (Element) visualParamsNodes.item(i);
            VisualParameters visualParams = parseVisualParameters(visualParamsElement);
            visualParamsList.add(visualParams);
        }
        
        gem.setVisualParameters(visualParamsList);
        
        return gem;
    }

    private VisualParameters parseVisualParameters(Element visualParamsElement) {
        VisualParameters visualParams = new VisualParameters();
        
        NodeList colorNodes = visualParamsElement.getElementsByTagNameNS(ParserConstants.NAMESPACE, "color");
        List<Color> colors = new ArrayList<>();
        for (int i = 0; i < colorNodes.getLength(); i++) {
            String colorText = colorNodes.item(i).getTextContent().trim();
            colors.add(Color.fromString(colorText));
        }
        visualParams.setColors(colors);
        
        String transparencyText = getElementText(visualParamsElement, "transparency");
        visualParams.setTransparency(new BigDecimal(transparencyText));
        
        String facetsText = getElementText(visualParamsElement, "facets");
        visualParams.setFacets(Integer.parseInt(facetsText));
        
        return visualParams;
    }

    private String getElementText(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagNameNS(ParserConstants.NAMESPACE, tagName);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent().trim();
        }
        return "";
    }
}

