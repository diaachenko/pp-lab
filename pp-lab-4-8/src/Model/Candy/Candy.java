package Model.Candy;

import Model.Treat.Treat;

public class Candy extends Treat {
    private String flavour;

    public Candy(String name, int weight, int sugarAmount, int volume, String flavour) {
        super(name, weight, sugarAmount, volume);
        this.flavour = flavour;
    }

    public String getFlavour() {
        return this.flavour;
    }

    @Override
    public String toString() {
        return String.format("ЦУКЕРКА \"%s\" | Вага: %dg | Цукор: %dg | Смак: %s",
                getName(), getWeight(), getSugarAmount(), getFlavour());
    }
}
