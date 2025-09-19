package ordering;

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

    public int pay(int paidAmount) {
        validateEnoughToPay(paidAmount);

        return returnChange(paidAmount);
    }

    private void validateEnoughToPay(int paidAmount) {
        if (paidAmount < shoppingCart.getTotalPrice()) {
            throw new IllegalArgumentException();
        }
    }

    private int returnChange(int paidAmount) {
        return paidAmount - shoppingCart.getTotalPrice();
    }
}
