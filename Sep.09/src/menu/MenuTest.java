package menu;

public class MenuTest {

    public static class TestMenu extends Menu {
        private final int id;

        public TestMenu(int id, String name, int price) {
            super(name, price);
            this.id = id;
        }
    }

}
