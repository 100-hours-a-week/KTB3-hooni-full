import menu.Menus;
import reader.ConsoleIO;

public class Main {
    private static final String WELCOME_MESSAGE = "안녕하세요. 주문을 시작해주세요.";
    private static final String YOU_SHOULD_ENTER_CHOICE_NUMBER = "원하시는 메뉴 혹은 옵션의 번호를 입력하여 주문해주세요.\n";
    private static final String WOULD_YOU_CHOOSE_MORE_MENU = "\n메뉴를 더 고르시겠습니까?\n 1. 추가 메뉴 구매하기 2. 결제하기\n";
    private static final int YES = 1;

    public static void main(String[] args) {
        Menus menus = Menus.init();

        while (true) {
            ConsoleIO.print(WELCOME_MESSAGE);
            ConsoleIO.print(YOU_SHOULD_ENTER_CHOICE_NUMBER);

            menus.chooseMainMenu();

            while (wantMoreMenu()) {
                menus.chooseMoreMenu();
            }

            // TODO: 추가 메뉴 선택 끝나면 결제 프로세스 진행
        }
    }

    private static boolean wantMoreMenu() {
        ConsoleIO.print(WOULD_YOU_CHOOSE_MORE_MENU);
        int input = ConsoleIO.inputNumber();

        return YES == input;
    }
}
