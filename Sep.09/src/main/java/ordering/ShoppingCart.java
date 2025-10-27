package ordering;

import menu.MenuItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {
    private final Map<MenuItem, Integer> pickedMenus;

    public ShoppingCart() {
        this.pickedMenus = Collections.synchronizedMap(new LinkedHashMap<>());
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

    public boolean isRemainingInStock() {
        return pickedMenus.entrySet().stream()
                .allMatch(entry -> {
                    int quantity = entry.getValue();
                    return entry.getKey().isEnoughStock(quantity);
                });
    }

    public void purchase() {
        pickedMenus.forEach((menuItem, quantity) -> menuItem.sell(quantity));
    }

    public List<String> removeInsufficientItems() {
        List<String> removedItems = new ArrayList<>();

        for (var entry : pickedMenus.entrySet()) {
            MenuItem item = entry.getKey();
            Integer buyingQuantity = entry.getValue();

            if (item.isEnoughStock(buyingQuantity)) {
                continue;
            }

            if (item.isSoldOut()) {
                removedItems.add(item.getName());
                pickedMenus.put(item, 0);
            } else {
                removedItems.add(item.getName());
                pickedMenus.put(item, item.getStock());
            }
        }

        return removedItems;
    }

    public boolean isEmpty() {
        if (pickedMenus.isEmpty()) {
            return true;
        }

        return pickedMenus.values().stream()
                .allMatch(quantity -> quantity == 0);
    }
}
