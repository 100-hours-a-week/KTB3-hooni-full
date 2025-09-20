package menu;

import java.util.Objects;

public abstract class MenuItem {
    private final String name;
    private final int price;

    protected MenuItem(String name, int price) {
        this.name = name;
        this.price = price;
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
