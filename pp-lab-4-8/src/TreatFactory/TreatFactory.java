package TreatFactory;

import java.util.Scanner;
import Model.Treat.Treat;
import Model.Candy.Candy;
import Model.Chocolate.Chocolate;
import Model.Cookie.Cookie;
import Model.Waffle.Waffle;
import Utils.InputValidator;

public class TreatFactory {
    public static Treat createTreat(Scanner sc) {
        System.out.println("\n--- Введення даних солодощів ---");
        System.out.println("1. Цукерка\n2. Шоколад\n3. Печиво\n4. Вафля\n0. Скасувати");
        int type = InputValidator.getInt(sc, "Тип: ", 0, 4);

        if (type == 0) {
            System.out.println("Створення скасовано.");
            return null;
        }

        String name = InputValidator.getString(sc, "Назва: ");
        int weight = InputValidator.getPositiveInt(sc, "Вага (г): ");
        int sugar = InputValidator.getValidSugar(sc, weight);
        int volume = InputValidator.getPositiveInt(sc, "Об'єм (см3): ");

        switch (type) {
            case 1:
                String flav = InputValidator.getString(sc, "Смак: ");
                return new Candy(name, weight, sugar, volume, flav);
            case 2:
                String flavType = InputValidator.getString(sc, "Вид: ");
                String flavChoc = InputValidator.getString(sc, "Смак: ");
                int cocoa = InputValidator.getInt(sc, "Відсоток какао (0-100): ", 0, 100);
                return new Chocolate(name, weight, sugar, volume, flavType, flavChoc, cocoa);
            case 3:
                String crumb = InputValidator.getString(sc, "Тісто: ");
                return new Cookie(name, weight, sugar, volume, crumb);
            case 4:
                String flavour = InputValidator.getString(sc, "Смак: ");
                return new Waffle(name, weight, sugar, volume, flavour);
            default: return null;
        }
    }
}
