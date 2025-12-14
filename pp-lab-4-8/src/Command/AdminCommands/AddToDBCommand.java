package Commands.AdminCommands;

import Commands.Command;
import Context.AppContext;
import Model.Treat.Treat;
import TreatFactory.TreatFactory;
import Utils.AppLogger;

public class AddToDBCommand implements Command {
    private AppContext context;
    public AddToDBCommand(AppContext ctx) { this.context = ctx; }
    @Override public void execute() {
        Treat t = TreatFactory.createTreat(context.getScanner());
        if (t != null) {
            context.addToDatabase(t);
            System.out.println("Солодощі збережено в базу даних.");
            AppLogger.info("[ADMIN] Додано новий шаблон у базу: " + t.getName() + " (" + t.getClass().getSimpleName() + ")");
        }
    }
}
