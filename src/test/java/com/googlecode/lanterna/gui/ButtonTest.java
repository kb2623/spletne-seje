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

import com.googlecode.lanterna.gui.Theme.Category;
import com.googlecode.lanterna.gui.component.AbstractComponent;
import com.googlecode.lanterna.gui.component.Button;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.terminal.ansi.UnixTerminal;
import com.googlecode.lanterna.TestTerminalFactory;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 *
 * @author Martin
 */
public class ButtonTest
{
    public static void main(String[] args) throws IOException
    {
        Terminal terminal = new TestTerminalFactory(args).createTerminal();
        if(terminal instanceof UnixTerminal) {
            terminal = new UnixTerminal(System.in, System.out, Charset.forName("UTF-8"),
                                            null, UnixTerminal.CtrlCBehaviour.CTRL_C_KILLS_APPLICATION);
        }
        final GUIScreen guiScreen = new GUIScreen(new TerminalScreen(terminal));
        guiScreen.getScreen().startScreen();
        guiScreen.setBackgroundRenderer(new DefaultBackgroundRenderer("GUI Test"));

        final Window mainWindow = new Window("Window with panels");
        mainWindow.addComponent(new AbstractComponent() {
            @Override
            public void repaint(TextGraphics graphics)
            {
                graphics.applyTheme(graphics.getTheme().getDefinition(Category.SHADOW));
                for(int y = 0; y < graphics.getHeight(); y++)
                    for(int x = 0; x < graphics.getWidth(); x++)
                        graphics.drawString(x, y, "X");
            }

            @Override
            protected TerminalSize calculatePreferredSize() {
                return new TerminalSize(20, 6);
            }
        });
        Panel buttonPanel = new Panel(Panel.Orientation.HORIZONTAL);
        Button button1 = new Button("Button1", mainWindow::close);
        Button button2 = new Button("Button2");
        Button button3 = new Button("Button3");
        buttonPanel.addComponent(button1);
        buttonPanel.addComponent(button2);
        buttonPanel.addComponent(button3);
        mainWindow.addComponent(buttonPanel);

        guiScreen.showWindow(mainWindow, GUIScreen.Position.CENTER);
        guiScreen.getScreen().stopScreen();
    }
}
