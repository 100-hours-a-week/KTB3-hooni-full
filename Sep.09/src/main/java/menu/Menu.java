package menu;

import reader.ConsoleIO;

import java.util.Objects;

public abstract class Menu {
    private static int index = 1;

    private final int id;
    private final String name;
    private final int price;

    protected Menu(String name, int price) {
        this.id = index++;
        this.name = name;
        this.price = price;
    }

    public void showInfo() {
        ConsoleIO.printf("[%d. %s, %d원]   ", id, name, price);
    }

    public boolean is(int id) {
        return this.id == id;
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

        if (obj instanceof Menu) {
            return this.id == ((Menu) obj).id;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
