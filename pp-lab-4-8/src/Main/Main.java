package Main;

import java.util.Scanner;

import Commands.FindTreatsSugCommand.FindTreatsSugCommand;
import Commands.HelpCommand.HelpCommand;
import Menu.Menu;
import Context.AppContext;
import Commands.CreateTreatPackCommand.CreateTreatPackCommand;
import Commands.DeleteTreatPackCommand.DeleteTreatPackCommand;
import Commands.AdminCommands.AdminLoginCommand;
import Commands.PrintAllPacksCommand.PrintAllPacksCommand;
import Commands.SelectPackCommand.SelectPackCommand;
import Commands.UpdateTreatPackCommand.UpdateTreatPackCommand;
import Utils.AppLogger;

public class Main {
    public static void main(String[] args) {
        try {
            AppLogger.setup();
            Scanner scanner = new Scanner(System.in);
            AppContext context = new AppContext(scanner);

            Menu startMenu = new Menu("ГОЛОВНЕ МЕНЮ", scanner, true);

            startMenu.addItem(1, "Створити новий пакунок", new CreateTreatPackCommand(context));
            startMenu.addItem(2, "Показати всі пакунки", new PrintAllPacksCommand(context));
            startMenu.addItem(3, "Вибрати пакунок для роботи", new SelectPackCommand(context));
            startMenu.addItem(4, "Оновити поточний пакунок", new UpdateTreatPackCommand(context));
            startMenu.addItem(5, "Видалити поточний пакунок", new DeleteTreatPackCommand(context));
            startMenu.addItem(6, "Пошук цукерок за діапазоном цукру", new FindTreatsSugCommand(context));
            startMenu.addItem(7, "Адмін-панель", new AdminLoginCommand(context));
            startMenu.addItem(8, "Допомога", new HelpCommand());

            startMenu.show();

            System.out.println("\nРоботу програми завершено.");
            scanner.close();
            AppLogger.info("Програму завершено користувачем.");

        } catch (Exception e) {
            System.out.println("Сталася критична помилка. Адміністратора повідомлено.");
            AppLogger.severe("Критичний збій у Main: " + e.getMessage(), e);
        }
    }
}
