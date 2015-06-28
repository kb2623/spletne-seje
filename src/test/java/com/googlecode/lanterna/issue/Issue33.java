/*
 * This file is part of lanterna (http://code.google.com/p/lanterna/).
 *
 * lanterna is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2010-2015 Martin
 */
package com.googlecode.lanterna.issue;

import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Button;
import com.googlecode.lanterna.gui.component.EmptySpace;
import com.googlecode.lanterna.gui.component.Label;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.component.Table;
import com.googlecode.lanterna.gui.layout.LinearLayout;
import com.googlecode.lanterna.TestTerminalFactory;
import java.io.IOException;
import java.util.Random;

/**
 * http://code.google.com/p/lanterna/issues/detail?id=33
 *
 * Verifying that adding rows to a table asynchronously refreshes the GUI screen
 *
 * @author Martin
 */
public class Issue33 {
    public static void main(String[] args) throws IOException {
        final GUIScreen gui = new TestTerminalFactory(args).createGUIScreen();
        gui.getScreen().startScreen();

        final Window mainWindow = new Window("Window with a table");
        final Table table = new Table(4, "My Table");
        table.setColumnPaddingSize(1);
        table.addRow(new Label("Column 1"),
                        new Label("Column 2"),
                        new Label("Column 3"),
                        new Label("Column 4"));
        mainWindow.addComponent(table);

        Panel bottomPanel = new Panel(Panel.Orientation.HORIZONTAL);
        bottomPanel.addComponent(new EmptySpace(), LinearLayout.GROWS_HORIZONTALLY);
        bottomPanel.addComponent(new Button("Close", mainWindow::close));
        mainWindow.addComponent(bottomPanel);

        Thread addTenRowsThread = new Thread(() -> {
            try {
                for(int i = 0; i < 10; i++) {
                    Thread.sleep(1000);
                    gui.runInEventThread(() -> {
                        Random random = new Random();
                        table.addRow(new Label(random.nextDouble() + ""),
                                        new Label(random.nextBoolean() + ""),
                                        new Label(random.nextGaussian() + ""),
                                        new Label(random.nextInt() + ""));
                    });
                }
            }
            catch(InterruptedException e) {}
        });
        addTenRowsThread.setDaemon(true);
        addTenRowsThread.start();

        gui.showWindow(mainWindow);
        gui.getScreen().stopScreen();
    }
}
