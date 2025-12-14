package Model.Package;

import java.io.Serializable;

public abstract class GiftContainer implements Serializable {
    private String name;
    private int maxVolume;
    private int weight;
    private int maxLoad;

    public GiftContainer(String name, int weight, int maxVolume, int maxLoad) {
        this.name = name;
        this.maxVolume = maxVolume;
        this.weight = weight;
        this.maxLoad = maxLoad;
    }

    public String getName() {
        return name;
    }

    public int getMaxVolume() {
        return maxVolume;
    }

    public int getWeight() {
        return weight;
    }

    public int getMaxLoad() {
        return maxLoad;
    }

    @Override
    public String toString() {
        return String.format("%s (Об'єм: %d см3, Тара: %dg, Витримує: %dg)", name, maxVolume, weight, maxLoad);
    }
}
