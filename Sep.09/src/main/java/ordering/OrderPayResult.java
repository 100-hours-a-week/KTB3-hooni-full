package ordering;

import java.util.List;

public class OrderPayResult {
    private final OrderStatus orderStatus;
    private final int change;
    private final List<String> insufficientItems;

    public OrderPayResult(OrderStatus orderStatus, int change, List<String> insufficientItems) {
        this.orderStatus = orderStatus;
        this.change = change;
        this.insufficientItems = insufficientItems;
    }

    public static OrderPayResult success(int change) {
        return new OrderPayResult(OrderStatus.SUCCESS, change, List.of());
    }

    public OrderStatus getStatus() {
        return this.orderStatus;
    }

    public int getChange() {
        return this.change;
    }

    public List<String> getInsufficientItems() {
        return insufficientItems;
    }

    public boolean isInsufficientStock() {
        return this.orderStatus == OrderStatus.INSUFFICIENT_STOCK;
    }

    public boolean isEmptyCart() {
        return this.orderStatus == OrderStatus.EMPTY_CART;
    }
}
