package Commands.AdminCommands;

import Commands.Command;
import Context.AppContext;
import Model.Treat.Treat;
import Utils.AppLogger;
import Utils.InputValidator;

public class RemoveFromDBCommand implements Command {
    private AppContext context;
    public RemoveFromDBCommand(AppContext ctx) { this.context = ctx; }
    @Override public void execute() {
        new ListDBCommand(context).execute();
        var db = context.getTreatDatabase();
        if (db.isEmpty()) return;

        int idx = InputValidator.getInt(context.getScanner(), "Номер для видалення: ", 1, db.size());
        Treat toRemove = db.get(idx - 1);

        context.removeFromDatabase(idx - 1);
        System.out.println("Видалено.");
        AppLogger.info("[ADMIN] Видалено шаблон з бази: " + toRemove.getName());
    }
}