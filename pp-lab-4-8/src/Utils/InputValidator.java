package Utils;

import java.util.Scanner;

public class InputValidator {
    public static int getInt(Scanner sc, String prompt, int min, int max) {
        int input;
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine();
            try {
                input = Integer.parseInt(line);
                if (input >= min && input <= max) {
                    return input;
                } else {
                    System.out.println("Помилка: число має бути в межах від " + min + " до " + max);
                }
            } catch (NumberFormatException e) {
                System.out.println("Помилка: введіть коректне ціле число.");
            }
        }
    }

    public static int getPositiveInt(Scanner sc, String prompt) {
        return getInt(sc, prompt, 0, Integer.MAX_VALUE);
    }

    public static String getString(Scanner sc, String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = sc.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Помилка: рядок не може бути порожнім.");
        }
    }

    public static int getValidSugar(Scanner sc, int weight) {
        while (true) {
            int sugar = getPositiveInt(sc, "Вміст цукру (г): ");
            if (sugar <= weight) {
                return sugar;
            }
            System.out.println("Помилка: Вміст цукру (" + sugar + "г) не може перевищувати загальну вагу (" + weight + "г)!");
        }
    }
}