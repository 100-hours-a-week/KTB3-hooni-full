package menu;

import reader.ConsoleIO;

import java.util.List;

public class Menus {
    private static final String CHOOSE_MAIN_MENU = "필수 선택 메뉴를 골라주세요.";
    private static final String CHOOSE_MORE_MENU = "추가 선택 메뉴를 골라주세요.";

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

    public Menu chooseMainMenu() {
        ConsoleIO.print(CHOOSE_MAIN_MENU);
        showMainMenus();

        int menuId;
        while (true) {
            try {
                menuId = ConsoleIO.inputNumber();
                validateIsMainMenu(menuId);
                break;
            } catch (IllegalArgumentException e) {
                ConsoleIO.printWrongInput();
            }
        }

        return findMenu(menuId);
    }

    public Menu chooseMoreMenu() {
        ConsoleIO.print(CHOOSE_MORE_MENU);
        showAllMenus();

        int menuId;
        while (true) {
            try {
                menuId = ConsoleIO.inputNumber();
                validateIsMenuId(menuId);
                break;
            } catch (IllegalArgumentException e) {
                ConsoleIO.printWrongInput();
            }
        }

        return findMenu(menuId);
    }

    private void showAllMenus() {
        showMainMenus();
        showBeverages();
        showSideMenus();
    }

    private void showMainMenus() {
        menus.stream()
                .filter(menu -> menu instanceof MainMenu)
                .forEach(Menu::showInfo);
        System.out.println(); // 출력 포맷을 위한 개행
    }

    private void showBeverages() {
        menus.stream()
                .filter(menu -> menu instanceof Beverage)
                .forEach(Menu::showInfo);
        System.out.println(); // 출력 포맷을 위한 개행
    }

    private void showSideMenus() {
        menus.stream()
                .filter(menu -> menu instanceof SideMenu)
                .forEach(Menu::showInfo);
        System.out.println(); // 출력 포맷을 위한 개행
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
