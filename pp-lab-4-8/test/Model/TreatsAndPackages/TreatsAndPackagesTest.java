package Model.TreatsAndPackages;

import Model.Candy.Candy;
import Model.Cardboard.CardboardBox;
import Model.Chocolate.Chocolate;
import Model.Cookie.Cookie;
import Model.Glass.Jar;
import Model.MetalBox.MetalBox;
import Model.Package.GiftContainer;
import Model.Paper.PaperBag;
import Model.Treat.Treat;
import Model.Waffle.Waffle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TreatHierarchyTest {

    @Test
    @DisplayName("Treat: Розрахунок відсотка цукру")
    void testSugarPercentage() {
        Treat treat = new Candy("Test", 100, 20, 10, "Flavor");

        assertEquals(20.0, treat.getSugarPercentage(), 0.01,
                "Відсоток цукру має бути 20%");
    }

    @Test
    @DisplayName("Treat: Розрахунок відсотка цукру (Вага 0 - захист від ділення на нуль)")
    void testSugarPercentageZeroWeight() {
        Treat treat = new Candy("Air", 0, 0, 10, "Flavor");

        assertEquals(0.0, treat.getSugarPercentage(),
                "При вазі 0 відсоток має бути 0");
    }

    @Test
    @DisplayName("Treat: Геттери базових полів")
    void testBaseGetters() {
        Treat treat = new Candy("BaseName", 50, 25, 100, "F");

        assertEquals("BaseName", treat.getName());
        assertEquals(50, treat.getWeight());
        assertEquals(25, treat.getSugarAmount());
        assertEquals(100, treat.getVolume());
    }

    @Test
    @DisplayName("Treat: тестуємо ToString")
    void testBaseToString() {
        Treat treat = new Candy("BaseName", 50, 25, 100, "F");

        String output = treat.toString();

        assertTrue(output.contains("BaseName"), "У виводі має бути назва солодощів");
    }

    // --- ТЕСТИ СПАДКОЄМЦІВ ---

    @Test
    @DisplayName("Candy: Специфічні поля та toString")
    void testCandy() {
        Candy candy = new Candy("Romashka", 15, 10, 12, "Rum");

        String info = candy.toString();
        assertTrue(info.contains("Romashka"));
        assertTrue(info.contains("15"));

        assertTrue(info.contains("ЦУКЕРКА"));
        assertTrue(info.contains("Rum"));
    }

    @Test
    @DisplayName("Chocolate: Специфічні поля та toString")
    void testChocolate() {
        Chocolate choco = new Chocolate("Milka", 100, 50, 80, "Milk", "Caramel", 30);

        String info = choco.toString();

        assertTrue(info.contains("Milka"));
        assertTrue(info.contains("Caramel"));
        assertTrue(info.contains("ШОКОЛАД") || info.contains("Шоколад"));
        assertTrue(info.contains("30"));
    }

    @Test
    @DisplayName("Cookie: Специфічні поля та toString")
    void testCookie() {
        Cookie cookie = new Cookie("Oreo", 50, 20, 40, "Crunchy");

        String info = cookie.toString();

        assertTrue(info.contains("Oreo"));
        assertTrue(info.contains("ПЕЧИВО") || info.contains("Печиво"));
        assertTrue(info.contains("Crunchy"));
    }

    @Test
    @DisplayName("Waffle: Специфічні поля та toString")
    void testWaffle() {
        Waffle waffle = new Waffle("Artek", 30, 15, 60, "Lemon");

        String info = waffle.toString();

        assertTrue(info.contains("Artek"));
        assertTrue(info.contains("ВАФЛЯ") || info.contains("Вафля"));
        assertTrue(info.contains("Lemon"));
    }
}

class PackageHierarchyTest {

    @Test
    @DisplayName("PaperBag: Перевірка конструктора та геттерів")
    void testPaperBag() {
        PaperBag bag = new PaperBag("Eco Bag", 10, 1000, 500);

        assertEquals("Eco Bag", bag.getName());
        assertEquals(10, bag.getWeight(), "Вага тари некоректна");
        assertEquals(1000, bag.getMaxVolume(), "Об'єм некоректний");
        assertEquals(500, bag.getMaxLoad(), "Вантажопідйомність некоректна");
    }

    @Test
    @DisplayName("CardboardBox: Перевірка конструктора")
    void testCardboardBox() {
        CardboardBox box = new CardboardBox("Post Box", 50, 2000, 1000);

        assertEquals("Post Box", box.getName());
        assertEquals(50, box.getWeight());
        assertEquals(2000, box.getMaxVolume());
        assertEquals(1000, box.getMaxLoad());
    }

    @Test
    @DisplayName("MetalBox: Перевірка конструктора")
    void testMetalBox() {
        MetalBox box = new MetalBox("Tin Can", 150, 500, 2000);

        assertEquals("Tin Can", box.getName());
        assertEquals(150, box.getWeight());
        assertEquals(500, box.getMaxVolume());
        assertEquals(2000, box.getMaxLoad());
    }

    @Test
    @DisplayName("Jar: Перевірка конструктора")
    void testJar() {
        Jar jar = new Jar("Glass Jar", 300, 1000, 5000);

        assertEquals("Glass Jar", jar.getName());
        assertEquals(300, jar.getWeight());
        assertEquals(1000, jar.getMaxVolume());
        assertEquals(5000, jar.getMaxLoad());
    }

    @Test
    @DisplayName("GiftContainer: Перевірка методу toString")
    void testToString() {
        GiftContainer container = new CardboardBox("TestBox", 10, 100, 50);

        String info = container.toString();

        assertNotNull(info);
        assertTrue(info.contains("TestBox"));
        assertTrue(info.contains("100")); // Об'єм
        assertTrue(info.contains("10"));  // Вага
        assertTrue(info.contains("50"));  // MaxLoad
    }
}
