package Commands.SelectPackCommand;

import Context.AppContext;
import Utils.AppLogger;
import Utils.InputValidator;
import Commands.Command;
import Commands.PrintAllPacksCommand.*;
import Model.TreatPack.TreatPack;

public class SelectPackCommand implements Command {
    private final AppContext context;
    public SelectPackCommand(AppContext context) { this.context = context; }

    @Override
    public void execute() {
        var packs = context.getPacks();
        if (packs.isEmpty()) {
            System.out.println("Список порожній.");
            return;
        }

        new PrintAllPacksCommand(context).execute();
        
        int idx = InputValidator.getInt(context.getScanner(), 
            "Введіть номер пакунку для роботи: ", 1, packs.size());
            
        TreatPack selected = packs.get(idx - 1);
        context.setCurrentPack(selected);
        System.out.println("Ви обрали пакунок: " + selected.getName());
        AppLogger.info("Користувач переключився на пакунок: \"" + selected.getName() + "\"");
    }
}
