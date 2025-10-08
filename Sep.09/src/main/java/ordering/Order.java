package ordering;

import common.message.GuidanceMessage;
import java.util.List;

public class Order {
    private static final String BILL_FORMAT = """
            
            선택하신 메뉴 계산서입니다.
            ===================================
            %s
            -----------------------------------
            총 지불해야 할 금액 : %d 원
            ===================================
            
            """;

    private final ShoppingCart shoppingCart;

    public Order(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public String getBill() {
        return String.format(BILL_FORMAT, shoppingCart.getBillFormat(), shoppingCart.getTotalPrice());
    }

    public int getTotalPrice() {
        return shoppingCart.getTotalPrice();
    }

    public OrderPayResult pay(int paidAmount) {
        validateEnoughToPay(paidAmount);

        if (shoppingCart.isRemainingInStock()) {
            shoppingCart.purchase();
            return OrderPayResult.success(returnChange(paidAmount));
        } else {
            List<String> removedItemNames = shoppingCart.removeInsufficientItems();

            if (shoppingCart.isEmpty()) {
                return new OrderPayResult(OrderStatus.EMPTY_CART, paidAmount, removedItemNames);
            }

            shoppingCart.purchase();
            return new OrderPayResult(OrderStatus.INSUFFICIENT_STOCK, returnChange(paidAmount), removedItemNames);
        }
    }

    private void validateEnoughToPay(int paidAmount) {
        if (paidAmount < shoppingCart.getTotalPrice()) {
            throw new IllegalArgumentException(GuidanceMessage.YOU_SHOULD_INPUT_ENOUGH_TO_PAY.getText());
        }
    }

    private int returnChange(int paidAmount) {
        return paidAmount - shoppingCart.getTotalPrice();
    }
}
