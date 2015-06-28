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
import com.googlecode.lanterna.gui.dialog.MessageBox;
import com.googlecode.lanterna.TestTerminalFactory;
import java.io.IOException;

/**
 *
 * @author Martin
 */
public class ActionListTest
{
    public static void main(String[] args) throws IOException
    {
        final GUIScreen guiScreen = new TestTerminalFactory(args).createGUIScreen();
        guiScreen.getScreen().startScreen();
        final Window window1 = new Window("Text box window");
        //window1.addComponent(new Widget(1, 1));

        Panel mainPanel = new Panel(new Border.Invisible(), Panel.Orientation.VERTICAL);
        ActionListBox actionListBox = new ActionListBox();
        for(int i = 0; i < 5; i++)
            actionListBox.addAction(new ActionListBoxItem(guiScreen));

        mainPanel.addComponent(actionListBox);
        window1.addComponent(mainPanel);

        Panel buttonPanel = new Panel(new Border.Invisible(), Panel.Orientation.HORIZONTAL);
        Button exitButton = new Button("Exit", window1::close);
        buttonPanel.addComponent(new EmptySpace(20, 1));
        buttonPanel.addComponent(exitButton);
        window1.addComponent(buttonPanel);
        guiScreen.showWindow(window1, GUIScreen.Position.CENTER);
        guiScreen.getScreen().stopScreen();
    }

    private static class ActionListBoxItem implements Action {
        private static int counter = 1;
        private GUIScreen owner;
        private int nr;

        public ActionListBoxItem(GUIScreen owner) {
            this.nr = counter++;
            this.owner = owner;
        }

        @Override
        public String toString() {
            return "ActionListBox item #" + nr;
        }

        public void doAction() throws IOException {
            MessageBox.showMessageBox(owner, "Action", "Selected " + toString());
        }
    }
}

class MyWindow extends Window
{
    public MyWindow()
    {
        super("My Window!");
        Panel horisontalPanel = new Panel(new Border.Invisible(), Panel.Orientation.HORIZONTAL);
        Panel leftPanel = new Panel(new Border.Bevel(true), Panel.Orientation.HORIZONTAL);
        Panel middlePanel = new Panel(new Border.Bevel(true), Panel.Orientation.HORIZONTAL);
        Panel rightPanel = new Panel(new Border.Bevel(true), Panel.Orientation.HORIZONTAL);

        horisontalPanel.addComponent(leftPanel);
        horisontalPanel.addComponent(middlePanel);
        horisontalPanel.addComponent(rightPanel);

        addComponent(horisontalPanel);
    }
}
