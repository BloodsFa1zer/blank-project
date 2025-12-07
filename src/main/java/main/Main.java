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
import java.util.Scanner;

/**
 * Головний клас для лабораторної роботи з обробки XML
 * Тут реалізовано два режими: демо та інтерактивний
 */
public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    
    // Шляхи до файлів - використовуються в обох режимах
    private static final String XML_FILE = "src/main/resources/xml/gems.xml";
    private static final String XSD_FILE = "src/main/resources/xsd/gems.xsd";
    private static final String XSL_FILE = "src/main/resources/xsl/transform.xsl";
    private static final String OUTPUT_XML = "target/transformed-gems.xml";
    
    // Прапорець для визначення чи використовувати затримки (тільки в демо режимі)
    private static boolean useDelays = false;

    public static void main(String[] args) {
        logger.info("Запуск програми для обробки XML файлів з інформацією про дорогоцінні камені");
        
        // Якщо передано аргумент - використовуємо його для автоматичного вибору
        if (args.length > 0) {
            if (args[0].equals("demo")) {
                runDemoMode();
            } else if (args[0].equals("interactive")) {
                runInteractiveMode();
            } else {
                // Невірний аргумент - показуємо меню
                showModeSelection();
            }
        } else {
            // Якщо аргументів немає - показуємо меню вибору режиму
            showModeSelection();
        }
    }
    
    /**
     * Показує меню вибору режиму роботи
     * Користувач обирає між демо та інтерактивним режимом
     */
    private static void showModeSelection() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n=== ВИБІР РЕЖИМУ РОБОТИ ===");
        System.out.println("Оберіть режим:");
        System.out.println("1 - Демо режим (автоматичне виконання всіх операцій)");
        System.out.println("2 - Інтерактивний режим (вибір конкретних операцій)");
        System.out.print("\nВаш вибір: ");
        
        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // очищаємо буфер
            
            switch (choice) {
                case 1:
                    runDemoMode();
                    break;
                case 2:
                    runInteractiveMode();
                    break;
                default:
                    System.out.println("Невірний вибір! Запускаю демо режим за замовчуванням.");
                    runDemoMode();
            }
        } catch (Exception e) {
            System.out.println("Помилка при виборі режиму: " + e.getMessage());
            System.out.println("Запускаю демо режим за замовчуванням.");
            runDemoMode();
        } finally {
            scanner.close();
        }
    }
    
    /**
     * Демо режим - автоматично виконує всі операції
     * Корисно для швидкого тестування або показу функціональності
     * Додано затримки для кращої читабельності
     */
    private static void runDemoMode() {
        useDelays = true; // увімкнути затримки для демо режиму
        System.out.println("\n=== ДЕМО РЕЖИМ ===");
        System.out.println("Автоматичне виконання всіх операцій...\n");
        sleep(1500); // даємо час прочитати повідомлення
        
        try {
            // Спочатку завжди валідуємо XML перед роботою з ним
            // Це важливо, бо якщо файл невалідний - немає сенсу його парсити
            System.out.println("1. Валідація XML файлу...");
            sleep(800);
            XmlValidator validator = new XmlValidator();
            boolean isValid = validator.validate(XML_FILE, XSD_FILE);
            
            if (!isValid) {
                System.out.println("Помилка: XML файл не пройшов валідацію!");
                return;
            }
            System.out.println("✓ XML файл валідний\n");
            sleep(1200); // пауза після валідації
            
            // Парсимо одним парсером за раз і показуємо результати
            // Так можна порівняти чи всі парсери дають однаковий результат
            System.out.println("2. Парсинг через SAX парсер...");
            sleep(800);
            parseWithSax();
            System.out.println();
            sleep(2000); // пауза після виводу результатів SAX
            
            System.out.println("3. Парсинг через DOM парсер...");
            sleep(800);
            parseWithDom();
            System.out.println();
            sleep(2000); // пауза після виводу результатів DOM
            
            System.out.println("4. Парсинг через StAX парсер...");
            sleep(800);
            parseWithStax();
            System.out.println();
            sleep(2000); // пауза після виводу результатів StAX
            
            // XSL трансформація - перетворює XML в інший формат
            // У нашому випадку групує камені за типом дорогоцінності
            System.out.println("5. XSL трансформація...");
            sleep(800);
            transformXml();
            System.out.println("✓ Трансформація завершена. Результат у файлі: " + OUTPUT_XML);
            sleep(1500);
            
            System.out.println("\n=== ДЕМО РЕЖИМ ЗАВЕРШЕНО ===");
            logger.info("Демо режим виконано успішно");
            
        } catch (Exception e) {
            System.out.println("Помилка під час виконання: " + e.getMessage());
            logger.error("Помилка в демо режимі", e);
        } finally {
            useDelays = false; // вимкнути затримки після завершення демо
        }
    }
    
    /**
     * Допоміжний метод для затримки виконання
     * Використовується в демо режимі для кращої читабельності
     * @param milliseconds кількість мілісекунд для затримки
     */
    private static void sleep(long milliseconds) {
        if (!useDelays) {
            return; // не використовуємо затримки якщо не в демо режимі
        }
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Інтерактивний режим - користувач сам обирає що робити
     * Зручно коли потрібно перевірити щось конкретне
     */
    private static void runInteractiveMode() {
        useDelays = false; // вимкнути затримки в інтерактивному режимі
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n=== ІНТЕРАКТИВНИЙ РЕЖИМ ===");
        
        // Цикл для можливості виконати кілька операцій підряд
        boolean running = true;
        while (running) {
            System.out.println("\nОберіть дію:");
            System.out.println("1 - Валідація XML");
            System.out.println("2 - Парсинг через SAX");
            System.out.println("3 - Парсинг через DOM");
            System.out.println("4 - Парсинг через StAX");
            System.out.println("5 - XSL трансформація");
            System.out.println("6 - Порівняння всіх парсерів");
            System.out.println("7 - Сортування каменів");
            System.out.println("8 - Виконати все (як в демо)");
            System.out.println("0 - Вихід");
            System.out.print("\nВаш вибір: ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // очищаємо буфер після nextInt
                
                switch (choice) {
                    case 1:
                        validateXml();
                        break;
                    case 2:
                        parseWithSax();
                        break;
                    case 3:
                        parseWithDom();
                        break;
                    case 4:
                        parseWithStax();
                        break;
                    case 5:
                        transformXml();
                        break;
                    case 6:
                        compareAllParsers();
                        break;
                    case 7:
                        sortGemsInteractive(scanner);
                        break;
                    case 8:
                        runDemoMode();
                        break;
                    case 0:
                        System.out.println("До побачення!");
                        running = false; // виходимо з циклу
                        break;
                    default:
                        System.out.println("Невірний вибір! Спробуйте ще раз.");
                }
                
            } catch (Exception e) {
                System.out.println("Помилка: " + e.getMessage());
                logger.error("Помилка в інтерактивному режимі", e);
                scanner.nextLine(); // очищаємо буфер при помилці
            }
        }
        
        scanner.close();
    }
    
    /**
     * Валідація XML файлу
     * Перевіряє чи XML відповідає XSD схемі
     */
    private static void validateXml() {
        System.out.println("\n--- Валідація XML ---");
        try {
            XmlValidator validator = new XmlValidator();
            boolean isValid = validator.validate(XML_FILE, XSD_FILE);
            
            if (isValid) {
                System.out.println("✓ XML файл валідний!");
            } else {
                System.out.println("✗ XML файл не валідний!");
            }
        } catch (Exception e) {
            System.out.println("Помилка валідації: " + e.getMessage());
        }
    }
    
    /**
     * Парсинг через SAX парсер
     * SAX - це event-based парсер, читає файл послідовно
     * Плюс: швидкий, не завантажує весь файл в пам'ять
     * Мінус: складніший у використанні, не можна "прокручувати" назад
     */
    private static void parseWithSax() throws Exception {
        System.out.println("\n--- SAX Парсер ---");
        GemParser parser = new SaxGemParser();
        List<Gem> gems = parser.parse(XML_FILE);
        displayAndSortGems(gems, "SAX");
    }
    
    /**
     * Парсинг через DOM парсер
     * DOM - створює дерево об'єктів в пам'яті
     * Плюс: зручний, можна легко навігувати по дереву
     * Мінус: завантажує весь файл в пам'ять, повільніший для великих файлів
     */
    private static void parseWithDom() throws Exception {
        System.out.println("\n--- DOM Парсер ---");
        GemParser parser = new DomGemParser();
        List<Gem> gems = parser.parse(XML_FILE);
        displayAndSortGems(gems, "DOM");
    }
    
    /**
     * Парсинг через StAX парсер
     * StAX - це pull parser, сам контролюємо коли читати наступний елемент
     * Плюс: швидкий, зручний, не завантажує весь файл
     * Мінус: трохи складніший ніж DOM, але простіший ніж SAX
     */
    private static void parseWithStax() throws Exception {
        System.out.println("\n--- StAX Парсер ---");
        GemParser parser = new StaxGemParser();
        List<Gem> gems = parser.parse(XML_FILE);
        displayAndSortGems(gems, "StAX");
    }
    
    /**
     * Порівняння всіх трьох парсерів
     * Перевіряємо чи вони дають однакову кількість каменів
     * Це важливо для перевірки коректності реалізації
     */
    private static void compareAllParsers() {
        System.out.println("\n--- Порівняння парсерів ---");
        try {
            SaxGemParser saxParser = new SaxGemParser();
            DomGemParser domParser = new DomGemParser();
            StaxGemParser staxParser = new StaxGemParser();
            
            List<Gem> saxGems = saxParser.parse(XML_FILE);
            List<Gem> domGems = domParser.parse(XML_FILE);
            List<Gem> staxGems = staxParser.parse(XML_FILE);
            
            System.out.println("SAX парсер знайшов: " + saxGems.size() + " каменів");
            System.out.println("DOM парсер знайшов: " + domGems.size() + " каменів");
            System.out.println("StAX парсер знайшов: " + staxGems.size() + " каменів");
            
            if (saxGems.size() == domGems.size() && domGems.size() == staxGems.size()) {
                System.out.println("✓ Всі парсери знайшли однакову кількість каменів!");
            } else {
                System.out.println("✗ Парсери знайшли різну кількість каменів!");
            }
            
        } catch (Exception e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }
    
    /**
     * Інтерактивне сортування каменів
     * Користувач обирає за яким критерієм сортувати
     */
    private static void sortGemsInteractive(Scanner scanner) {
        System.out.println("\n--- Сортування каменів ---");
        
        try {
            // Спочатку парсимо файл (використовуємо SAX, можна будь-який)
            GemParser parser = new SaxGemParser();
            List<Gem> gems = parser.parse(XML_FILE);
            
            System.out.println("Оберіть критерій сортування:");
            System.out.println("1 - За назвою");
            System.out.println("2 - За вартістю (каратами)");
            System.out.println("3 - За походженням");
            System.out.println("4 - За дорогоцінністю та вартістю");
            System.out.print("Ваш вибір: ");
            
            int sortChoice = scanner.nextInt();
            scanner.nextLine();
            
            switch (sortChoice) {
                case 1:
                    Collections.sort(gems, GemComparator.byName());
                    System.out.println("\nКамені відсортовані за назвою:");
                    break;
                case 2:
                    Collections.sort(gems, GemComparator.byValue());
                    System.out.println("\nКамені відсортовані за вартістю:");
                    break;
                case 3:
                    Collections.sort(gems, GemComparator.byOrigin());
                    System.out.println("\nКамені відсортовані за походженням:");
                    break;
                case 4:
                    Collections.sort(gems, GemComparator.byPreciousnessAndValue());
                    System.out.println("\nКамені відсортовані за дорогоцінністю та вартістю:");
                    break;
                default:
                    System.out.println("Невірний вибір!");
                    return;
            }
            
            // Виводимо відсортовані камені
            for (int i = 0; i < gems.size(); i++) {
                Gem gem = gems.get(i);
                System.out.printf("%d. %s", i + 1, gem.getName());
                
                if (gem.getValue() != null) {
                    System.out.printf(" - %.2f карат", gem.getValue());
                }
                if (gem.getOrigin() != null) {
                    System.out.printf(" (%s)", gem.getOrigin());
                }
                System.out.println();
            }
            
        } catch (Exception e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }
    
    /**
     * Відображення та сортування каменів
     * Показує базову інформацію про камені та сортує їх двома способами
     * В демо режимі додає невеликі затримки для кращої читабельності
     */
    private static void displayAndSortGems(List<Gem> gems, String parserName) {
        System.out.println("Знайдено " + gems.size() + " каменів (парсер: " + parserName + ")");
        sleep(500);
        
        // Сортуємо за назвою - це стандартний спосіб
        Collections.sort(gems, GemComparator.byName());
        System.out.println("\nСортування за назвою:");
        sleep(300);
        for (Gem gem : gems) {
            System.out.println("  - " + gem.getName());
            sleep(200); // невелика затримка між кожним каменем
        }
        sleep(800);
        
        // Сортуємо за вартістю - показуємо скільки карат
        Collections.sort(gems, GemComparator.byValue());
        System.out.println("\nСортування за вартістю (карати):");
        sleep(300);
        for (Gem gem : gems) {
            System.out.printf("  - %s: %.2f карат\n", gem.getName(), gem.getValue());
            sleep(200); // невелика затримка між кожним каменем
        }
    }
    
    /**
     * XSL трансформація
     * Перетворює XML файл за допомогою XSL стилів
     * У нашому випадку групує камені за типом дорогоцінності
     */
    private static void transformXml() throws Exception {
        System.out.println("\n--- XSL Трансформація ---");
        XslTransformer transformer = new XslTransformer();
        transformer.transform(XML_FILE, XSL_FILE, OUTPUT_XML);
        System.out.println("✓ Трансформація завершена!");
        System.out.println("Результат збережено у файл: " + OUTPUT_XML);
    }
}
