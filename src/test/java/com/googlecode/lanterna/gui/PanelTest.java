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
import com.googlecode.lanterna.gui.layout.LinearLayout;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TestTerminalFactory;
import java.io.IOException;

/**
 *
 * @author Martin
 */
public class PanelTest
{
    public static void main(String[] args) throws IOException 
    {
        final GUIScreen guiScreen = new TestTerminalFactory(args).createGUIScreen();
        guiScreen.getScreen().startScreen();
        guiScreen.setBackgroundRenderer(new DefaultBackgroundRenderer("GUI Test"));
        final Window mainWindow = new Window("Window with panels");
        TextFillComponent oneComponent = new TextFillComponent(5,5, '1');
        TextFillComponent xComponent = new TextFillComponent(5,5, 'X');
        TextFillComponent twoComponent = new TextFillComponent(5,5, '2');
        Panel componentPanel = new Panel(Panel.Orientation.VERTICAL);
        componentPanel.addComponent(oneComponent);
        componentPanel.addComponent(xComponent, LinearLayout.MAXIMIZES_VERTICALLY);
        componentPanel.addComponent(twoComponent);
        mainWindow.addComponent(componentPanel, LinearLayout.MAXIMIZES_VERTICALLY);
        mainWindow.addComponent(new Button("Close", mainWindow::close));

        guiScreen.showWindow(mainWindow, GUIScreen.Position.FULL_SCREEN);
        guiScreen.getScreen().stopScreen();
    }

    private static class TextFillComponent extends AbstractComponent
    {
        private final TerminalSize preferredSize;
        private final char fillCharacter;

        public TextFillComponent(int width, int height, char fillCharacter)
        {
            this.preferredSize = new TerminalSize(width, height);
            this.fillCharacter = fillCharacter;
        }

        @Override
        public TerminalSize calculatePreferredSize()
        {
            return preferredSize;
        }

        @Override
        public boolean isScrollable()
        {
            return true;
        }

        @Override
        public void repaint(TextGraphics graphics)
        {
            StringBuilder sb = new StringBuilder();
            graphics.applyTheme(Category.DIALOG_AREA);
            for(int i = 0; i < graphics.getWidth(); i++)
                sb.append(fillCharacter);
            for(int i = 0; i < graphics.getHeight(); i++)
                graphics.drawString(0, i, sb.toString());
        }
    }
}
