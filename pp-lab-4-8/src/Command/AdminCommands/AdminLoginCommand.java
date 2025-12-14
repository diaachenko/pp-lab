package Commands.AdminCommands;

import Commands.Command;
import Context.AppContext;
import Menu.Menu;
import Utils.AppLogger;
import Utils.InputValidator;

public class AdminLoginCommand implements Command {
    private final AppContext context;
    private final String ADMIN_PASS = "admin123";

    public AdminLoginCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        System.out.println("\n--- ВХІД ДЛЯ АДМІНІСТРАТОРА ---");
        String inputPass = InputValidator.getString(context.getScanner(), "Введіть пароль: ");

        if (!inputPass.equals(ADMIN_PASS)) {
            System.out.println("Невірний пароль! Доступ заборонено.");
            AppLogger.warning("Невдала спроба входу в Адмін-панель (Невірний пароль).");
            return;
        }

        AppLogger.info("Успішний вхід в Адмін-панель.");
        Menu adminMenu = new Menu("АДМІН ПАНЕЛЬ (БАЗА СОЛОДОЩІВ)", context.getScanner(), true);
        adminMenu.addItem(1, "Переглянути базу солодощів", new ListDBCommand(context));
        adminMenu.addItem(2, "Додати новий шаблон солодощів", new AddToDBCommand(context));
        adminMenu.addItem(3, "Видалити шаблон", new RemoveFromDBCommand(context));
        adminMenu.show();
    }
}