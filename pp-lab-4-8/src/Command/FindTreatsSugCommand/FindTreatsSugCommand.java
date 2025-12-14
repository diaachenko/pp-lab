package Commands.FindTreatsSugCommand;

import Commands.Command;
import Context.AppContext;
import Utils.InputValidator;
import Model.Treat.Treat;
import java.util.List;
import java.util.Scanner;

public class FindTreatsSugCommand implements Command {
    private final AppContext context;

    public FindTreatsSugCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        if (context.getCurrentPack() == null) {
            System.out.println("\n[Помилка]: Не обрано пакунок для пошуку!");
            System.out.println("Будь ласка, спочатку оберіть пакунок (пункт 3 у головному меню).");
            return;
        }

        System.out.println("\n--- ПОШУК СОЛОДОЩІВ У ПАКУНКУ \"" + context.getCurrentPack().getName() + "\" ---");

        Scanner sc = context.getScanner();
        int min = InputValidator.getPositiveInt(sc, "Введіть мінімум цукру (г): ");
        int max = InputValidator.getInt(sc, "Введіть максимум цукру (г): ", min, Integer.MAX_VALUE);

        List<Treat> results = context.getCurrentPack().findTreatsInSugarRange(min, max);

        if (results.isEmpty()) {
            System.out.println("На жаль, солодощів з таким вмістом цукру не знайдено.");
        } else {
            System.out.println("Знайдено " + results.size() + " од.:");
            for (Treat t : results) {
                System.out.println(" - " + t);
            }
        }
    }
}
