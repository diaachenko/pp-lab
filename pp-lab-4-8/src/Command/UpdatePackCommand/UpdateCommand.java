package Commands.UpdateTreatPackCommand;

import java.util.List;
import Commands.Command;
import Menu.Menu;
import Context.AppContext;
import Utils.AppLogger;
import Utils.InputValidator;
import Model.Treat.Treat;
import Model.TreatPack.TreatPack;

public class UpdateTreatPackCommand implements Command {
    private final AppContext context;
    private final Menu menu;

    public UpdateTreatPackCommand(AppContext context) {
        this.context = context;
        this.menu = new Menu("МЕНЮ РЕДАГУВАННЯ ПАКУНКУ", context.getScanner(), true);
        menu.addItem(1, "Додати солодощі", new AddTreatCommand(context));
        menu.addItem(2, "Видалити солодощі", new DeleteTreatCommand(context));
        menu.addItem(3, "Відсортувати вміст", new SortTreatsCommand(context));
        menu.addItem(4, "Показати поточний стан пакунку", new ShowPackCommand(context));
    }

    @Override
    public void execute() {
        if (context.getCurrentPack() == null) {
            System.out.println("Помилка: Ви не обрали пакунок для редагування!");
            System.out.println("Створіть новий або виберіть зі списку всіх наборів.");
            return;
        }
        System.out.println("Редагування пакунку: " + context.getCurrentPack().getName());
        menu.show();
    }
}

class AddTreatCommand implements Command {
    private final AppContext context;

    public AddTreatCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        if (context.getCurrentPack() == null) {
            System.out.println("Помилка: Не обрано пакунок для редагування.");
            return;
        }

        List<Treat> db = context.getTreatDatabase();
        if (db.isEmpty()) {
            System.out.println("\n[Увага]: База даних солодощів порожня!");
            System.out.println("Будь ласка, зайдіть в Адмін-панель (пункт 7) та додайте товари.");
            return;
        }

        System.out.println("\n--- ДОСТУПНІ СОЛОДОЩІ ---");
        System.out.printf("%-3s %-20s %-10s %-10s %-10s%n", "№", "Назва", "Вага", "Об'єм", "Цукор");
        System.out.println("------------------------------------------------------------");

        for (int i = 0; i < db.size(); i++) {
            Treat t = db.get(i);
            System.out.printf("%-3d %-20s %-10d %-10d %-10d%n",
                    (i + 1), t.getName(), t.getWeight(), t.getVolume(), t.getSugarAmount());
        }
        System.out.println("------------------------------------------------------------");
        System.out.println("0. Скасувати");

        int choice = InputValidator.getInt(context.getScanner(), "Введіть номер товару: ", 0, db.size());
        if (choice == 0) return;

        Treat selectedTreat = db.get(choice - 1);

        int quantity = InputValidator.getPositiveInt(context.getScanner(), "Введіть кількість (шт): ");
        if (quantity == 0) return;

        long totalWeightToAdd = (long) selectedTreat.getWeight() * quantity;
        long totalVolumeToAdd = (long) selectedTreat.getVolume() * quantity;

        int currentWeight = context.getCurrentPack().getNettoMass();
        int maxLoad = context.getCurrentPack().getContainer().getMaxLoad();
        int volumeLeft = context.getCurrentPack().getVolumeLeft();

        if (totalVolumeToAdd > volumeLeft) {
            int maxByVol = volumeLeft / selectedTreat.getVolume();
            System.out.println("\n[Помилка]: Недостатньо місця для " + quantity + " шт.");
            System.out.println("Потрібно об'єму: " + totalVolumeToAdd + " см3");
            System.out.println("Вільний об'єм:   " + volumeLeft + " см3");
            System.out.println("Максимум можна додати: " + maxByVol + " шт.");
            return;
        }

