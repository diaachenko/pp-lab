package Model.Treat;

import java.io.Serializable;

public abstract class Treat implements Serializable {
    private String name;
    private int weight;
    private int sugarAmount;
    private int volume;

    public Treat(String name, int weight, int sugarAmount, int volume) {
        this.name = name;
        this.weight = weight;
        this.sugarAmount = sugarAmount;
        this.volume = volume;
    }

    public double getSugarPercentage() {
        if (weight == 0) return 0;
        return ((double) sugarAmount / weight) * 100;
    }

    public String getName() { return name; }
    public int getWeight() { return weight; }
    public int getSugarAmount() { return sugarAmount; }
    public int getVolume() { return volume; }

    @Override
    public String toString() {
        return String.format("%s [Вага: %dg, Цукор: %dg]", name, weight, sugarAmount);
    }
}
