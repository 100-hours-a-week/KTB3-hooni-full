package ordering;

import menu.Menu;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ShoppingCartTest {

    @Test
    public void 쇼핑카트에_새로운_메뉴_추가() {
        // given
        ShoppingCart shoppingCart = new ShoppingCart();

        // when
        Menu menu = new TestMenu(1, "테스트 메뉴", 1000);
        shoppingCart.add(menu);

        // then
        assertThat(shoppingCart.getTotalQuantity()).isEqualTo(1);
        assertThat(shoppingCart.getTotalPrice()).isEqualTo(1000);
    }

    @Test
    public void 쇼핑카트에_이미_있는_메뉴_또_추가하기() {
        // given
        Menu menu = new TestMenu(1, "테스트 메뉴", 1000);
        Menu menu2 = new TestMenu(2, "테스트 메뉴2", 3000);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.add(menu);
        shoppingCart.add(menu2);

        // when
        Menu extraMenu = new TestMenu(1, "테스트 메뉴", 1000);
        shoppingCart.add(extraMenu);

        // then
        assertThat(shoppingCart.getTotalQuantity()).isEqualTo(3);
        assertThat(shoppingCart.getTotalPrice()).isEqualTo(5000);

    }

    static class TestMenu extends Menu {
        private final int id;

        TestMenu(int id, String name, int price) {
            super(name, price);
            this.id = id;
        }
    }
}
