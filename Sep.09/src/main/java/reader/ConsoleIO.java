package reader;

import java.io.IOException;

public class ConsoleIO {
    private static final String INPUT_RIGHT_VALUE = "[ERROR] 올바른 값을 입력해주세요.";

    private static final Reader reader = new Reader();

    public static void print(String message) {
        System.out.println(message);
    }

    public static void printf(String format, Object... args) {
        System.out.printf(format, args);
    }

    public static void printWrongInput() {
        System.out.println(INPUT_RIGHT_VALUE);
    }

    public static int inputNumber() {
        while (true) {
            try {
                String input = reader.read();

                int number = parseInteger(input);
                validateNaturalNumber(number);

                return number;
            } catch (IllegalArgumentException | IOException e) {
                printWrongInput();
            }
        }
    }

    private static int parseInteger(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException();
        }

        int number;
        try {
            number = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }

        return number;
    }

    private static void validateNaturalNumber(int number) {
        if (number <= 0) {
            throw new IllegalArgumentException();
        }
    }
}
