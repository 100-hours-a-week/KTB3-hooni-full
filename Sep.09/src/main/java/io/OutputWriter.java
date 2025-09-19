package io;

import ui.Writer;

public class OutputWriter implements Writer {

    @Override
    public void write(String message) {
        System.out.println(message);
    }
}
