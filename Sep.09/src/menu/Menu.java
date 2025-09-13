package menu;

import reader.ConsoleIO;

public abstract class Menu {
    private static int index = 1;

    private final int id;
    private final String name;
    private int price;

    Menu(String name, int price) {
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
}
