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
package com.googlecode.lanterna.gui;

import com.googlecode.lanterna.gui.component.ActionListBox;
import com.googlecode.lanterna.gui.component.Button;
import com.googlecode.lanterna.gui.component.EmptySpace;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.layout.LinearLayout;
import com.googlecode.lanterna.TestTerminalFactory;
import java.io.IOException;

/**
 * This class can be used to test various layout arrangements
 * @author Martin
 */
public class LayoutTest {

    public static void main(String[] args) throws IOException {
        final GUIScreen guiScreen = new TestTerminalFactory(args).createGUIScreen();
        guiScreen.getScreen().startScreen();
        final Window window1 = new Window("Layout test selector");

        ActionListBox actionListBox = new ActionListBox();
        for (int i = 0; i < 5; i++) {
            actionListBox.addAction("New window " + i, () -> {
            });
        }
        window1.addComponent(actionListBox);
        window1.addComponent(new EmptySpace());

        Panel buttonPanel = new Panel(new Border.Invisible(), Panel.Orientation.HORIZONTAL);
        Button exitButton = new Button("Exit", window1::close);
        buttonPanel.addComponent(new EmptySpace(1, 1), LinearLayout.MAXIMIZES_HORIZONTALLY);
        buttonPanel.addComponent(exitButton);
        window1.addComponent(buttonPanel);
        guiScreen.showWindow(window1, GUIScreen.Position.CENTER);
        guiScreen.getScreen().stopScreen();
    }
}