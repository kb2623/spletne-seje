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

import com.googlecode.lanterna.gui.Action;
import com.googlecode.lanterna.gui.Component;
import com.googlecode.lanterna.gui.DefaultBackgroundRenderer;
import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Button;
import com.googlecode.lanterna.gui.dialog.ListSelectDialog;
import com.googlecode.lanterna.gui.dialog.MessageBox;
import com.googlecode.lanterna.gui.layout.LinearLayout;
import com.googlecode.lanterna.TestTerminalFactory;
import java.io.IOException;

/**
 *
 * @author Martin
 */
public class Issue36 {
    public static void main(String[] args) throws IOException {
        final GUIScreen guiScreen = new TestTerminalFactory(args).createGUIScreen();
        guiScreen.getScreen().startScreen();
        guiScreen.setBackgroundRenderer(new DefaultBackgroundRenderer("GUI Test"));

        final Window mainWindow = new Window("Window");
        Button listDialogButton = new Button("Show ListSelectDialog", () -> {
            String selected = ListSelectDialog.<String>showDialog(guiScreen, "Choose a string",
                    "We are checking the appearance of this dialog",
                    "First",
                    "Second",
                    "Third",
                    "Fourth",
                    "Fifth");
            MessageBox.showMessageBox(guiScreen, "Selection", "You chose: " + selected);
        });
        mainWindow.addComponent(listDialogButton);
        Button exitButton = new Button("Close", mainWindow::close);
        exitButton.setAlignment(Component.Alignment.RIGHT_CENTER);
        mainWindow.addComponent(exitButton, LinearLayout.GROWS_HORIZONTALLY);

        guiScreen.showWindow(mainWindow, GUIScreen.Position.CENTER);
        guiScreen.getScreen().stopScreen();
    }
}
