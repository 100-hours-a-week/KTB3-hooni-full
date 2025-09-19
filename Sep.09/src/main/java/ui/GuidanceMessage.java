package ui;

public enum GuidanceMessage {
    WELCOME_MESSAGE("안녕하세요. 주문을 시작해주세요."),
    YOU_SHOULD_ENTER_CHOICE_NUMBER("원하시는 메뉴 혹은 옵션의 번호를 입력하여 주문해주세요.\n"),
    WOULD_YOU_CHOOSE_MORE_MENU("\n메뉴를 더 고르시겠습니까?\n 1. 추가 메뉴 구매하기 2. 결제하기\n"),
    THANK_YOU_FOR_USING("\n주문을 완료했습니다. 이용해주셔서 감사합니다.\n\n"),
    CHOOSE_MAIN_MENU("필수 선택 메뉴를 골라주세요."),
    CHOOSE_MORE_MENU("추가 선택 메뉴를 골라주세요."),
    ENTER_MONEY("돈을 투입해주세요."),
    INSUFFICIENT_AMOUNT_TO_PAY("결제하기에 부족한 금액입니다."),
    RETURN_CHANGE_AMOUNT("거스름돈을 드리겠습니다."),

    // Error Message
    YOU_SHOULD_INPUT_INTEGER("숫자를 입력해주세요."),
    YOU_SHOULD_INPUT_NATURAL_NUMBER("1 이상의 자연수를 입력해주세요."),

    ;

    private final String text;

    GuidanceMessage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
