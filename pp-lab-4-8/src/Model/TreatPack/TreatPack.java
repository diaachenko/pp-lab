package Model.TreatPack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import Model.Treat.Treat;
import Model.Package.GiftContainer;
import java.io.Serializable;

public class TreatPack implements Serializable {
    private String name;
    private List<Treat> contains;
    private GiftContainer container;

    public TreatPack(String name, GiftContainer container) {
        this.name = name;
        this.container = container;
        this.contains = new ArrayList<>();
    }

    public boolean addTreat(Treat treat) {
        if (getVolumeLeft() < treat.getVolume()) {
            System.out.println("[Помилка]: Недостатньо місця! Потрібно: " + treat.getVolume() + ", Є: " + getVolumeLeft());
            return false;
        }

        int currentNetto = getNettoMass();
        if (currentNetto + treat.getWeight() > container.getMaxLoad()) {
            System.out.println("[Помилка]: Упаковка не витримає! Макс. навантаження: "
                    + container.getMaxLoad() + "г. Зараз: " + currentNetto + "г. Ви хочете додати: " + treat.getWeight() + "г.");
            return false;
        }

        contains.add(treat);
        return true;
    }

    public void removeTreat(int index) {
        if (index >= 0 && index < contains.size()) {
            contains.remove(index);
        }
    }

    public int getNettoMass() {
        return contains.stream().mapToInt(Treat::getWeight).sum();
    }

    public int getBruttoMass() {
        return getNettoMass() + container.getWeight();
    }

    public int getOccupiedVolume() {
        return contains.stream().mapToInt(Treat::getVolume).sum();
    }

    public int getVolumeLeft() {
        return container.getMaxVolume() - getOccupiedVolume();
    }

    public int getWeightLeft() { return container.getMaxLoad() - getNettoMass(); }
    
	public String getName() {
		return name;
	}

    public GiftContainer getContainer() {
        return this.container;
    }

    public List<Treat> getTreats() {
        return contains;
    }

    public void sortByWeight() {
        contains.sort(Comparator.comparingInt(Treat::getWeight));
    }

    public void sortBySugarAmount() {
        contains.sort(Comparator.comparingInt(Treat::getSugarAmount));
    }

    public void sortBySugarPercentage() {
        contains.sort(Comparator.comparingDouble(Treat::getSugarPercentage));
    }

    public String getLoadInfo() {
        return getNettoMass() + "/" + container.getMaxLoad() + "г";
    }

    public List<Treat> findTreatsInSugarRange(int min, int max) {
        return contains.stream()
                .filter(t -> t.getSugarAmount() >= min && t.getSugarAmount() <= max)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Подарунок \"").append(this.getName()).append("\";\n");
        sb.append("Упаковка: ");
        sb.append("Вага (Нетто/Брутто): ").append(getNettoMass()).append("/").append(getBruttoMass()).append(" г\n");
        sb.append("Лишилось місця (см3): ").append(getVolumeLeft()).append(" cм3\n");
        sb.append("Доступна вага (г): ").append(getWeightLeft()).append(" г\n");
        sb.append("Вміст:\n");
        for (int i = 0; i < contains.size(); i++) {
            sb.append(i + 1).append(". ").append(contains.get(i)).append("\n");
        }
        return sb.toString();
    }
}
