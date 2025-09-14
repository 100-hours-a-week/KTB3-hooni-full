package ordering;

import menu.Menu;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {
    private final Map<Menu, Integer> pickedMenus;

    public ShoppingCart() {
        this.pickedMenus = new HashMap<>();
    }

    public void add(Menu menu) {
        pickedMenus.put(menu, pickedMenus.getOrDefault(menu, 0) + 1);
    }

    int getTotalQuantity() {
        return pickedMenus.values().stream().mapToInt(Integer::intValue).sum();
    }

    int getTotalPrice() {
        return pickedMenus.keySet().stream()
                .mapToInt(menu -> menu.getPrice() * pickedMenus.get(menu))
                .sum();
    }
}
