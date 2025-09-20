package menu;

import java.util.Objects;

public class MenuItem {
    private final String name;
    private final int price;
    private final MenuType type;

    protected MenuItem(String name, int price, MenuType type) {
        this.name = name;
        this.price = price;
        this.type = type;
    }

    public String getInfo() {
        return String.format("%s, %d원", name, price);
    }

    public String getName() {
        return this.name;
    }

    public int getPrice() {
        return this.price;
    }

    public boolean isMainDish() {
        return this.type == MenuType.MainDish;
    }

    public boolean isBeverage() {
        return this.type == MenuType.Beverage;
    }

    public boolean isSideDish() {
        return this.type == MenuType.SideDish;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof MenuItem) {
            return this.name.equals(((MenuItem) obj).name);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
