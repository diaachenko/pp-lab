package Model.Cookie;
import Model.Treat.Treat;

public class Cookie extends Treat {
    private String doughType;

    public Cookie(String name, int weight, int sugarAmount, int volume, String crumbleType) {
        super(name, weight, sugarAmount, volume);
        this.doughType = crumbleType;
    }

    public String getDough() {
        return this.doughType;
    }

    @Override
    public String toString() {
        return String.format("ПЕЧИВО \"%s\" | Вага: %dg | Цукор: %dg | Тісто: %s",
                getName(), getWeight(), getSugarAmount(), getDough());
    }
}
