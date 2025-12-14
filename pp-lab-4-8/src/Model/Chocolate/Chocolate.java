package Model.Chocolate;

import Model.Treat.Treat;

public class Chocolate extends Treat {
    private String type;
    private String flavour;
    private int cocoaPercentage;

    public Chocolate(String name, int weight, int sugarAmount, int volume, String type, String flavour, int cocoaPercentage) {
        super(name, weight, sugarAmount, volume);
        this.type = type;
        this.flavour = flavour;
        this.cocoaPercentage = cocoaPercentage;
    }

    public String getFlavour() {
        return this.flavour;
    }

    public int getCocoa() {
        return this.cocoaPercentage;
    }

    public String getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return String.format("ШОКОЛАД \"%s\" | Вага: %dg | Цукор: %dg | Тип: %s | Смак: %s | Вміст какао: %dg%%",
                getName(), getWeight(), getSugarAmount(), getType(), getFlavour(), getCocoa());
    }
}
