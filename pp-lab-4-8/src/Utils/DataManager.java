package Utils;

import Model.TreatPack.TreatPack;
import Model.Treat.Treat;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static final String PACKS_FILE = "all_gift_packs.ser";
    private static final String TREATS_DB_FILE = "treats_db.ser";

    public static void savePacks(List<TreatPack> packs) {
        saveObject(packs, PACKS_FILE);
        AppLogger.info("Файл пакунків успішно збережено.");
    }

    public static List<TreatPack> loadPacks() {
        return (List<TreatPack>) loadObject(PACKS_FILE);
    }

    public static void saveTreatsDB(List<Treat> treats) {
        saveObject(treats, TREATS_DB_FILE);
    }

    public static List<Treat> loadTreatsDB() {
        return (List<Treat>) loadObject(TREATS_DB_FILE);
    }

    private static void saveObject(Object obj, String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(obj);
        } catch (IOException e) {
            System.out.println("[Error]: Не вдалося зберегти дані у " + fileName + ": " + e.getMessage());
        }
    }

    private static Object loadObject(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
}