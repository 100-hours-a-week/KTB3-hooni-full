package menu;

import java.util.List;

public class Menu {
    private final List<MenuItem> menuItems;

    private Menu() {
        menuItems = List.of(new MenuItem("데이터", 1, MenuType.Dummy), // 리스트 1부터 시작을 위한 더미데이터
                new MenuItem("빅맥", 5000, MenuType.MainDish), new MenuItem("치즈버거", 2000, MenuType.MainDish),
                new MenuItem("싸이버거", 4000, MenuType.MainDish), new MenuItem("와퍼", 4500, MenuType.MainDish),
                new MenuItem("콜라", 1000, MenuType.Beverage), new MenuItem("사이다", 1000, MenuType.Beverage),
                new MenuItem("제로콜라", 1500, MenuType.Beverage), new MenuItem("제로사이다", 1500, MenuType.Beverage),
                new MenuItem("감자튀김", 1000, MenuType.SideDish), new MenuItem("치킨너겟", 2000, MenuType.SideDish),
                new MenuItem("코울슬로", 2500, MenuType.SideDish), new MenuItem("치즈스틱", 1500, MenuType.SideDish)
        );
    }

    public static Menu init() {
        return new Menu();
    }

    public String getMainDishInfos() {
        StringBuilder sb = new StringBuilder();

        menuItems.stream()
                .filter(MenuItem::isMainDish)
                .forEach(menuItem -> sb.append(getInfo(menuItem)));
        sb.append("\n"); // 출력 포맷을 위한 개행

        return sb.toString();
    }

    public MenuItem chooseMainMenu(int menuIndex) {
        validateIsMainMenu(menuIndex);

        return findMenu(menuIndex);
    }

    public MenuItem chooseMoreMenu(int menuIndex) {
        validateIsMenuId(menuIndex);

        return findMenu(menuIndex);
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
                .filter(MenuItem::isBeverage)
                .forEach(menuItem -> sb.append(getInfo(menuItem)));
        sb.append("\n"); // 출력 포맷을 위한 개행

        return sb.toString();
    }

    private String getSideMenuInfos() {
        StringBuilder sb = new StringBuilder();

        menuItems.stream()
                .filter(MenuItem::isSideDish)
                .forEach(menuItem -> sb.append(getInfo(menuItem)));
        sb.append("\n");

        return sb.toString();
    }

    private String getInfo(MenuItem menuItem) {
        StringBuilder sb = new StringBuilder();

        sb.append("[").append(menuItems.indexOf(menuItem)).append(". ").append(menuItem.getInfo()).append("]  ");
        return sb.toString();
    }

    private void validateIsMenuId(int menuIndex) {
        try {
            menuItems.get(menuIndex);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException();
        }
    }

    private void validateIsMainMenu(int menuIndex) {
        validateIsMenuId(menuIndex);

        MenuItem menuItem = menuItems.get(menuIndex);
        if (!(menuItem.isMainDish())) {
            throw new IllegalArgumentException();
        }
    }

    private MenuItem findMenu(int menuIndex) {
        return menuItems.get(menuIndex); // 이전 검증에서 이미 존재함을 확인했음
    }
}
