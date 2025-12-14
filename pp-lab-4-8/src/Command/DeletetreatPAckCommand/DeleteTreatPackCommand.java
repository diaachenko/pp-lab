package Commands.DeleteTreatPackCommand;

import Commands.Command;
import Context.AppContext;
import Model.TreatPack.TreatPack;
import Utils.AppLogger;
import Utils.InputValidator;

public class DeleteTreatPackCommand implements Command {
    private final AppContext context;

    public DeleteTreatPackCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        TreatPack currentPack = context.getCurrentPack();

        if (currentPack == null) {
            System.out.println("Помилка: Не обрано пакунок для видалення!");
            System.out.println("Спочатку створіть новий або оберіть існуючий (пункт 3 в меню).");
            return;
        }

        System.out.println("Ви збираєтесь видалити поточний пакунок: \"" + currentPack.getName() + "\"");
        String confirm = InputValidator.getString(context.getScanner(), "Ви впевнені? (y/n): ");

        if (confirm.equalsIgnoreCase("y")) {
            context.removePack(currentPack);
            System.out.println("Пакунок видалено, зміни збережено у файл.");
            AppLogger.info("Видалено пакунок.");
        } else {
            System.out.println("Видалення скасовано.");
            AppLogger.info("Користувач скачував видалення");
        }
    }
}
