package menu;

import java.util.List;

public class Menus {
    private final List<Menu> menus;

    private Menus() {
        menus = List.of(new MainMenu("빅맥", 5000), new MainMenu("치즈버거", 2000),
                new MainMenu("싸이버거", 4000), new MainMenu("와퍼", 4500),
                new Beverage("콜라", 1000), new Beverage("사이다", 1000),
                new Beverage("제로콜라", 1500), new Beverage("제로사이다", 1500),
                new SideMenu("감자튀김", 1000), new SideMenu("치킨너겟", 2000),
                new SideMenu("코울슬로", 2500), new SideMenu("치즈스틱", 1500)
        );
    }

    public static Menus init() {
        return new Menus();
    }

    public String getMainMenuInfos() {
        StringBuilder sb = new StringBuilder();

        menus.stream()
                .filter(menu -> menu instanceof MainMenu)
                .forEach(menu -> sb.append(menu.getInfo()));
        sb.append("\n"); // 출력 포맷을 위한 개행

        return sb.toString();
    }

    public Menu chooseMainMenu(int menuId) {
        validateIsMainMenu(menuId);

        return findMenu(menuId);
    }

    public Menu chooseMoreMenu(int menuId) {
        validateIsMenuId(menuId);

        return findMenu(menuId);
    }

    public String getAllMenuInfos() {
        StringBuilder sb = new StringBuilder();

        sb.append(getMainMenuInfos())
                .append(getBeverageInfos())
                .append(getSideMenuInfos());

        return sb.toString();
    }

    private String getBeverageInfos() {
        StringBuilder sb = new StringBuilder();

        menus.stream()
                .filter(menu -> menu instanceof Beverage)
                .forEach(menu -> sb.append(menu.getInfo()));
        sb.append("\n"); // 출력 포맷을 위한 개행

        return sb.toString();
    }

    private String getSideMenuInfos() {
        StringBuilder sb = new StringBuilder();

        menus.stream()
                .filter(menu -> menu instanceof SideMenu)
                .forEach(menu -> sb.append(menu.getInfo()));
        sb.append("\n");

        return sb.toString();
    }

    private void validateIsMenuId(int menuId) {
        menus.stream()
                .filter(menu -> menu.is(menuId))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    private void validateIsMainMenu(int menuId) {
        validateIsMenuId(menuId);

        menus.stream()
                .filter(menu -> menu.is(menuId))
                .filter(menu -> menu instanceof MainMenu)
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    private Menu findMenu(int menuId) {
        return menus.stream()
                .filter(menu -> menu.is(menuId))
                .findAny()
                .get(); // 이전 검증에서 이미 존재함을 확인했음
    }
}
