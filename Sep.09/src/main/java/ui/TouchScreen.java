package ui;

public class TouchScreen {
    Reader reader;
    Writer writer;

    public TouchScreen(Reader reader, Writer writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public void show(String message) {
        writer.write(message);
    }

    public void show(Exception e) {
        writer.write("[ERROR] " + e.getMessage());
    }

    public int inputNaturalNumber() {
        while (true) {
            String input = reader.read();

            try {
                int number = parseInteger(input);
                validateNaturalNumber(number);

                return number;
            } catch (IllegalArgumentException e) {
                show(e);
            }
        }
    }

    private int parseInteger(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException(GuidanceMessage.YOU_SHOULD_INPUT_INTEGER.getText());
        }

        int number;
        try {
            number = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(GuidanceMessage.YOU_SHOULD_INPUT_INTEGER.getText());
        }

        return number;
    }

    private void validateNaturalNumber(int number) {
        if (number <= 0) {
            throw new IllegalArgumentException(GuidanceMessage.YOU_SHOULD_INPUT_NATURAL_NUMBER.getText());
        }
    }
}
