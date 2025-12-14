package Menu;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import Commands.Command;
import Utils.AppLogger;

class MenuInvoker {
	private Map<Integer, Command> commands = new HashMap<>();
	
	public void setCommand(int key, Command command) {
		commands.put(key, command);
	}
	
	public void executeCommand(int key) {
		Command command = commands.get(key);
		if (command != null) {
            AppLogger.info("Користувач викликав команду ID: " + key + " (" + command.getClass().getSimpleName() + ")");
            command.execute();
        }
		else {
            AppLogger.warning("Користувач ввів невірний пункт меню: " + key);
            System.out.println("Такого пункту не існує.");
        }
	}
}

public class Menu {
    private final Scanner scan;
    private final MenuInvoker invoker = new MenuInvoker();
    private final Map<Integer, String> labels = new HashMap<>();
    private final String title;
    private final Boolean is_constant;

    public Menu(String title, Scanner sc, Boolean ic) {
        this.title = title;
        this.scan = sc;
        this.is_constant = ic; 
    }

    public void addItem(int number, String label, Command command) {
        labels.put(number, label);
        invoker.setCommand(number, command);
    }

    public void show() {
        int choice;
        do {
            System.out.println("\n=== " + title + " ===");
            for (var entry : labels.entrySet()) {
                System.out.println(entry.getKey() + ". " + entry.getValue());
            }
            System.out.println("0. Назад");
            System.out.print("Ваш вибір: ");
            choice = Integer.parseInt(scan.nextLine());

            if (choice != 0) {
                invoker.executeCommand(choice);
            }

        } while ((choice != 0) && (this.is_constant == true));
    }
}
