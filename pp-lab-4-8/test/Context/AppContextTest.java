package Context;

import Model.Candy.Candy;
import Model.Cardboard.CardboardBox;
import Model.Treat.Treat;
import Model.TreatPack.TreatPack;
import Utils.DataManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class AppContextTest {

    private AppContext context;
    private final String PACKS_FILE = "all_gift_packs.ser";
    private final String DB_FILE = "treats_db.ser";

    @BeforeEach
    void setUp() {
        cleanFiles();

        context = new AppContext(new Scanner("test input"));
    }

    @AfterEach
    void tearDown() {
        cleanFiles();
    }

    private void cleanFiles() {
        new File(PACKS_FILE).delete();
        new File(DB_FILE).delete();
    }

    @Test
    @DisplayName("Ініціалізація: Списки не null, навіть без файлів")
    void testInitialization() {
        assertNotNull(context.getPacks(), "Список пакунків не має бути null");
        assertTrue(context.getPacks().isEmpty(), "Список пакунків має бути порожнім на старті");

        assertNotNull(context.getTreatDatabase(), "База солодощів не має бути null");
        assertTrue(context.getTreatDatabase().isEmpty());

        assertNotNull(context.getScanner(), "Сканер має бути збережений");
        assertNull(context.getCurrentPack(), "Поточний пакунок має бути null");
    }

    @Test
    @DisplayName("Ініціалізація: Завантаження вже існуючих даних з файлів")
    void testLoadExistingData() {
        TreatPack oldPack = new TreatPack("OldPack", new CardboardBox("Box", 10, 100, 100));
        List<TreatPack> preSavedPacks = new ArrayList<>();
        preSavedPacks.add(oldPack);

        Treat oldCandy = new Candy("OldCandy", 10, 5, 5, "Taste");
        List<Treat> preSavedDB = new ArrayList<>();
        preSavedDB.add(oldCandy);

        DataManager.savePacks(preSavedPacks);
        DataManager.saveTreatsDB(preSavedDB);

        AppContext restoredContext = new AppContext(new Scanner(""));

        assertNotNull(restoredContext.getPacks());
        assertEquals(1, restoredContext.getPacks().size(), "Мав завантажитися 1 пакунок");
        assertEquals("OldPack", restoredContext.getPacks().get(0).getName());

        assertNotNull(restoredContext.getTreatDatabase());
        assertEquals(1, restoredContext.getTreatDatabase().size(), "Мала завантажитися 1 цукерка в базу");
        assertEquals("OldCandy", restoredContext.getTreatDatabase().get(0).getName());
    }

    @Test
    @DisplayName("addPack: Додавання та збереження")
    void testAddPack() {
        TreatPack pack = new TreatPack("Pack1", new CardboardBox("Box", 10, 100, 100));

        context.addPack(pack);

        assertEquals(1, context.getPacks().size());
        assertEquals("Pack1", context.getPacks().get(0).getName());
        assertTrue(new File(PACKS_FILE).exists(), "Файл мав бути створений після додавання");
    }

    @Test
    @DisplayName("removePack: Видалення НЕактивного пакунку")
    void testRemoveInactivePack() {
        TreatPack pack1 = new TreatPack("Pack1", new CardboardBox("Box", 10, 100, 100));
        TreatPack pack2 = new TreatPack("Pack2", new CardboardBox("Box", 10, 100, 100));

        context.addPack(pack1);
        context.addPack(pack2);

        context.setCurrentPack(pack2);

        context.removePack(pack1);

        assertEquals(1, context.getPacks().size());
        assertEquals(pack2, context.getCurrentPack(), "Поточний пакунок не мав змінитися");
    }

    @Test
    @DisplayName("removePack: Видалення АКТИВНОГО пакунку (скидання currentPack)")
    void testRemoveActivePack() {
        TreatPack pack1 = new TreatPack("Pack1", new CardboardBox("Box", 10, 100, 100));
        context.addPack(pack1);

        context.setCurrentPack(pack1);
        assertNotNull(context.getCurrentPack());

        context.removePack(pack1);

        assertTrue(context.getPacks().isEmpty());
        assertNull(context.getCurrentPack(), "Поточний пакунок мав стати null, бо ми його видалили");
    }

    @Test
    @DisplayName("Database: Додавання та збереження")
    void testAddToDatabase() {
        Treat candy = new Candy("Sweet", 10, 5, 5, "Flavor");

        context.addToDatabase(candy);

        assertEquals(1, context.getTreatDatabase().size());
        assertTrue(new File(DB_FILE).exists(), "Файл бази мав бути створений");
    }

    @Test
    @DisplayName("Database: Успішне видалення")
    void testRemoveFromDatabase() {
        Treat candy1 = new Candy("C1", 10, 5, 5, "A");
        Treat candy2 = new Candy("C2", 10, 5, 5, "B");

        context.addToDatabase(candy1);
        context.addToDatabase(candy2);

        context.removeFromDatabase(0);

        assertEquals(1, context.getTreatDatabase().size());
        assertEquals("C2", context.getTreatDatabase().get(0).getName());
    }

    @Test
    @DisplayName("Database: Видалення з некоректним індексом (не має впасти)")
    void testRemoveFromDatabaseInvalidIndex() {
        context.addToDatabase(new Candy("C1", 10, 5, 5, "A"));

        // Спроба видалити індекс -1
        context.removeFromDatabase(-1);
        assertEquals(1, context.getTreatDatabase().size(), "Нічого не мало видалитися");

        // Спроба видалити індекс 100
        context.removeFromDatabase(100);
        assertEquals(1, context.getTreatDatabase().size(), "Нічого не мало видалитися");
    }

    @Test
    @DisplayName("saveChanges: Примусове збереження")
    void testSaveChanges() {
        context.saveChanges();
        assertTrue(new File(PACKS_FILE).exists());
    }
}
