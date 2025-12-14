package Commands.AdminCommands;

import Commands.Command;
import Context.AppContext;

public class ListDBCommand implements Command {
    private AppContext context;
    public ListDBCommand(AppContext ctx) { this.context = ctx; }
    @Override public void execute() {
        var db = context.getTreatDatabase();
        if (db.isEmpty()) {
            System.out.println("База порожня.");
            return;
        }
        System.out.println("\n--- БАЗА ШАБЛОНІВ СОЛОДОЩІВ ---");
        for (int i = 0; i < db.size(); i++) {
            System.out.println((i+1) + ". " + db.get(i));
        }
    }
}