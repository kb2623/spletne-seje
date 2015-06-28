package com.googlecode.lanterna.gui2;

import java.io.IOException;

public class DynamicGridLayoutTest extends TestBase {
    public static void main(String[] args) throws IOException, InterruptedException {
        new DynamicGridLayoutTest().run(args);
    }

    @Override
    public void init(WindowBasedTextGUI textGUI) {
        final BasicWindow window = new BasicWindow("Grid layout test");

        Panel mainPanel = new Panel();
        mainPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        final Panel gridPanel = new Panel();
        GridLayout gridLayout = new GridLayout(4);
        gridPanel.setLayoutManager(gridLayout);

        Panel controlPanel = new Panel();
        controlPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        controlPanel.addComponent(new Button("Reset Grid", gridPanel::removeAllComponents));

        textGUI.addWindow(window);
    }
}
