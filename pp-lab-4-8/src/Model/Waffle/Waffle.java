package Model.Waffle;
import Model.Treat.Treat;

public class Waffle extends Treat {
    private String flavourType;

    public Waffle(String name, int weight, int sugarAmount, int volume, String flavour) {
        super(name, weight, sugarAmount, volume);
        this.flavourType = flavour;
    }

    public String getFlavour() {
        return this.flavourType;
    }

    @Override
    public String toString() {
        return String.format("ВАФЛЯ \"%s\" | Вага: %dg | Цукор: %dg | Смак: %s",
                getName(), getWeight(), getSugarAmount(), getFlavour());
    }
}
