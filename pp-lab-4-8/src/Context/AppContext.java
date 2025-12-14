package Context;

import Model.TreatPack.TreatPack;
import Model.Treat.Treat;
import Utils.DataManager;
import java.util.List;
import java.util.Scanner;

public class AppContext {
    private List<TreatPack> packs;
    private List<Treat> treatDatabase; // База солодощів
    private TreatPack currentPack;
    private Scanner scanner;

    public AppContext(Scanner scanner) {
        this.scanner = scanner;
        this.packs = DataManager.loadPacks();
        this.treatDatabase = DataManager.loadTreatsDB(); // Завантаження бази
        System.out.println("Система завантажена.");
        System.out.println("Наборів: " + packs.size() + ", Солодощів у базі: " + treatDatabase.size());
    }

    public List<TreatPack> getPacks() { return packs; }

    public List<Treat> getTreatDatabase() { return treatDatabase; }

    public void addToDatabase(Treat treat) {
        treatDatabase.add(treat);
        DataManager.saveTreatsDB(treatDatabase);
    }
    public void removeFromDatabase(int index) {
        if (index >= 0 && index < treatDatabase.size()) {
            treatDatabase.remove(index);
            DataManager.saveTreatsDB(treatDatabase);
        }
    }

    public void addPack(TreatPack pack) {
        packs.add(pack);
        saveChanges();
    }

    public void removePack(TreatPack pack) {
        packs.remove(pack);
        if (currentPack == pack) currentPack = null;
        saveChanges();
    }

    public TreatPack getCurrentPack() { return currentPack; }
    public void setCurrentPack(TreatPack currentPack) { this.currentPack = currentPack; }
    public Scanner getScanner() { return scanner; }

    public void saveChanges() {
        DataManager.savePacks(packs);
    }
}
