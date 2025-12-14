package TreatFactory;

import Model.Candy.Candy;
import Model.Chocolate.Chocolate;
import Model.Cookie.Cookie;
import Model.Treat.Treat;
import Model.Waffle.Waffle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class TreatFactoryTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    private Scanner scannerWithInput(String input) {
        return new Scanner(input);
    }

    @Test
    @DisplayName("Створення Цукерки (Case 1)")
    void testCreateCandy() {
        String input = "1\nRomashka\n15\n10\n12\nRum\n";

        Treat result = TreatFactory.createTreat(scannerWithInput(input));

        assertNotNull(result);
        assertTrue(result instanceof Candy, "Має створитися об'єкт Candy");
        assertEquals("Romashka", result.getName());
        assertEquals(15, result.getWeight());
        assertTrue(result.toString().contains("Rum"));
    }

    @Test
    @DisplayName("Створення Шоколаду (Case 2)")
    void testCreateChocolate() {
        String input = "2\nMilka\n100\n50\n80\nMilk\nOreo\n30\n";

        Treat result = TreatFactory.createTreat(scannerWithInput(input));

        assertNotNull(result);
        assertEquals("Milka", result.getName());
        assertEquals(50, result.getSugarAmount());
        assertTrue(result.toString().contains("Milka")); // Перевірка специфічного поля
    }

    @Test
    @DisplayName("Створення Печива (Case 3)")
    void testCreateCookie() {
        String input = "3\nOreo\n50\n20\n40\nCrunchy\n";

        Treat result = TreatFactory.createTreat(scannerWithInput(input));

        assertNotNull(result);
        assertTrue(result instanceof Cookie);
        assertEquals("Oreo", result.getName());
        assertTrue(result.toString().contains("Crunchy"));
    }

    @Test
    @DisplayName("Створення Вафлі (Case 4)")
    void testCreateWaffle() {
        String input = "4\nArtek\n30\n15\n60\nLemon\n";

        Treat result = TreatFactory.createTreat(scannerWithInput(input));

        assertNotNull(result);
        assertTrue(result instanceof Waffle);
        assertEquals("Artek", result.getName());
        assertTrue(result.toString().contains("Lemon"));
    }

    @Test
    @DisplayName("Скасування створення (Case 0)")
    void testCancelCreation() {
        String input = "0\n";

        Treat result = TreatFactory.createTreat(scannerWithInput(input));

        assertNull(result, "При виборі 0 метод має повернути null");

        assertTrue(outContent.toString().contains("Створення скасовано"));
    }

    @Test
    @DisplayName("Валідація цукру: Спроба ввести більше за вагу")
    void testSugarValidationLogic() {
        String input = "1\nTest\n100\n150\n50\n10\nA\n";

        Treat result = TreatFactory.createTreat(scannerWithInput(input));

        assertNotNull(result);
        assertEquals(100, result.getWeight());
        assertEquals(50, result.getSugarAmount(), "Має взяти друге (валідне) значення цукру");
    }
}
