package Utils;

import Model.Candy.Candy;
import Model.Cardboard.CardboardBox;
import Model.Treat.Treat;
import Model.TreatPack.TreatPack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class InputValidatorTest {

    private Scanner scannerWithInput(String input) {
        return new Scanner(input);
    }

    @Test
    @DisplayName("getInt: Успішне введення з першого разу")
    void testGetIntSuccess() {
        Scanner sc = scannerWithInput("5\n");
        int result = InputValidator.getInt(sc, "Enter: ", 1, 10);
        assertEquals(5, result);
    }

    @Test
    @DisplayName("getInt: Введення тексту, потім правильного числа")
    void testGetIntInvalidFormat() {
        Scanner sc = scannerWithInput("abc\n5\n");

        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(new ByteArrayOutputStream()));

        int result = InputValidator.getInt(sc, "Enter: ", 1, 10);

        System.setOut(originalOut);
        assertEquals(5, result);
    }

    @Test
    @DisplayName("getInt: Число поза діапазоном, потім правильне")
    void testGetIntOutOfRange() {
        Scanner sc = scannerWithInput("100\n0\n5\n");

        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(new ByteArrayOutputStream()));

        int result = InputValidator.getInt(sc, "Enter: ", 1, 10);

        System.setOut(originalOut);
        assertEquals(5, result);
    }

    @Test
    @DisplayName("getPositiveInt: Успішне введення")
    void testGetPositiveInt() {
        Scanner sc = scannerWithInput("10\n");
        int result = InputValidator.getPositiveInt(sc, "Enter: ");
        assertEquals(10, result);
    }

    @Test
    @DisplayName("getString: Пропуск порожніх рядків")
    void testGetString() {
        Scanner sc = scannerWithInput("   \n\nHello\n");

        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(new ByteArrayOutputStream()));

        String result = InputValidator.getString(sc, "Enter: ");

        System.setOut(originalOut);
        assertEquals("Hello", result);
    }

    @Test
    @DisplayName("getValidSugar: Валідація відносно ваги")
    void testGetValidSugar() {
        Scanner sc = scannerWithInput("150\n50\n");

        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(new ByteArrayOutputStream()));

        int result = InputValidator.getValidSugar(sc, 100);

        System.setOut(originalOut);
        assertEquals(50, result);
    }
}

class DataManagerTest {

    private final String PACKS_FILE = "all_gift_packs.ser";
    private final String DB_FILE = "treats_db.ser";

    @BeforeEach
    @AfterEach
    void cleanUp() {
        deleteFileOrFolder(new File(PACKS_FILE));
        deleteFileOrFolder(new File(DB_FILE));
    }

    private void deleteFileOrFolder(File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) f.delete();
        }
        file.delete();
    }

    @Test
    @DisplayName("Збереження та завантаження Пакунків (Happy Path)")
    void testSaveAndLoadPacks() {
        List<TreatPack> packs = new ArrayList<>();
        packs.add(new TreatPack("Pack1", new CardboardBox("Box", 10, 100, 100)));

        DataManager.savePacks(packs);

        assertTrue(new File(PACKS_FILE).exists());

        List<TreatPack> loaded = DataManager.loadPacks();

        assertNotNull(loaded);
        assertEquals(1, loaded.size());
        assertEquals("Pack1", loaded.get(0).getName());
    }

    @Test
    @DisplayName("Збереження та завантаження Бази Солодощів")
    void testSaveAndLoadTreatsDB() {
        List<Treat> treats = new ArrayList<>();
        treats.add(new Candy("Candy1", 10, 5, 5, "A"));

        DataManager.saveTreatsDB(treats);

        assertTrue(new File(DB_FILE).exists());

        List<Treat> loaded = DataManager.loadTreatsDB();
        assertEquals(1, loaded.size());
        assertEquals("Candy1", loaded.get(0).getName());
    }

    @Test
    @DisplayName("Завантаження відсутнього файлу (має повернути порожній список)")
    void testLoadMissingFile() {
        List<TreatPack> loaded = DataManager.loadPacks();

        assertNotNull(loaded);
        assertTrue(loaded.isEmpty());
    }

    @Test
    @DisplayName("Помилка збереження (IOException catch block)")
    void testSaveError() {
        File folderTrap = new File(DB_FILE);
        folderTrap.mkdir();

        List<Treat> treats = new ArrayList<>();
        treats.add(new Candy("Test", 10, 10, 10, "A"));

        assertDoesNotThrow(() -> DataManager.saveTreatsDB(treats));

        assertTrue(folderTrap.isDirectory());
    }

    @Test
    @DisplayName("Завантаження пошкодженого файлу (має обробити помилку)")
    void testLoadCorruptFile() throws IOException {
        try (FileWriter writer = new FileWriter(PACKS_FILE)) {
            writer.write("I am not a serialized object, I am just text.");
        }

        List<TreatPack> loaded = DataManager.loadPacks();

        assertNotNull(loaded);
        assertTrue(loaded.isEmpty(), "Має повернути пустий список при помилці зчитування");
    }
}
