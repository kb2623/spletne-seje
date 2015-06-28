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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Testing to close non-top windows
 * @author Martin
 */
public class MultilevelCloseWindow {

    private static final List<Window> WINDOWS = new ArrayList<>();
    private static final AtomicInteger WINDOW_COUNTER = new AtomicInteger(0);

    public static void main(String[] args) throws IOException {
        final GUIScreen guiScreen = new TestTerminalFactory(args).createGUIScreen();
        guiScreen.getScreen().startScreen();
        guiScreen.showWindow(new MultiCloseWindow());
        guiScreen.getScreen().stopScreen();
    }

    private static class MultiCloseWindow extends Window {
        public MultiCloseWindow() {
            super("Window " + WINDOW_COUNTER.incrementAndGet());
            WINDOWS.add(this);
            ActionListBox actionListBox = new ActionListBox();
            for(final Window window: WINDOWS) {
                actionListBox.addAction("Close " + window.toString(), window::close);
            }
            addComponent(actionListBox);

            Panel buttonPanel = new Panel(Panel.Orientation.HORIZONTAL);
            buttonPanel.addComponent(new EmptySpace(), LinearLayout.GROWS_HORIZONTALLY);
            buttonPanel.addComponent(new Button("New window", () -> {
                MultiCloseWindow multiCloseWindow = new MultiCloseWindow();
                getOwner().showWindow(multiCloseWindow);
            }));
            buttonPanel.addComponent(new Button("Close", MultiCloseWindow.this::close));
            addComponent(buttonPanel);
        }
    }
}
