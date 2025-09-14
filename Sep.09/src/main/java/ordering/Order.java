package ordering;

import reader.ConsoleIO;

public class Order {
    private static final String ENTER_MONEY = "돈을 투입해주세요.";
    private static final String INSUFFICIENT_AMOUNT_TO_PAY = "결제하기에 부족한 금액입니다.";
    private static final String BILL_FORMAT = """
            
            선택하신 메뉴 계산서입니다.
            ===================================
            %s
            -----------------------------------
            총 지불해야 할 금액 : %d 원
            ===================================
                        
            """;
    private static final String YOUR_PAY_AND_CHANGE_AMOUNT = "지불 금액 : %d, 거스름돈 : %d";

    private final ShoppingCart shoppingCart;

    public Order(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public void checkout() {
        printBill();
        pay();
    }

    private void printBill() {
        ConsoleIO.printf(BILL_FORMAT,
                shoppingCart.getBillFormat(),
                shoppingCart.getTotalPrice());
    }

    private void pay() {
        ConsoleIO.print(ENTER_MONEY);

        int paidAmount;
        while (true) {
            try {
                paidAmount = ConsoleIO.inputNumber();
                validateEnoughToPay(paidAmount);
                break;
            } catch (IllegalArgumentException e) {
                ConsoleIO.print(INSUFFICIENT_AMOUNT_TO_PAY);
            }
        }

        returnChange(paidAmount);
    }

    private void validateEnoughToPay(int paidAmount) {
        if (paidAmount < shoppingCart.getTotalPrice()) {
            throw new IllegalArgumentException();
        }
    }

    private void returnChange(int paidAmount) {
        int change = paidAmount - shoppingCart.getTotalPrice();

        ConsoleIO.printf(YOUR_PAY_AND_CHANGE_AMOUNT,
                paidAmount, change);
    }
}
