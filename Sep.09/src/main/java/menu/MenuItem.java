package menu;

import java.util.Objects;

public class MenuItem {
    private final String name;
    private final int price;
    private final MenuType type;
    private final Stock stock;

    protected MenuItem(String name, int price, MenuType type, int initStock) {
        this.name = name;
        this.price = price;
        this.type = type;
        this.stock = new Stock(initStock);
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

    public boolean isEnoughStock(int needed) {
        return this.stock.isMoreThan(needed);
    }

    public void sell(int purchaseQuantity) {
        stock.decrease(purchaseQuantity);
    }

    public boolean isSoldOut() {
        return this.stock.isSoldOut();
    }

    public int getStock() {
        return stock.getQuantity();
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
