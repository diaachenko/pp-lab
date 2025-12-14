package Commands.CreateTreatPackCommand;
import java.util.Scanner;
import Commands.Command;
import Model.Paper.PaperBag;
import Model.Cardboard.CardboardBox;
import Model.MetalBox.MetalBox;
import Model.Glass.Jar;
import Model.TreatPack.TreatPack;
import Context.AppContext;
import Utils.AppLogger;
import Utils.InputValidator;

public class CreateTreatPackCommand implements Command {
	
	private AppContext context;
	
		public CreateTreatPackCommand(AppContext context) {
	        this.context = context;
	    }

	    @Override
	    public void execute() {
	        Scanner scanner = context.getScanner();
	        System.out.println("--- Створення нового пакунку ---");
	        System.out.print("Введіть назву подарунка: ");
	        String name = scanner.nextLine();

	        System.out.println("Оберіть тип упаковки:");
	        System.out.println("1. Паперовий пакет (Paper Bag)");
	        System.out.println("2. Картонна коробка (Cardboard Box)");
	        System.out.println("3. Металева коробка (Metal Box)");
	        System.out.println("4. Банка (Jar)");
            int type = InputValidator.getInt(scanner, "Вибір: ", 1, 4);

	        System.out.print("Введіть колір упаковки: ");
	        String color = scanner.nextLine();
            int weight = InputValidator.getPositiveInt(scanner, "Введіть вагу упаковки (г): ");
            int volume = InputValidator.getPositiveInt(scanner, "Введіть об'єм упаковки (см3): ");
            int maxLoad = InputValidator.getPositiveInt(scanner, "Введіть макс. вантажопідйомність (г): ");

	        Model.Package.GiftContainer pkg = null;
	        switch (type) {
	            case 1 -> pkg = new PaperBag(color, weight, volume, maxLoad);
	            case 2 -> pkg = new CardboardBox(color, weight, volume, maxLoad);
	            case 3 -> pkg = new MetalBox(color, weight, volume, maxLoad);
	            case 4 -> pkg = new Jar(color, weight, volume, maxLoad);
	        }

	        TreatPack newPack = new TreatPack(name, pkg);

	        context.addPack(newPack);
	        context.setCurrentPack(newPack);

	        System.out.println("Пакунок створено, додано до списку та обрано для редагування!");
            AppLogger.info("Створено новий пакунок: \"" + name + "\" (Тип: " + pkg.getClass().getSimpleName() + ")");
	    }
	}
