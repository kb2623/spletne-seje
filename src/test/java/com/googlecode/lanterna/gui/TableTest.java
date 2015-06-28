/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.googlecode.lanterna.gui;

import com.googlecode.lanterna.gui.component.Button;
import com.googlecode.lanterna.gui.component.CheckBox;
import com.googlecode.lanterna.gui.component.EmptySpace;
import com.googlecode.lanterna.gui.component.Label;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.component.ProgressBar;
import com.googlecode.lanterna.gui.component.Table;
import com.googlecode.lanterna.gui.component.TextBox;
import com.googlecode.lanterna.gui.layout.LinearLayout;
import com.googlecode.lanterna.TestTerminalFactory;
import java.io.IOException;
import java.util.Random;

/**
 *
 * @author Martin
 */
public class TableTest {

    public static void main(String[] args) throws IOException
    {
        final GUIScreen guiScreen = new TestTerminalFactory(args).createGUIScreen();
        guiScreen.getScreen().startScreen();
        final Window window1 = new Window("Text box window");

        final Table table = new Table(5, "My Test Table");
        table.setColumnPaddingSize(1);
        table.addRow(new Label("Column 1 "),
                new Label("Column 2 "),
                new Label("Column 3 "),
                new Label("Column 4 "),
                new Label("Column 5"));
        table.addRow(new TextBox("Here's a text box"),
                new CheckBox("checkbox", false),
                new ProgressBar(10),
                new EmptySpace(),
                new Button("Progress", () -> ((ProgressBar)table.getRow(1)[2]).setProgress(new Random().nextDouble())));


        window1.addComponent(table);
        Panel buttonPanel = new Panel(Panel.Orientation.HORIZONTAL);
        buttonPanel.addComponent(new EmptySpace(1, 1), LinearLayout.MAXIMIZES_HORIZONTALLY);
        Button exitButton = new Button("Exit", window1::close);
        buttonPanel.addComponent(exitButton);
        window1.addComponent(buttonPanel);
        guiScreen.showWindow(window1, GUIScreen.Position.CENTER);
        guiScreen.getScreen().stopScreen();
    }
}