        if (currentWeight + totalWeightToAdd > maxLoad) {
            int availableWeight = maxLoad - currentWeight;
            int maxByWeight = availableWeight / selectedTreat.getWeight();

            System.out.println("\n[Помилка]: Упаковка не витримає таку вагу!");
            System.out.println("Ви хочете додати: " + totalWeightToAdd + " г");
            System.out.println("Доступний ліміт:  " + availableWeight + " г");
            System.out.println("Максимум можна додати: " + maxByWeight + " шт.");
            return;
        }

        boolean success = true;
        for (int i = 0; i < quantity; i++) {
            if (!context.getCurrentPack().addTreat(selectedTreat)) {
                success = false;
                break;
            }
        }

        if (success) {
            System.out.println("Успішно додано " + quantity + " шт. \"" + selectedTreat.getName() + "\"!");
            context.saveChanges();
            AppLogger.info("Додано солодощі: " + selectedTreat.getName() + " x" + quantity + " у пакунок " + context.getCurrentPack().getName());
        } else {
            System.out.println("Сталася непередбачена помилка при додаванні.");
            AppLogger.warning("Невдала спроба додавання (помилка циклу) для товару: " + selectedTreat.getName());
        }
    }
}

class DeleteTreatCommand implements Command {
    private final AppContext context;
    public DeleteTreatCommand(AppContext context) { this.context = context; }

    @Override
    public void execute() {
        TreatPack pack = context.getCurrentPack();
        if (pack == null) {
            System.out.println("Пакунок відсутній.");
            return;
        }
        System.out.println(pack);
        int count = pack.getTreats().size();
        if (count == 0) {
            System.out.println("Пакунок порожній.");
            return;
        }

        int idx = InputValidator.getInt(context.getScanner(), "Введіть номер для видалення: ", 1, count);
        Treat t = pack.getTreats().get(idx - 1);
        String tName = t.getName();

        pack.removeTreat(idx - 1);
        context.saveChanges();

        System.out.println("Видалено.");
        AppLogger.info("Видалено солодощі \"" + tName + "\" з пакунку \"" + pack.getName() + "\"");
    }
}

class ShowPackCommand implements Command {
    private final AppContext context;
    public ShowPackCommand(AppContext context) { this.context = context; }
    @Override public void execute() { System.out.println(context.getCurrentPack()); }
}

class SortTreatsCommand implements Command {
 private final Menu menu;
 private final AppContext context;

 public SortTreatsCommand(AppContext context) {
     this.context = context;
     this.menu = new Menu("Відсортувати за...", context.getScanner(), false);
     menu.addItem(1, "Вагою", () -> {
         context.getCurrentPack().sortByWeight();
         System.out.println("Відсортовано за вагою.");
         context.saveChanges();
     });
     menu.addItem(2, "Вмістом цукру (г)", () -> {
         context.getCurrentPack().sortBySugarAmount();
         System.out.println("Відсортовано за кількістю цукру.");
         context.saveChanges();
     });
     menu.addItem(3, "Вмістом цукру (%)", () -> {
         context.getCurrentPack().sortBySugarPercentage();
         System.out.println("Відсортовано за % цукру.");
         context.saveChanges();
     });
 }
 @Override public void execute() { 
	 menu.show(); 
	 }
}

class SortByWeight implements Command {
	AppContext context;
	
	public SortByWeight(AppContext context) {
		this.context = context;
	}
	
    @Override
    public void execute() {
        TreatPack pack = context.getCurrentPack();
        pack.sortByWeight();
    }
}

class SortBySugar implements Command {
	AppContext context;
	
	public SortBySugar(AppContext context) {
		this.context = context;
	}
	
    @Override
    public void execute() {
        TreatPack pack = context.getCurrentPack();
        pack.sortBySugarAmount();
    }
}

class SortBySugarPercent implements Command {
	AppContext context;
	
	public SortBySugarPercent(AppContext context) {
		this.context = context;
	}
	
    @Override
    public void execute() {
        TreatPack pack = context.getCurrentPack();
        pack.sortBySugarPercentage();
    }
}
