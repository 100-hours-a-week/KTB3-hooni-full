package common.io;

import ui.Reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.System.exit;

public class InputReader implements Reader {
    private final BufferedReader reader;

    public InputReader() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public String read() {
        String input = "";

        try {
            input = reader.readLine();
        } catch (IOException e) {
            System.out.println("[ERROR] 입력에 문제가 생겼습니다. 프로그램이 종료됩니다.");
            exit(1);
        }

        return input;
    }
}
