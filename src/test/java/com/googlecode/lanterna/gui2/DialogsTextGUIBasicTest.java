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
package com.googlecode.lanterna.gui2;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TestTerminalFactory;
import com.googlecode.lanterna.gui2.dialogs.TextInputDialog;
import com.googlecode.lanterna.gui2.dialogs.TextInputDialogBuilder;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 *
 * @author Martin
 */
public class DialogsTextGUIBasicTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        Screen screen = new TestTerminalFactory(args).createScreen();
        screen.startScreen();
        final WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
        try {
            final BasicWindow window = new BasicWindow("Dialog test");

            Panel mainPanel = new Panel();
            ActionListBox dialogsListBox = new ActionListBox();
            dialogsListBox.addItem("Simple TextInputDialog", () -> {
                String result = TextInputDialog.showDialog(textGUI, "TextInputDialog sample", "This is the description", "initialContent");
                System.out.println("Result was: " + result);
            });
            dialogsListBox.addItem("Password input", () -> {
                String result = TextInputDialog.showPasswordDialog(textGUI, "Test password input", "This is a password input dialog", "");
                System.out.println("Result was: " + result);
            });
            dialogsListBox.addItem("Multi-line input", () -> {
                String result = new TextInputDialogBuilder()
                        .setTitle("Multi-line editor")
                        .setTextBoxSize(new TerminalSize(35, 5))
                        .build()
                        .showDialog(textGUI);
                System.out.println("Result was: " + result);
            });
            dialogsListBox.addItem("Numeric input", () -> {
                String result = new TextInputDialogBuilder()
                        .setTitle("Numeric input")
                        .setDescription("Enter a number")
                        .setValidationPattern(Pattern.compile("[0-9]+"))
                        .setPatternMissmatchErrorMessage("Please enter a valid number")
                        .build()
                        .showDialog(textGUI);
                System.out.println("Result was: " + result);
            });

            mainPanel.addComponent(dialogsListBox);
            mainPanel.addComponent(new EmptySpace(TerminalSize.ONE));
            mainPanel.addComponent(new Button("Exit", window::close));
            window.setComponent(mainPanel);

            textGUI.addWindowAndWait(window);
        } finally {
            screen.stopScreen();
        }
    }
}
