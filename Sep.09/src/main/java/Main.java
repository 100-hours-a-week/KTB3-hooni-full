import menu.Menu;
import menu.Menus;
import ordering.Order;
import ordering.ShoppingCart;
import reader.ConsoleIO;

import static java.lang.System.exit;

public class Main {
    private static final String WELCOME_MESSAGE = "안녕하세요. 주문을 시작해주세요.";
    private static final String YOU_SHOULD_ENTER_CHOICE_NUMBER = "원하시는 메뉴 혹은 옵션의 번호를 입력하여 주문해주세요.\n";
    private static final String WOULD_YOU_CHOOSE_MORE_MENU = "\n메뉴를 더 고르시겠습니까?\n 1. 추가 메뉴 구매하기 2. 결제하기\n";
    private static final String THANK_YOU_FOR_USING = "\n주문을 완료했습니다. 이용해주셔서 감사합니다.\n\n";

    private static final int YES = 1;

    public static void main(String[] args) {
        Menus menus = Menus.init();

        while (true) {
            ShoppingCart shoppingCart = new ShoppingCart();
            Order order = new Order(shoppingCart);

            ConsoleIO.print(WELCOME_MESSAGE);
            ConsoleIO.print(YOU_SHOULD_ENTER_CHOICE_NUMBER);

            Menu mainMenu = menus.chooseMainMenu();
            shoppingCart.add(mainMenu);

            while (wantMoreMenu()) {
                Menu extraMenu = menus.chooseMoreMenu();
                shoppingCart.add(extraMenu);
            }

            order.checkout();

            ConsoleIO.print(THANK_YOU_FOR_USING);
            sleep(2);
        }
    }

    private static boolean wantMoreMenu() {
        ConsoleIO.print(WOULD_YOU_CHOOSE_MORE_MENU);
        int input = ConsoleIO.inputNumber();

        return YES == input;
    }

    private static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            ConsoleIO.print("[ERROR] 프로그램 오류, 프로그램 종료합니다.");
            exit(1);
        }
    }
}
