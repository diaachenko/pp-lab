package Model.TreatPack;

import Model.Candy.Candy;
import Model.Cardboard.CardboardBox;
import Model.Chocolate.Chocolate;
import Model.Treat.Treat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TreatPackTest {

    private TreatPack pack;
    private CardboardBox container;

    private final int CONTAINER_WEIGHT = 100;
    private final int MAX_VOLUME = 1000;
    private final int MAX_LOAD = 500;

    @BeforeEach
    void setUp() {
        container = new CardboardBox("TestBox", CONTAINER_WEIGHT, MAX_VOLUME, MAX_LOAD);
        pack = new TreatPack("New Year Gift", container);
    }

    // --- 1. (Constructor) ---

    @Test
    @DisplayName("Перевірка початкового стану (порожній пакунок)")
    void testInitialization() {
        assertNotNull(pack.getTreats());
        assertTrue(pack.getTreats().isEmpty(), "Список має бути порожнім");
        assertEquals("New Year Gift", pack.getName());
        assertEquals(CONTAINER_WEIGHT, pack.getBruttoMass(), "Брутто має дорівнювати вазі тари");
        assertEquals(0, pack.getNettoMass(), "Нетто має бути 0");
        assertEquals(MAX_VOLUME, pack.getVolumeLeft(), "Весь об'єм має бути вільним");
    }

    // --- 2. (addTreat) ---

    @Test
    @DisplayName("Успішне додавання цукерки (нормальні умови)")
    void testAddTreatSuccess() {
        Treat treat = new Chocolate("Milka", 100, 50, 100, "Milk", "Oreo", 30);

        boolean result = pack.addTreat(treat);

        assertTrue(result, "Цукерка має додатися успішно");
        assertEquals(1, pack.getTreats().size());
        assertEquals(100, pack.getNettoMass());
        assertEquals(900, pack.getVolumeLeft());
    }

    @Test
    @DisplayName("Помилка додавання: Перевищення Вантажопідйомності (MaxLoad)")
    void testAddTreatFailByWeight() {
        Treat heavyTreat = new Chocolate("Brick", 600, 100, 100, "Dark", "Iron", 99);

        boolean result = pack.addTreat(heavyTreat);

        assertFalse(result, "Має заборонити додавання через перевантаження");
        assertTrue(pack.getTreats().isEmpty());
    }

    @Test
    @DisplayName("Помилка додавання: Недостатньо Об'єму")
    void testAddTreatFailByVolume() {
        Treat bigTreat = new Candy("Air", 10, 5, 1200, "Air");

        boolean result = pack.addTreat(bigTreat);

        assertFalse(result, "Має заборонити додавання через брак місця");
        assertTrue(pack.getTreats().isEmpty());
    }

    @Test
    @DisplayName("Граничний випадок: Заповнення під зав'язку (ідеально влізло)")
    void testAddTreatExactLimits() {
        Treat perfectFit = new Chocolate("Perfect", MAX_LOAD, 100, MAX_VOLUME, "Test", "Perfection", 50);

        boolean result = pack.addTreat(perfectFit);

        assertTrue(result, "Має дозволити, якщо дорівнює ліміту");
        assertEquals(MAX_LOAD, pack.getNettoMass());
        assertEquals(0, pack.getVolumeLeft());
    }

    @Test
    @DisplayName("Накопичувальний ефект: Друга цукерка не влазить")
    void testAddSecondTreatFail() {
        Treat t1 = new Chocolate("First", 300, 50, 500, "Dark", "None", 50);
        pack.addTreat(t1);

        Treat t2 = new Chocolate("Second", 300, 50, 100, "Dark", "NONE", 50);

        boolean result = pack.addTreat(t2);

        assertFalse(result, "Друга цукерка не має влізти за вагою");
        assertEquals(1, pack.getTreats().size(), "Список пустий");
    }

    // --- 3. (removeTreat) ---

    @Test
    @DisplayName("Успішне видалення")
    void testRemoveTreat() {
        Treat t1 = new Candy("C1", 10, 5, 10, "A");
        Treat t2 = new Candy("C2", 10, 5, 10, "B");
        pack.addTreat(t1);
        pack.addTreat(t2);

        pack.removeTreat(0); // Видаляємо першу

        assertEquals(1, pack.getTreats().size());
        assertEquals("C2", pack.getTreats().get(0).getName());
        assertEquals(10, pack.getNettoMass(), "Вага нетто має перерахуватися");
        assertEquals(110, pack.getBruttoMass(), "Вага брутто має перерахуватися");
    }

    @Test
    @DisplayName("Видалення з невірним індексом (не має впасти)")
    void testRemoveInvalidIndex() {
        pack.addTreat(new Candy("C1", 10, 5, 10, "A"));

        assertDoesNotThrow(() -> pack.removeTreat(100)); // Індекс > розміру
        assertDoesNotThrow(() -> pack.removeTreat(-1));  // Від'ємний індекс

        assertEquals(1, pack.getTreats().size(), "Нічого не мало видалитися");
    }

    // --- 4. ТЕСТИ СОРТУВАННЯ ---

    @Test
    @DisplayName("Сортування за вагою")
    void testSortByWeight() {
        Treat light = new Candy("Light", 10, 5, 10, "A");
        Treat heavy = new Candy("Heavy", 100, 50, 50, "B");
        Treat medium = new Candy("Medium", 50, 25, 20, "C");

        pack.addTreat(heavy);
        pack.addTreat(light);
        pack.addTreat(medium);

        pack.sortByWeight();

        List<Treat> sorted = pack.getTreats();
        assertEquals("Light", sorted.get(0).getName());
        assertEquals("Medium", sorted.get(1).getName());
        assertEquals("Heavy", sorted.get(2).getName());
    }

    @Test
    @DisplayName("Сортування за вмістом цукру")
    void testSortBySugar() {
        Treat sweet1 = new Candy("S1", 50, 10, 10, "A");
        Treat sweet2 = new Candy("S2", 50, 40, 10, "B");
        Treat sweet3 = new Candy("S3", 50, 5, 10, "C");

        pack.addTreat(sweet1);
        pack.addTreat(sweet2);
        pack.addTreat(sweet3);

        pack.sortBySugarAmount();

        assertEquals(5, pack.getTreats().get(0).getSugarAmount());
        assertEquals(10, pack.getTreats().get(1).getSugarAmount());
        assertEquals(40, pack.getTreats().get(2).getSugarAmount());
    }

    @Test
    @DisplayName("Сортування за вмістом цукру")
    void testSortBySugarPerc() {
        Treat sweet1 = new Candy("S1", 50, 10, 10, "A");
        Treat sweet2 = new Candy("S2", 50, 40, 10, "B");
        Treat sweet3 = new Candy("S3", 50, 5, 10, "C");

        pack.addTreat(sweet1);
        pack.addTreat(sweet2);
        pack.addTreat(sweet3);

        pack.sortBySugarPercentage();

        assertEquals(5, pack.getTreats().get(0).getSugarAmount());
        assertEquals(10, pack.getTreats().get(1).getSugarAmount());
        assertEquals(40, pack.getTreats().get(2).getSugarAmount());
    }

    // --- 5. ТЕСТИ ПОШУКУ (findTreatsInSugarRange) ---

    @Test
    @DisplayName("Пошук солодощів за діапазоном цукру (знайдено одну цукерку)")
    void testFindTreatsOne() {
        Treat t1 = new Candy("Low", 50, 10, 10, "A");
        Treat t2 = new Candy("Mid", 50, 30, 10, "B");
        Treat t3 = new Candy("High", 50, 50, 10, "C");

        pack.addTreat(t1);
        pack.addTreat(t2);
        pack.addTreat(t3);

        List<Treat> result = pack.findTreatsInSugarRange(20, 40);

        assertEquals(1, result.size());
        assertEquals("Mid", result.get(0).getName());
    }

    @Test
    @DisplayName("Пошук солодощів за діапазоном цукру")
    void testFindTreatsMore() {
        Treat t1 = new Candy("Low", 50, 10, 10, "A");
        Treat t2 = new Candy("Mid", 50, 30, 10, "B");
        Treat t3 = new Candy("High", 50, 50, 10, "C");

        pack.addTreat(t1);
        pack.addTreat(t2);
        pack.addTreat(t3);

        List<Treat> result = pack.findTreatsInSugarRange(5, 32);

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Пошук: нічого не знайдено")
    void testFindTreatsEmptyResult() {
        pack.addTreat(new Candy("Sweet", 50, 50, 10, "A"));

        List<Treat> result = pack.findTreatsInSugarRange(0, 10);

        assertTrue(result.isEmpty());
    }

    // --- 6. ТЕСТ TOSTRING (для покриття) ---
    @Test
    @DisplayName("Перевірка методу toString")
    void testToString() {
        pack.addTreat(new Candy("TestCandy", 10, 5, 10, "Flavor"));

        String info = pack.toString();


        assertNotNull(info, "Рядок не має бути null");
        assertTrue(info.length() > 0, "Рядок не має бути порожнім");

        assertTrue(info.contains("New Year Gift"),
                "Помилка: toString() не містить назву пакунку 'New Year Gift'");

        assertTrue(info.contains("TestCandy"),
                "Помилка: toString() не містить назву цукерки 'TestCandy'. Перевірте toString() у класі Candy/Treat!");
    }

    @Test
    @DisplayName("Перевірка геттерів")
    void testReportingMethods() {
        assertEquals(container, pack.getContainer(),
                "getContainer має повертати об'єкт упаковки");

        assertEquals("TestBox", pack.getContainer().getName());

        assertEquals("0/500г", pack.getLoadInfo(),
                "Для порожнього пакунку інфо має бути 0/Max");

        pack.addTreat(new Chocolate("Test", 100, 50, 50, "Dark", "Bytes", 50));

        assertEquals("100/500г", pack.getLoadInfo(),
                "Інфо про навантаження має оновитися після додавання");
    }
}

