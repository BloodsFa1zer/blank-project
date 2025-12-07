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

/**
 * SAX парсер для обробки XML файлів з інформацією про камені
 * SAX - це event-based парсер, тобто він викликає наші методи коли зустрічає елементи
 * Перевага: швидкий, не завантажує весь файл в пам'ять
 * Недолік: складніше працювати, бо не можна "прокрутити" назад
 */
public class SaxGemParser implements GemParser {
    private static final Logger logger = LogManager.getLogger(SaxGemParser.class);

    /**
     * Парсимо XML файл за допомогою SAX парсера
     * @param xmlFilePath шлях до XML файлу
     * @return список знайдених каменів
     */
    @Override
    public List<Gem> parse(String xmlFilePath) throws Exception {
        logger.info("Starting SAX parsing of file: {}", xmlFilePath);
        
        // Створюємо фабрику для SAX парсера
        // Фабрика потрібна щоб отримати екземпляр парсера
        SAXParserFactory factory = SAXParserFactory.newInstance();
        // Вказуємо що парсер повинен враховувати namespace
        // Це важливо бо в нашому XML є namespace
        factory.setNamespaceAware(true);
        SAXParser saxParser = factory.newSAXParser();
        
        // Створюємо обробник подій - він буде викликатись коли парсер знаходить елементи
        GemHandler handler = new GemHandler();
        // Запускаємо парсинг - парсер буде викликати методи handler'а
        saxParser.parse(new File(xmlFilePath), handler);
        
        // Після парсингу отримуємо список каменів з handler'а
        List<Gem> gems = handler.getGems();
        logger.info("SAX parsing completed. Found {} gems", gems.size());
        
        return gems;
    }

    /**
     * Внутрішній клас-обробник подій SAX парсера
     * Коли парсер зустрічає початок елемента - викликається startElement
     * Коли зустрічає текст - викликається characters
     * Коли зустрічає кінець елемента - викликається endElement
     */
    private static class GemHandler extends org.xml.sax.helpers.DefaultHandler {
        private List<Gem> gems; // тут зберігаємо всі знайдені камені
        private Gem currentGem; // поточний камінь який парсимо
        private VisualParameters currentVisualParams; // поточні візуальні параметри
        private StringBuilder currentText; // текст поточного елемента (може бути розбитий на частини)
        private boolean inVisualParameters; // прапорець чи ми зараз всередині visualParameters
        private List<Color> currentColors; // кольори для поточних візуальних параметрів

        public GemHandler() {
            this.gems = new ArrayList<>();
            this.currentText = new StringBuilder();
        }

        /**
         * Викликається коли парсер знаходить початок елемента
         * Тут ініціалізуємо об'єкти для нового елемента
         */
        @Override
        public void startElement(String uri, String localName, String qName, 
                                 org.xml.sax.Attributes attributes) {
            // Очищаємо текст попереднього елемента
            currentText.setLength(0);
            
            // Перевіряємо чи це елемент gem з правильним namespace
            if ("gem".equals(localName) && ParserConstants.NAMESPACE.equals(uri)) {
                // Створюємо новий об'єкт каменя
                currentGem = new Gem();
                // Отримуємо атрибут id (якщо є)
                String id = attributes.getValue("id");
                if (id != null) {
                    currentGem.setId(id);
                }
            } else if ("visualParameters".equals(localName) && ParserConstants.NAMESPACE.equals(uri)) {
                // Якщо знайшли visualParameters - встановлюємо прапорець
                // і створюємо новий об'єкт для візуальних параметрів
                inVisualParameters = true;
                currentVisualParams = new VisualParameters();
                currentColors = new ArrayList<>();
            }
        }

        /**
         * Викликається коли парсер знаходить текст всередині елемента
         * Важливо: текст може прийти частинами, тому додаємо до StringBuilder
         */
        @Override
        public void characters(char[] ch, int start, int length) {
            currentText.append(ch, start, length);
        }

        /**
         * Викликається коли парсер знаходить кінець елемента
         * Тут ми вже знаємо весь текст елемента і можемо його обробити
         */
        @Override
        public void endElement(String uri, String localName, String qName) {
            // Отримуємо весь текст елемента і прибираємо зайві пробіли
            String text = currentText.toString().trim();
            
            // Перевіряємо namespace - якщо не наш, ігноруємо
            if (!ParserConstants.NAMESPACE.equals(uri)) {
                return;
            }
            
            // Обробляємо різні типи елементів
            switch (localName) {
                case "gem":
                    // Кінець елемента gem - додаємо камінь до списку
                    if (currentGem != null) {
                        gems.add(currentGem);
                        currentGem = null; // очищаємо поточний камінь
                    }
                    break;
                case "name":
                    if (currentGem != null) {
                        currentGem.setName(text);
                    }
                    break;
                case "preciousness":
                    if (currentGem != null) {
                        // Використовуємо fromString щоб перетворити рядок в enum
                        currentGem.setPreciousness(Preciousness.fromString(text));
                    }
                    break;
                case "origin":
                    if (currentGem != null) {
                        currentGem.setOrigin(text);
                    }
                    break;
                case "visualParameters":
                    // Кінець visualParameters - додаємо до каменя
                    if (currentGem != null && currentVisualParams != null) {
                        currentVisualParams.setColors(currentColors);
                        // Перевіряємо чи є вже список, якщо ні - створюємо
                        if (currentGem.getVisualParameters() == null) {
                            currentGem.setVisualParameters(new ArrayList<>());
                        }
                        currentGem.getVisualParameters().add(currentVisualParams);
                        // Очищаємо поточні параметри
                        currentVisualParams = null;
                        currentColors = null;
                        inVisualParameters = false;
                    }
                    break;
                case "color":
                    // Колір може бути тільки всередині visualParameters
                    if (inVisualParameters && currentVisualParams != null) {
                        currentColors.add(Color.fromString(text));
                    }
                    break;
                case "transparency":
                    if (inVisualParameters && currentVisualParams != null) {
                        // Парсимо BigDecimal з рядка
                        currentVisualParams.setTransparency(new BigDecimal(text));
                    }
                    break;
                case "facets":
                    if (inVisualParameters && currentVisualParams != null) {
                        // Парсимо int з рядка
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

