# Лабораторна робота 2: Обробка XML документів

## Опис проекту

Проект реалізує систему обробки XML-документів для "Алмазного фонду" - колекції дорогоцінних та напівдорогоцінних каменів. Система підтримує парсинг XML за допомогою трьох різних парсерів (SAX, DOM, StAX), валідацію за XSD схемою, XSL трансформації та сортування об'єктів.

## Структура проекту

```
.
├── pom.xml                                    # Maven конфігурація
├── README.md                                  # Документація
├── TESTING.md                                 # Інструкції з тестування
├── .gitignore                                # Git ignore файл
└── src/
    ├── main/
    │   ├── java/
    │   │   ├── Main.java                     # Головний клас додатку
    │   │   ├── model/                        # Модельні класи
    │   │   │   ├── Gem.java
    │   │   │   ├── Preciousness.java
    │   │   │   ├── VisualParameters.java
    │   │   │   └── Color.java
    │   │   ├── parser/                       # Парсери XML
    │   │   │   ├── GemParser.java
    │   │   │   ├── SaxGemParser.java
    │   │   │   ├── DomGemParser.java
    │   │   │   └── StaxGemParser.java
    │   │   ├── validator/                    # Валідація XML
    │   │   │   └── XmlValidator.java
    │   │   ├── transformer/                  # XSL трансформації
    │   │   │   └── XslTransformer.java
    │   │   └── comparator/                   # Компаратори для сортування
    │   │       └── GemComparator.java
    │   └── resources/
    │       ├── xsd/
    │       │   └── gems.xsd                  # XSD схема
    │       ├── xml/
    │       │   └── gems.xml                  # XML файл з даними
    │       ├── xsl/
    │       │   └── transform.xsl             # XSL трансформація
    │       └── log4j2.xml                   # Конфігурація логування
    └── test/
        └── java/
            ├── parser/                        # Тести парсерів
            ├── validator/                     # Тести валідації
            ├── transformer/                   # Тести трансформацій
            ├── comparator/                    # Тести компараторів
            └── model/                         # Тести моделі
```

## Вимоги

- Java 11 або вище
- Maven 3.6+

## Встановлення та запуск

### Збірка проекту

```bash
mvn clean compile
```

### Запуск додатку

```bash
mvn exec:java -Dexec.mainClass="main.Main"
```

Або:

```bash
mvn clean package
java -cp target/diamond-fund-lab-1.0-SNAPSHOT.jar main.Main
```

### Детальні інструкції з тестування

Дивіться файл [TESTING.md](TESTING.md) для детальних інструкцій з тестування проекту.

### Запуск тестів

```bash
mvn test
```

## Особливості реалізації

### XSD Схема (gems.xsd)

- **Прості типи**: `preciousnessType`, `colorType`, `transparencyType`, `facetsType`, `valueType`
- **Комплексні типи**: `visualParametersType`, `gemType`
- **Переліки (enumerations)**: для `preciousness` та `color`
- **Граничні значення**: 
  - `transparency`: 0-100%
  - `facets`: 4-15 граней
  - `value`: > 0 каратів
- **Атрибути**: `id` типу `ID` (обов'язковий)

### XML Парсери

Проект реалізує три типи парсерів:

1. **SAX Parser** (`SaxGemParser`) - потоковий парсер, ефективний для великих файлів
2. **DOM Parser** (`DomGemParser`) - завантажує весь документ в пам'ять, зручний для навігації
3. **StAX Parser** (`StaxGemParser`) - pull-парсер, баланс між продуктивністю та зручністю

### Валідація XML

Клас `XmlValidator` виконує валідацію XML-файлу проти XSD схеми перед обробкою.

### XSL Трансформація

XSL стиль (`transform.xsl`) перетворює початковий XML, групуючи камені за типом дорогоцінності:
- `PreciousGems` - дорогоцінні камені
- `SemiPreciousGems` - напівдорогоцінні камені

### Сортування

Реалізовано кілька компараторів в класі `GemComparator`:
- `byName()` - сортування за назвою
- `byValue()` - сортування за вагою (карати)
- `byOrigin()` - сортування за місцем видобутку
- `byPreciousnessAndValue()` - сортування за типом та вагою

### Логування

Використовується Log4j2 для логування:
- Консольний вивід
- Файл логів: `logs/diamond-fund.log`
- Рівень логування налаштований в `log4j2.xml`

## Приклад використання

```java
import parser.SaxGemParser;
import parser.GemParser;
import model.Gem;
import validator.XmlValidator;
import comparator.GemComparator;
import transformer.XslTransformer;
import java.util.Collections;
import java.util.List;

// Парсинг XML за допомогою SAX
GemParser parser = new SaxGemParser();
List<Gem> gems = parser.parse("src/main/resources/xml/gems.xml");

// Валідація XML
XmlValidator validator = new XmlValidator();
boolean isValid = validator.validate("gems.xml", "gems.xsd");

// Сортування
Collections.sort(gems, GemComparator.byName());

// XSL трансформація
XslTransformer transformer = new XslTransformer();
transformer.transform("gems.xml", "transform.xsl", "output.xml");
```

## Тестування

Всі компоненти покриті Unit-тестами:
- Тести для всіх трьох парсерів
- Тести валідації
- Тести XSL трансформації
- Тести компараторів
- Тести моделі даних

## Git Репозиторій

### Ініціалізація репозиторію

```bash
git init
git add .
git commit -m "Initial commit: Laboratory work 2 - XML processing"
```

### Приклад commit повідомлень

```bash
git commit -m "feat: Add XSD schema with simple and complex types"
git commit -m "feat: Implement SAX, DOM, and StAX parsers"
git commit -m "feat: Add XML validation with XSD"
git commit -m "feat: Implement XSL transformation for grouping by preciousness"
git commit -m "feat: Add Comparator implementations for sorting"
git commit -m "test: Add unit tests for all components"
git commit -m "docs: Update README with project documentation"
```

### Приклад вихідного XML після XSL трансформації

Після виконання XSL трансформації, XML буде перетворено на структуру з групуванням за типом дорогоцінності:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<GemsByPreciousness xmlns="http://www.diamondfund.ua/gems">
    <PreciousGems>
        <gem id="gem001">...</gem>
        <gem id="gem002">...</gem>
        <gem id="gem003">...</gem>
        <gem id="gem006">...</gem>
    </PreciousGems>
    <SemiPreciousGems>
        <gem id="gem004">...</gem>
        <gem id="gem005">...</gem>
        <gem id="gem007">...</gem>
    </SemiPreciousGems>
</GemsByPreciousness>
```

## Автор

Лабораторна робота 2 - Обробка XML документів
