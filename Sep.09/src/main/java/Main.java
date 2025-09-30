import common.io.InputReader;
import common.io.OutputWriter;
import menu.MenuItem;
import menu.Menu;
import ordering.Order;
import ordering.ShoppingCart;
import common.message.GuidanceMessage;
import ui.TouchScreen;

import static java.lang.System.exit;

public class Main {
    private static final int YES = 1;

    public static void main(String[] args) {
        TouchScreen touchScreen = new TouchScreen(new InputReader(), new OutputWriter());

        Menu menu = Menu.init();

        while (true) {
            ShoppingCart shoppingCart = new ShoppingCart();
            Order order = new Order(shoppingCart);

            touchScreen.show(GuidanceMessage.WELCOME_MESSAGE.getText());
            touchScreen.show(GuidanceMessage.YOU_SHOULD_ENTER_CHOICE_NUMBER.getText());

            touchScreen.show(menu.getMainDishInfos());
            chooseMainDish(touchScreen, menu, shoppingCart);

            while (wantMoreMenu(touchScreen)) {
                touchScreen.show(menu.getAllMenuInfos());
                chooseMoreMenu(touchScreen, menu, shoppingCart);
            }

            touchScreen.show(order.getBill());
            checkout(touchScreen, order);

            touchScreen.show(GuidanceMessage.THANK_YOU_FOR_USING.getText());
            sleep(2);
        }
    }

    private static void chooseMainDish(TouchScreen touchScreen, Menu menu, ShoppingCart shoppingCart) {
        touchScreen.show(GuidanceMessage.CHOOSE_MAIN_MENU.getText());

        while (true) {
            int menuItemIndex = touchScreen.inputNaturalNumber();

            try {
                MenuItem mainDish = menu.chooseMainMenu(menuItemIndex);
                shoppingCart.add(mainDish);

                break;
            } catch (IllegalArgumentException e) {
                touchScreen.show(e);
            }
        }
    }

    private static boolean wantMoreMenu(TouchScreen touchScreen) {
        touchScreen.show(GuidanceMessage.WOULD_YOU_CHOOSE_MORE_MENU.getText());

        int input = 0;
        while (true) {
            input = touchScreen.inputNaturalNumber();

            try {
                validateIsAllowedChoice(input);
                break;
            } catch (IllegalArgumentException e) {
                touchScreen.show(e);
            }
        }

        return YES == input;
    }

    private static void validateIsAllowedChoice(int input) {
        if (input != 1 && input != 2) {
            throw new IllegalArgumentException(GuidanceMessage.YOU_SHOULD_INPUT_RIGHT_RANGE_MENU_NUMBER.getText());
        }
    }

    private static void chooseMoreMenu(TouchScreen touchScreen, Menu menu, ShoppingCart shoppingCart) {
        touchScreen.show(GuidanceMessage.CHOOSE_MORE_MENU.getText());

        while (true) {
            int menuIndex = touchScreen.inputNaturalNumber();

            try {
                MenuItem extraMenu = menu.chooseMoreMenu(menuIndex);
                shoppingCart.add(extraMenu);

                break;
            } catch (IllegalArgumentException e) {
                touchScreen.show(e);
            }
        }
    }

    private static void checkout(TouchScreen touchScreen, Order order) {
        touchScreen.show(GuidanceMessage.ENTER_MONEY.getText());

        while (true) {
            int amount = touchScreen.inputNaturalNumber();

            try {
                int change = order.pay(amount);
                touchScreen.show(GuidanceMessage.RETURN_CHANGE_AMOUNT.getText() + " : " + change);

                break;
            } catch (IllegalArgumentException e) {
                touchScreen.show(e);
            }
        }
    }

    private static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            System.out.print("[ERROR] 프로그램 오류, 프로그램 종료합니다.");
            exit(1);
        }
    }
}
