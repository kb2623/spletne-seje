package com.googlecode.lanterna.gui2;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TestTerminalFactory;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;

public class MultiButtonTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        Screen screen = new TestTerminalFactory(args).createScreen();
        screen.startScreen();
        MultiWindowTextGUI textGUI = new MultiWindowTextGUI(screen);
        textGUI.setEOFWhenNoWindows(true);
        try {
            final BasicWindow window = new BasicWindow("Button test");
            Panel contentArea = new Panel();
            contentArea.setLayoutManager(new LinearLayout(Direction.VERTICAL));
            contentArea.addComponent(new Button(""));
            contentArea.addComponent(new Button("TRE"));
            contentArea.addComponent(new Button("Button"));
            contentArea.addComponent(new Button("Another button"));
            contentArea.addComponent(new EmptySpace(new TerminalSize(5, 1)));
            //contentArea.addComponent(new Button("Here is a\nmulti-line\ntext segment that is using \\n"));
            contentArea.addComponent(new Button("OK", window::close));

            window.setComponent(contentArea);
            textGUI.addWindowAndWait(window);
        } finally {
            screen.stopScreen();
        }
    }
}
