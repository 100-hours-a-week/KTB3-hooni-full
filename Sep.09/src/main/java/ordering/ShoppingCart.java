package ordering;

import menu.MenuItem;

import java.util.LinkedHashMap;
import java.util.Map;

public class ShoppingCart {
    private final Map<MenuItem, Integer> pickedMenus;

    public ShoppingCart() {
        this.pickedMenus = new LinkedHashMap<>();
    }

    public void add(MenuItem menu) {
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

    String getBillFormat() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<MenuItem, Integer> entry : pickedMenus.entrySet()) {
            sb.append(entry.getKey().getName()).append(" (*").append(entry.getValue()).append(")")
                    .append("  || ").append(entry.getKey().getPrice() * entry.getValue()).append("\n");
        }

        return sb.toString();
    }
}
