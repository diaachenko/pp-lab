package Commands;

import Commands.AdminCommands.AdminLoginCommand;
import Commands.CreateTreatPackCommand.CreateTreatPackCommand;
import Commands.DeleteTreatPackCommand.DeleteTreatPackCommand;
import Commands.FindTreatsSugCommand.FindTreatsSugCommand;
import Commands.HelpCommand.HelpCommand;
import Commands.SelectPackCommand.SelectPackCommand;
import Commands.PrintAllPacksCommand.PrintAllPacksCommand;
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

class CommandsTest {

    private AppContext context;
    private final String PACKS_FILE = "all_gift_packs_test.ser";
    private final String DB_FILE = "treats_test_db.ser";

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        new File(PACKS_FILE).delete();
        new File(DB_FILE).delete();

        context = null;
    }

    @AfterEach
    void tearDown() {
        new File(PACKS_FILE).delete();
        new File(DB_FILE).delete();

        System.setOut(originalOut);
    }

    private void prepareContextWithInput(String data) {
        Scanner mockScanner = new Scanner(data);
        context = new AppContext(mockScanner);
        context.getPacks().clear();
        context.getTreatDatabase().clear();
    }

    // --- ТЕСТ 1: Створення пакунку ---
    @Test
    @DisplayName("Команда CreateTreatPack: успішне створення коробки")
    void testCreatePackCommandCardboard() {

        String input = "MyTestPack\n2\nRed\n50\n1000\n500\n";
        prepareContextWithInput(input);

        CreateTreatPackCommand command = new CreateTreatPackCommand(context);
        command.execute();

        assertEquals(1, context.getPacks().size(), "У списку має з'явитися 1 пакунок");
        TreatPack created = context.getPacks().get(0);
        assertEquals("MyTestPack", created.getName());
        assertEquals(500, created.getContainer().getMaxLoad());

        assertNotNull(context.getCurrentPack());
        assertEquals("MyTestPack", context.getCurrentPack().getName());
    }

    @Test
    @DisplayName("Команда CreateTreatPack: успішне створення пакета")
    void testCreatePackCommandPaperBag() {

        String input = "MyTestPack\n1\nRed\n50\n1000\n500\n";
        prepareContextWithInput(input);

        CreateTreatPackCommand command = new CreateTreatPackCommand(context);
        command.execute();

        assertEquals(1, context.getPacks().size(), "У списку має з'явитися 1 пакунок");
        TreatPack created = context.getPacks().get(0);
        assertEquals("MyTestPack", created.getName());
        assertEquals(500, created.getContainer().getMaxLoad());

        assertNotNull(context.getCurrentPack());
        assertEquals("MyTestPack", context.getCurrentPack().getName());
    }

    @Test
    @DisplayName("Команда CreateTreatPack: успішне створення металу")
    void testCreatePackCommandMetal() {

        String input = "MyTestPack\n3\nRed\n50\n1000\n500\n";
        prepareContextWithInput(input);

        CreateTreatPackCommand command = new CreateTreatPackCommand(context);
        command.execute();

        assertEquals(1, context.getPacks().size(), "У списку має з'явитися 1 пакунок");
        TreatPack created = context.getPacks().get(0);
        assertEquals("MyTestPack", created.getName());
        assertEquals(500, created.getContainer().getMaxLoad());

        assertNotNull(context.getCurrentPack());
        assertEquals("MyTestPack", context.getCurrentPack().getName());
    }

    @Test
    @DisplayName("Команда CreateTreatPack: успішне створення банки")
    void testCreatePackCommandJar() {

        String input = "MyTestPack\n4\nRed\n50\n1000\n500\n";
        prepareContextWithInput(input);

        CreateTreatPackCommand command = new CreateTreatPackCommand(context);
        command.execute();

        assertEquals(1, context.getPacks().size(), "У списку має з'явитися 1 пакунок");
        TreatPack created = context.getPacks().get(0);
        assertEquals("MyTestPack", created.getName());
        assertEquals(500, created.getContainer().getMaxLoad());

        assertNotNull(context.getCurrentPack());
        assertEquals("MyTestPack", context.getCurrentPack().getName());
    }

    // --- ТЕСТ 2: Вибір пакунку ---
    @Test
    @DisplayName("Команда SelectPack: вибір зі списку")
    void testSelectPackCommand() {
        String input = "1\n";
        prepareContextWithInput(input);

        TreatPack pack = new TreatPack("ExistingPack", new CardboardBox("Box", 10, 100, 100));
        context.addPack(pack);

        context.setCurrentPack(null);

        SelectPackCommand command = new SelectPackCommand(context);
        command.execute();

        assertEquals(pack, context.getCurrentPack(), "Пакунок має бути обраним");
    }

    @Test
    @DisplayName("Команда SelectPack: вибір з пустого списку")
    void testSelectPackCommandNull() {
        prepareContextWithInput("");

        context.getPacks().clear();
        context.setCurrentPack(null);

        SelectPackCommand command = new SelectPackCommand(context);
        command.execute();

        assertNull(context.getCurrentPack(), "Пакунок не мав обратися, бо список порожній");
    }

    // --- ТЕСТ 3: Видалення пакунку ---
    @Test
    @DisplayName("Команда DeleteTreatPack: видалення поточного")
    void testDeletePackCommand() {
        String input = "y\n";
        prepareContextWithInput(input);

        TreatPack pack = new TreatPack("To Delete", new CardboardBox("Box", 10, 100, 100));
        context.addPack(pack);
        context.setCurrentPack(pack);

        DeleteTreatPackCommand command = new DeleteTreatPackCommand(context);
        command.execute();

        assertTrue(context.getPacks().isEmpty(), "Список має стати порожнім");
        assertNull(context.getCurrentPack(), "Поточний пакунок має бути null");
    }

    @Test
    @DisplayName("Команда DeleteTreatPack: видалення пустого")
    void testDeletePackCommandNull() {
        String input = "y\n";
        prepareContextWithInput(input);

        TreatPack pack = new TreatPack("To Delete", new CardboardBox("Box", 10, 100, 100));
        context.addPack(pack);
        context.setCurrentPack(null);

        DeleteTreatPackCommand command = new DeleteTreatPackCommand(context);
        command.execute();

        assertEquals(1, context.getPacks().size(), "Список має стати порожнім");
    }

    @Test
    @DisplayName("Команда DeleteTreatPack: скасування")
    void testDeletePackCancel() {
        // Імітуємо введення: "n" (відмова)
        String input = "n\n";
        prepareContextWithInput(input);

        TreatPack pack = new TreatPack("Safe Pack", new CardboardBox("Box", 10, 100, 100));
        context.addPack(pack);
        context.setCurrentPack(pack);

        DeleteTreatPackCommand command = new DeleteTreatPackCommand(context);
        command.execute();

        assertEquals(1, context.getPacks().size(), "Пакунок не мав видалитися");
        assertNotNull(context.getCurrentPack());
    }

    // --- ТЕСТ 5: Пошук ---
    @Test
    @DisplayName("Команда FindTreats: перевірка виводу")
    void testFindTreatsCommand() {
        System.setOut(new PrintStream(outContent));

        String input = "20\n40\n";
        prepareContextWithInput(input);

        TreatPack pack = new TreatPack("SearchPack", new CardboardBox("Box", 10, 100, 100));
        pack.addTreat(new Candy("Sweet", 10, 30, 10, "A"));
        pack.addTreat(new Candy("Sour", 10, 10, 10, "B"));
        context.setCurrentPack(pack);

        FindTreatsSugCommand command = new FindTreatsSugCommand(context);
        command.execute();

        String output = outContent.toString();

        assertTrue(output.contains("Знайдено 1 од."), "Мало знайти 1 цукерку");
        assertTrue(output.contains("Sweet"), "У виводі має бути назва 'Sweet'");
        assertFalse(output.contains("Sour"), "У виводі НЕ має бути 'Sour'");
    }

    @Test
    @DisplayName("Команда FindTreats: перевірка виводу")
    void testFindTreatsCommandNull() {
        System.setOut(new PrintStream(outContent));

        String input = "";
        prepareContextWithInput(input);

        context.setCurrentPack(null);

        FindTreatsSugCommand command = new FindTreatsSugCommand(context);
        command.execute();
    }

    @Test
    @DisplayName("Команда FindTreats: перевірка виводу")
    void testFindTreatsCommandZero() {
        System.setOut(new PrintStream(outContent));

        String input = "40\n60\n";
        prepareContextWithInput(input);

        TreatPack pack = new TreatPack("SearchPack", new CardboardBox("Box", 10, 100, 100));
        pack.addTreat(new Candy("Sweet", 10, 30, 10, "A"));
        pack.addTreat(new Candy("Sour", 10, 10, 10, "B"));
        context.setCurrentPack(pack);

        FindTreatsSugCommand command = new FindTreatsSugCommand(context);
        command.execute();

        String output = outContent.toString();

        assertTrue(output.contains("На жаль"), "Мало знайти 1 цукерку");
    }

    // --- ТЕСТ 6: Допомога (перехоплення System.out) ---
    @Test
    void testHelp() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        new HelpCommand().execute();

        String output = outContent.toString();
        assertTrue(output.contains("ДОВІДНИК КОРИСТУВАЧА"));
    }

    @Test
    void testWrongPassword() {
        // Вводимо неправильний пароль
        prepareContextWithInput("wrongPass\n");

        AdminLoginCommand cmd = new AdminLoginCommand(context);
        cmd.execute();

        // Меню не мало відкритися, змін в базі немає
        assertTrue(context.getTreatDatabase().isEmpty());
    }

    @Test
    void testAddTreatToDB() {
        String input = "admin123\n" +
                "2\n" +
                "1\nTestCandy\n10\n5\n10\nTaste\n" +
                "0\n";

        prepareContextWithInput(input);

        new AdminLoginCommand(context).execute();

        assertEquals(1, context.getTreatDatabase().size());
        assertEquals("TestCandy", context.getTreatDatabase().get(0).getName());
    }

    @Test
    void testAddTreatToDBNull() {
        String input = "admin123\n2\n0\n0\n";

        prepareContextWithInput(input);

        new AdminLoginCommand(context).execute();

        assertEquals(0, context.getTreatDatabase().size());
    }

    @Test
    void testRemoveTreatFromDB() {
        prepareContextWithInput("");
        context.addToDatabase(new Candy("ToDelete", 10, 5, 10, "A"));

        String input = "admin123\n3\n1\n0\n";
        context = new AppContext(new Scanner(input));

        new AdminLoginCommand(context).execute();

        assertTrue(context.getTreatDatabase().isEmpty(), "База має стати порожньою");
    }

    @Test
    void testRemoveTreatFromEmptyDB() {
        String input = "admin123\n3\n1\n0\n";
        context = new AppContext(new Scanner(input));

        new AdminLoginCommand(context).execute();

        assertTrue(context.getTreatDatabase().isEmpty(), "База має стати порожньою");
    }

    @Test
    void testListDB() {
        prepareContextWithInput("admin123\n1\n0\n");
        context.addToDatabase(new Candy("ViewMe", 10, 5, 10, "A"));

        assertDoesNotThrow(() -> new AdminLoginCommand(context).execute());
    }

    @Test
    @DisplayName("Команда PrintAllPacks")
    void testPrintEmpty() {
        String input = "";
        prepareContextWithInput(input);

        PrintAllPacksCommand command = new PrintAllPacksCommand(context);
        command.execute();

        assertTrue(context.getPacks().isEmpty(), "Список має стати порожнім");
        assertNull(context.getCurrentPack(), "Поточний пакунок має бути null");
    }
}

