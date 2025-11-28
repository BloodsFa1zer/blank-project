# Інструкції з тестування проекту

## Передумови

Переконайтеся, що у вас встановлено:
- Java 11 або вище
- Maven 3.6+

Перевірте версію Java:
```bash
java -version
```

Перевірте версію Maven:
```bash
mvn -version
```

## 1. Компіляція проекту

Спочатку скомпілюйте проект:

```bash
mvn clean compile
```

Ця команда:
- Очистить попередні збірки (`clean`)
- Скомпілює всі Java файли (`compile`)
- Завантажить необхідні залежності з Maven репозиторію

**Очікуваний результат:** Повідомлення `BUILD SUCCESS`

## 2. Запуск Unit-тестів

Запустіть всі тести:

```bash
mvn test
```

Або запустіть тести з детальним виводом:

```bash
mvn test -X
```

### Що тестується:

- ✅ **Парсери** (SAX, DOM, StAX) - перевірка коректного парсингу XML
- ✅ **Валідація XML** - перевірка валідації проти XSD схеми
- ✅ **XSL трансформація** - перевірка перетворення XML
- ✅ **Компаратори** - перевірка сортування за різними критеріями
- ✅ **Модель даних** - перевірка створення та роботи об'єктів

**Очікуваний результат:** Всі тести проходять успішно

### Запуск конкретного тесту

Щоб запустити тільки один тестовий клас:

```bash
mvn test -Dtest=GemTest
```

Або конкретний тестовий метод:

```bash
mvn test -Dtest=GemTest#testGemCreation
```

## 3. Запуск основного додатку

Запустіть головний клас додатку:

```bash
mvn exec:java -Dexec.mainClass="main.Main"
```

Або спочатку створіть JAR файл:

```bash
mvn clean package
java -cp target/diamond-fund-lab-1.0-SNAPSHOT.jar main.Main
```

**Що відбувається:**
1. Валідація XML файлу проти XSD схеми
2. Парсинг XML за допомогою трьох парсерів (SAX, DOM, StAX)
3. Сортування каменів за назвою та вагою
4. XSL трансформація XML файлу

**Очікуваний результат:** 
- В консолі ви побачите логи з інформацією про парсинг
- Створиться файл `target/transformed-gems.xml` з перетвореним XML

## 4. Перевірка результатів

### Перевірка логів

Логи зберігаються у файлі:
```
logs/diamond-fund.log
```

Перегляньте логи:
```bash
cat logs/diamond-fund.log
```

### Перевірка трансформованого XML

Після запуску додатку перевірте створений файл:
```bash
cat target/transformed-gems.xml
```

Файл повинен містити XML з групуванням каменів за типом дорогоцінності.

## 5. Ручне тестування компонентів

### Тестування парсерів окремо

Створіть простий тестовий клас або використайте існуючі тести:

```java
import parser.SaxGemParser;
import model.Gem;
import java.util.List;

SaxGemParser parser = new SaxGemParser();
List<Gem> gems = parser.parse("src/main/resources/xml/gems.xml");
System.out.println("Parsed " + gems.size() + " gems");
```

### Тестування валідації

```java
import validator.XmlValidator;

XmlValidator validator = new XmlValidator();
boolean isValid = validator.validate(
    "src/main/resources/xml/gems.xml",
    "src/main/resources/xsd/gems.xsd"
);
System.out.println("XML is valid: " + isValid);
```

### Тестування сортування

```java
import comparator.GemComparator;
import java.util.Collections;

Collections.sort(gems, GemComparator.byName());
// або
Collections.sort(gems, GemComparator.byValue());
```

## 6. Тестування з невалідним XML

Щоб перевірити валідацію, створіть тестовий невалідний XML файл:

1. Створіть файл `test-invalid.xml` з помилками (наприклад, без обов'язкового атрибута `id`)
2. Запустіть валідацію:
```java
XmlValidator validator = new XmlValidator();
boolean isValid = validator.validate("test-invalid.xml", "src/main/resources/xsd/gems.xsd");
// Очікується: false
```

## 7. Перевірка покриття тестами

Для перевірки покриття коду тестами (потребує додаткового плагіну):

```bash
mvn clean test jacoco:report
```

Звіт буде доступний у: `target/site/jacoco/index.html`

## 8. Вирішення проблем

### Помилка: "Cannot find symbol"
**Причина:** Неправильні imports або не скомпільований проект
**Рішення:** 
```bash
mvn clean compile
```

### Помилка: "FileNotFoundException"
**Причина:** Неправильні шляхи до файлів
**Рішення:** Переконайтеся, що ви запускаєте з кореня проекту

### Помилка: "XML validation failed"
**Причина:** XML не відповідає XSD схемі
**Рішення:** Перевірте XML файл на відповідність схемі

### Тести не знаходять файли
**Причина:** Тести шукають файли відносно поточного каталогу
**Рішення:** Запускайте тести з кореня проекту:
```bash
cd /path/to/project
mvn test
```

## 9. Додаткові команди

### Очищення проекту
```bash
mvn clean
```

### Створення JAR файлу
```bash
mvn clean package
```

### Пропуск тестів під час збірки
```bash
mvn clean package -DskipTests
```

### Запуск з детальним виводом
```bash
mvn clean compile -X
```

## 10. Перевірка структури проекту

Переконайтеся, що структура проекту правильна:

```
src/
├── main/
│   ├── java/
│   │   ├── Main.java
│   │   ├── model/
│   │   ├── parser/
│   │   ├── validator/
│   │   ├── transformer/
│   │   └── comparator/
│   └── resources/
│       ├── xml/
│       ├── xsd/
│       └── xsl/
└── test/
    └── java/
        ├── model/
        ├── parser/
        ├── validator/
        ├── transformer/
        └── comparator/
```

## Корисні посилання

- [Maven Documentation](https://maven.apache.org/guides/)
- [JUnit 5 Documentation](https://junit.org/junit5/docs/current/user-guide/)
- [Log4j2 Documentation](https://logging.apache.org/log4j/2.x/manual/index.html)

