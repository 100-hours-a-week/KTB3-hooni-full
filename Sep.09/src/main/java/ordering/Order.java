package ordering;

import common.message.GuidanceMessage;
import ui.TouchScreen;

import java.util.List;

import static common.message.GuidanceMessage.REMOVE_INSUFFICIENT_ITEMS_AND_PAY;

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

    public int pay(int paidAmount, TouchScreen touchScreen) {
        validateEnoughToPay(paidAmount);

        if (shoppingCart.isRemainingInStock()) {
            shoppingCart.purchase();
        } else {
            List<String> soldOutItemNames = shoppingCart.removeInsufficientItems();

            if (shoppingCart.isEmpty()) {
                touchScreen.show(
                        String.format(REMOVE_INSUFFICIENT_ITEMS_AND_PAY.getText(), soldOutItemNames) + "\n" +
                        GuidanceMessage.NO_ITEMS_TO_CHECKOUT.getText());
                return paidAmount;
            }

            touchScreen.show(String.format(REMOVE_INSUFFICIENT_ITEMS_AND_PAY.getText(), soldOutItemNames));
            shoppingCart.purchase();
        }

        return returnChange(paidAmount);
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
