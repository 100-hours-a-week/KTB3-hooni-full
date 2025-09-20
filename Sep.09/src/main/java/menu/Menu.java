package menu;

import java.util.List;

public class Menu {
    private final List<MenuItem> menuItems;

    private Menu() {
        menuItems = List.of(new MainDish("빅맥", 5000), new MainDish("치즈버거", 2000),
                new MainDish("싸이버거", 4000), new MainDish("와퍼", 4500),
                new Beverage("콜라", 1000), new Beverage("사이다", 1000),
                new Beverage("제로콜라", 1500), new Beverage("제로사이다", 1500),
                new SideDish("감자튀김", 1000), new SideDish("치킨너겟", 2000),
                new SideDish("코울슬로", 2500), new SideDish("치즈스틱", 1500)
        );
    }

    public static Menu init() {
        return new Menu();
    }

    public String getMainDishInfos() {
        StringBuilder sb = new StringBuilder();

        menuItems.stream()
                .filter(menuItem -> menuItem instanceof MainDish)
                .forEach(menuItem -> sb.append(menuItem.getInfo()));
        sb.append("\n"); // 출력 포맷을 위한 개행

        return sb.toString();
    }

    public MenuItem chooseMainMenu(int menuId) {
        validateIsMainMenu(menuId);

        return findMenu(menuId);
    }

    public MenuItem chooseMoreMenu(int menuId) {
        validateIsMenuId(menuId);

        return findMenu(menuId);
    }

    public String getAllMenuInfos() {
        StringBuilder sb = new StringBuilder();

        sb.append(getMainDishInfos())
                .append(getBeverageInfos())
                .append(getSideMenuInfos());

        return sb.toString();
    }

    private String getBeverageInfos() {
        StringBuilder sb = new StringBuilder();

        menuItems.stream()
                .filter(menuItem -> menuItem instanceof Beverage)
                .forEach(menuItem -> sb.append(menuItem.getInfo()));
        sb.append("\n"); // 출력 포맷을 위한 개행

        return sb.toString();
    }

    private String getSideMenuInfos() {
        StringBuilder sb = new StringBuilder();

        menuItems.stream()
                .filter(menuItem -> menuItem instanceof SideDish)
                .forEach(menuItem -> sb.append(menuItem.getInfo()));
        sb.append("\n");

        return sb.toString();
    }

    private void validateIsMenuId(int menuId) {
        menuItems.stream()
                .filter(menuItem -> menuItem.is(menuId))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    private void validateIsMainMenu(int menuId) {
        validateIsMenuId(menuId);

        menuItems.stream()
                .filter(menuItem -> menuItem.is(menuId))
                .filter(menuItem -> menuItem instanceof MainDish)
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    private MenuItem findMenu(int menuId) {
        return menuItems.stream()
                .filter(menuItem -> menuItem.is(menuId))
                .findAny()
                .get(); // 이전 검증에서 이미 존재함을 확인했음
    }
}
