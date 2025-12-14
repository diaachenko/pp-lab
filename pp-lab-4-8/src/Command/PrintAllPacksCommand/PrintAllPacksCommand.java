package Commands.PrintAllPacksCommand;

import Context.AppContext;
import Commands.Command;
import Model.TreatPack.TreatPack;

public class PrintAllPacksCommand implements Command {
    private AppContext context;

    public PrintAllPacksCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        var packs = context.getPacks();
        if (packs.isEmpty()) {
            System.out.println("\nСписок пакунків порожній.");
            return;
        }

        System.out.println("\n=== СПИСОК УСІХ НАБОРІВ ===");
        System.out.printf("%-3s %-20s %-12s %-12s %-15s %-10s%n",
                "№", "Назва", "Вага брутто ", "Вага нетто", "Навантаження", "К-сть");
        System.out.println("-----------------------------------------------------------------------");

        for (int i = 0; i < packs.size(); i++) {
            TreatPack p = packs.get(i);

            System.out.printf("%-3d %-20s %-12d %-12d %-15s %-10d%n",
                    (i + 1),
                    p.getName(),
                    p.getBruttoMass(),
                    p.getNettoMass(),
                    p.getLoadInfo(),
                    p.getTreats().size()
            );
        }
        System.out.println("-----------------------------------------------------------------------");
    }
}
