package Commands.UpdateTreatPackCommand;

import Context.AppContext;
import Model.Candy.Candy;
import Model.Cardboard.CardboardBox;
import Model.TreatPack.TreatPack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class UpdateTreatPackTest {

    private AppContext context;
    private TreatPack pack;
    private final String PACKS_FILE = "all_gift_packs.ser";
    private final String DB_FILE = "treats_db.ser";

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        new File(PACKS_FILE).delete();
        new File(DB_FILE).delete();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        new File(PACKS_FILE).delete();
        new File(DB_FILE).delete();
        System.setOut(originalOut);
    }

    private void prepareContext(String input) {
        context = new AppContext(new Scanner(input));
        context.getPacks().clear();
        context.getTreatDatabase().clear();

        pack = new TreatPack("TestPack", new CardboardBox("Box", 100, 1000, 500));
        context.addPack(pack);
        context.setCurrentPack(pack);
    }

    // --- 1. ТЕСТИ ДЛЯ UpdateTreatPackCommand (Головне меню) ---

    @Test
    @DisplayName("UpdateMenu: Запуск і вихід")
    void testUpdateMenuExecution() {
        prepareContext("4\n0\n");

        new UpdateTreatPackCommand(context).execute();

        String output = outContent.toString();
        assertTrue(output.contains("МЕНЮ РЕДАГУВАННЯ ПАКУНКУ"));
        assertTrue(output.contains("TestPack"));
    }

    @Test
    @DisplayName("UpdateMenu: Помилка, якщо пакунок не обрано")
    void testUpdateMenuNoPack() {
        prepareContext("");
        context.setCurrentPack(null);

        new UpdateTreatPackCommand(context).execute();

        assertTrue(outContent.toString().contains("Помилка: Ви не обрали пакунок"));
    }

    // --- 2. ТЕСТИ ДЛЯ AddTreatCommand ---

    @Test
    @DisplayName("AddTreat: Успішне додавання")
    void testAddTreatSuccess() {
        prepareContext("1\n5\n");

        context.addToDatabase(new Candy("DbCandy", 10, 5, 10, "A"));

        new AddTreatCommand(context).execute();

        assertEquals(5, pack.getTreats().size());
        assertEquals(50, pack.getNettoMass());
        assertTrue(outContent.toString().contains("Успішно додано 5 шт"));
    }

    @Test
    @DisplayName("AddTreat: Скасування вибору (ввід 0)")
    void testAddTreatCancel() {
        prepareContext("0\n");
        context.addToDatabase(new Candy("A", 10, 5, 10, "A"));

        new AddTreatCommand(context).execute();

        assertTrue(pack.getTreats().isEmpty());
    }

    @Test
    @DisplayName("AddTreat: Порожня база")
    void testAddTreatEmptyDB() {
        prepareContext("");

        new AddTreatCommand(context).execute();

        assertTrue(outContent.toString().contains("База даних солодощів порожня"));
    }

    @Test
    @DisplayName("AddTreat: Помилка 'Недостатньо місця' (Об'єм)")
    void testAddTreatFailVolume() {
        prepareContext("1\n100\n");

        context.addToDatabase(new Candy("Air", 1, 1, 20, "A"));

        new AddTreatCommand(context).execute();

        assertTrue(pack.getTreats().isEmpty());
        assertTrue(outContent.toString().contains("Недостатньо місця"));
        assertTrue(outContent.toString().contains("Потрібно об'єму: 2000"));
    }

    @Test
    @DisplayName("AddTreat: Помилка 'Перевантаження' (MaxLoad)")
    void testAddTreatFailWeight() {
        prepareContext("1\n10\n");

        context.addToDatabase(new Candy("Heavy", 60, 1, 10, "A"));

        new AddTreatCommand(context).execute();

        assertTrue(pack.getTreats().isEmpty());
        assertTrue(outContent.toString().contains("Упаковка не витримає"));
        assertTrue(outContent.toString().contains("Ви хочете додати: 600"));
    }

    @Test
    @DisplayName("AddTreat: Не обрано пакунок")
    void testAddTreatNoPack() {
        prepareContext("");
        context.setCurrentPack(null);

        new AddTreatCommand(context).execute();

        assertTrue(outContent.toString().contains("Не обрано пакунок"));
    }

    @Test
    @DisplayName("AddTreat: Введено 0 кількість")
    void testAddTreatZeroQuantity() {
        prepareContext("1\n0\n"); // Товар 1, кількість 0
        context.addToDatabase(new Candy("C", 10, 5, 10, "A"));

        new AddTreatCommand(context).execute();

        assertTrue(pack.getTreats().isEmpty());
    }

    // --- 3. ТЕСТИ ДЛЯ DeleteTreatCommand ---

    @Test
    @DisplayName("DeleteTreat: Успішне видалення")
    void testDeleteTreatSuccess() {
        prepareContext("1\n"); // Видалити 1-й елемент
        pack.addTreat(new Candy("ToDel", 10, 5, 10, "A"));

        new DeleteTreatCommand(context).execute();

        assertTrue(pack.getTreats().isEmpty());
        assertTrue(outContent.toString().contains("Видалено"));
    }

    @Test
    @DisplayName("DeleteTreat: Порожній пакунок")
    void testDeleteTreatEmpty() {
        prepareContext("");
        new DeleteTreatCommand(context).execute();

        assertTrue(outContent.toString().contains("Пакунок порожній"));
    }

    @Test
    @DisplayName("DeleteTreat: Пакунок відсутній")
    void testDeleteTreatNoPack() {
        prepareContext("");
        context.setCurrentPack(null);
        new DeleteTreatCommand(context).execute();

        assertTrue(outContent.toString().contains("Пакунок відсутній"));
    }

    // --- 4. ТЕСТИ ДЛЯ SortTreatsCommand (Меню) ---

    @Test
    @DisplayName("SortByWeight: Прямий виклик")
    void testSortByWeightDirect() {
        prepareContext("");
        pack.addTreat(new Candy("Heavy", 100, 10, 10, ""));
        pack.addTreat(new Candy("Light", 10, 10, 10, ""));

        SortByWeight command = new SortByWeight(context);
        command.execute();

        assertEquals("Light", pack.getTreats().get(0).getName());
        assertEquals("Heavy", pack.getTreats().get(1).getName());
    }

    @Test
    @DisplayName("SortBySugar: Прямий виклик")
    void testSortBySugarDirect() {
        prepareContext("");
        pack.addTreat(new Candy("Sweet", 100, 100, 10, ""));
        pack.addTreat(new Candy("Diet", 100, 5, 10, ""));

        SortBySugar command = new SortBySugar(context);
        command.execute();

        assertEquals("Diet", pack.getTreats().get(0).getName());
    }

    @Test
    @DisplayName("SortBySugarPercent: Прямий виклик")
    void testSortBySugarPercentDirect() {
        prepareContext("");
        pack.addTreat(new Candy("50Percent", 100, 50, 10, ""));
        pack.addTreat(new Candy("10Percent", 100, 10, 10, ""));

        SortBySugarPercent command = new SortBySugarPercent(context);
        command.execute();

        assertEquals("10Percent", pack.getTreats().get(0).getName());
    }

    @Test
    @DisplayName("SortTreatsCommand: За вагою")
    void testSortMenuWrapperWeig() {
        String input = "1\n";
        prepareContext(input);
        pack.addTreat(new Candy("Heavy", 100, 1, 1, ""));
        pack.addTreat(new Candy("Light", 10, 1, 1, ""));

        SortTreatsCommand menuCommand = new SortTreatsCommand(context);
        menuCommand.execute();

        assertEquals("Light", pack.getTreats().get(0).getName());
    }

    @Test
    @DisplayName("SortTreatsCommand: За вагою цукру")
    void testSortMenuWrapperSugAm() {
        String input = "2\n";
        prepareContext(input);
        pack.addTreat(new Candy("Sweet", 100, 10, 1, ""));
        pack.addTreat(new Candy("Diet", 100, 1, 1, ""));

        SortTreatsCommand menuCommand = new SortTreatsCommand(context);
        menuCommand.execute();

        assertEquals("Diet", pack.getTreats().get(0).getName());
    }

    @Test
    @DisplayName("SortTreatsCommand: За відсотком цукру")
    void testSortMenuWrapperSugPerc() {
        String input = "3\n";
        prepareContext(input);
        pack.addTreat(new Candy("Sweet", 100, 10, 1, ""));
        pack.addTreat(new Candy("Diet", 100, 1, 1, ""));

        SortTreatsCommand menuCommand = new SortTreatsCommand(context);
        menuCommand.execute();

        assertEquals("Diet", pack.getTreats().get(0).getName());
    }

    @Test
    @DisplayName("Сортування порожнього пакунку (не має впасти)")
    void testSortEmptyPack() {
        prepareContext("");

        assertDoesNotThrow(() -> new SortByWeight(context).execute());
        assertDoesNotThrow(() -> new SortBySugar(context).execute());
    }

    // --- 5. ТЕСТ ДЛЯ ShowPackCommand ---

    @Test
    @DisplayName("ShowPackCommand")
    void testShowPack() {
        prepareContext("");
        new ShowPackCommand(context).execute();
        assertTrue(outContent.toString().contains("TestPack"));
    }
}
