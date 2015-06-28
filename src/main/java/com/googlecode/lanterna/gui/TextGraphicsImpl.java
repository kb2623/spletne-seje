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

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.TabBehaviour;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;

/**
 * This class works as a kind of 'pencil', able to output text graphics. The 
 * main use for it is not only to provide a way to draw text through, it is also
 * the carrier of context information, such as which screen we are associated
 * with, what the current theme is and how big size the drawing area has.
 * @author Martin
 */
@Deprecated
class TextGraphicsImpl implements com.googlecode.lanterna.gui.TextGraphics
{
    private final TerminalPosition topLeft;
    private final TerminalSize areaSize;
    private final Screen screen;
    private final Theme theme;
    private TextColor foregroundColor;
    private TextColor backgroundColor;
    private boolean currentlyBold;

    TextGraphicsImpl(final TerminalPosition topLeft, final TerminalSize areaSize, final Screen screen, final Theme theme)
    {
        this.topLeft = topLeft;
        this.areaSize = areaSize;
        this.screen = screen;
        this.theme = theme;
        this.currentlyBold = false;
        this.foregroundColor = TextColor.ANSI.DEFAULT;
        this.backgroundColor = TextColor.ANSI.DEFAULT;
    }

    private TextGraphicsImpl(final TextGraphicsImpl graphics, final TerminalPosition topLeft, final TerminalSize areaSize)
    {
        this(new TerminalPosition(topLeft.getColumn() + graphics.topLeft.getColumn(), topLeft.getRow() + graphics.topLeft.getRow()),
                new TerminalSize(areaSize.getColumns() < (graphics.getWidth() - topLeft.getColumn()) ? areaSize.getColumns() : (graphics.getWidth() - topLeft.getColumn()),
                    areaSize.getRows() < (graphics.getHeight() - topLeft.getRow()) ? areaSize.getRows() : (graphics.getHeight() - topLeft.getRow())),
                graphics.screen, graphics.theme);
        foregroundColor = graphics.foregroundColor;
        backgroundColor = graphics.backgroundColor;
        currentlyBold = graphics.currentlyBold;
    }

    /**
     * Creates a new TextGraphics object using the same area or smaller. Use the
     * terminalPosition variable to determine what the new TextGraphics object
     * will cover. 
     * @param terminalPosition In local coordinates of the current TextGraphics,
     * the left-left coordinates of the new TextGraphics
     * @return A new TextGraphics object covering the same or smaller area as 
     * this
     */
    @Override
    public com.googlecode.lanterna.gui.TextGraphics subAreaGraphics(final TerminalPosition terminalPosition)
    {
        TerminalSize newArea;
        newArea = areaSize.withColumns(areaSize.getColumns() - terminalPosition.getColumn());
        newArea = newArea.withRows(areaSize.getRows() - terminalPosition.getRow());
        return subAreaGraphics(terminalPosition, newArea);
    }


    /**
     * Creates a new TextGraphics object using the same area or smaller. Use the
     * topLeft and subAreaSize variable to determine what the new TextGraphics 
     * object will cover. 
     * @param topLeft  In local coordinates of the current TextGraphics,
     * the left-left coordinates of the new TextGraphics
     * @param subAreaSize Size of the area the new TextGraphics will cover
     * @return A new TextGraphics object covering the same or smaller area as 
     * this
     */
    @Override
    public com.googlecode.lanterna.gui.TextGraphics subAreaGraphics(TerminalPosition topLeft, TerminalSize subAreaSize)
    {
        if(topLeft.getColumn() >= areaSize.getColumns() ||
                topLeft.getRow() >= areaSize.getRows()) {
            return new NullTextGraphics();  //Return something that doesn't do anything
        }
        
        if(topLeft.getColumn() + subAreaSize.getColumns() > areaSize.getColumns())
            subAreaSize = subAreaSize.withColumns(areaSize.getColumns() - topLeft.getColumn());
        if(topLeft.getRow() + subAreaSize.getRows() > areaSize.getRows())
            subAreaSize = subAreaSize.withRows(areaSize.getRows() - topLeft.getRow());

        return new TextGraphicsImpl(this, topLeft, subAreaSize);
    }

    /**
     * Draws a string to the terminal, with the first character starting at 
     * specified coordinates and which an option list of styles applied. All
     * coordinates are local to the top-left corner of the TextGraphics object
     * @param column Column of the first character in the string
     * @param row Row of the first character in the string
     * @param string String to print to terminal
     * @param styles Which styles to apply to the string
     */
    @Override
    public void drawString(int column, int row, String string, SGR... styles)
    {
        if(column >= areaSize.getColumns() || row >= areaSize.getRows() || string == null)
            return;

        string = TabBehaviour.ALIGN_TO_COLUMN_4.replaceTabs(string, column + topLeft.getColumn());
        
        if(string.length() + column > areaSize.getColumns())
            string = string.substring(0, areaSize.getColumns() - column);

        TextGraphics screenWriter = screen.newTextGraphics();
        screenWriter.setForegroundColor(foregroundColor);
        screenWriter.setBackgroundColor(backgroundColor);
        screenWriter.enableModifiers(styles);
        if(currentlyBold) {
            screenWriter.enableModifiers(SGR.BOLD);
        }
        screenWriter.putString(column + topLeft.getColumn(), row + topLeft.getRow(), string);
    }

    @Override
    public TextColor getBackgroundColor()
    {
        return backgroundColor;
    }

    @Override
    public TextColor getForegroundColor()
    {
        return foregroundColor;
    }

    @Override
    public void setBackgroundColor(TextColor backgroundColor)
    {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public void setForegroundColor(TextColor foregroundColor)
    {
        this.foregroundColor = foregroundColor;
    }

    /**
     * Width, in columns, of the TextGraphics drawing area. Attemps to draw 
     * outside of this will be ignored
     * @return Size of the TextGraphics area, in columns
     */
    @Override
    public int getWidth()
    {
        return areaSize.getColumns();
    }

    /**
     * Height, in rows, of the TextGraphics drawing area. Attemps to draw 
     * outside of this will be ignored
     * @return Size of the TextGraphics area, in rows
     */
    @Override
    public int getHeight()
    {
        return areaSize.getRows();
    }

    /**
     * Size of the area the {@code TextGraphics} can edit, as a {@code TerminalSize} object, 
     * any attempts to draw outside of this area will be ignored.
     * @return Size of the area the {@code TextGraphics} can edit, as a {@code TerminalSize} object
     */
    @Override
    public TerminalSize getSize() {
        return new TerminalSize(getWidth(), getHeight());
    }

    @Override
    public void setBoldMask(boolean enabledBoldMask)
    {
        currentlyBold = enabledBoldMask;
    }

    @Override
    public Theme getTheme()
    {
        return theme;
    }

    /**
     * Translates local coordinates of this TextGraphics object to global
     * @param pointInArea Point in local coordinates
     * @return The point in global coordinates
     */
    @Override
    public TerminalPosition translateToGlobalCoordinates(TerminalPosition pointInArea)
    {
        return new TerminalPosition(pointInArea.getColumn() + topLeft.getColumn(), pointInArea.getRow() + topLeft.getRow());
    }

    /**
     * Applies theme-specific settings according to the category supplied. This
     * may modify the foreground color, background color and/or styles. Any 
     * string drawn after this call will have these settings applied
     * @param category Category to use
     */
    @Override
    public void applyTheme(Theme.Category category)
    {
        applyTheme(getTheme().getDefinition(category));
    }
    

    /**
     * Applies theme-specific settings according to the definition supplied. This
     * may modify the foreground color, background color and/or styles. Any 
     * string drawn after this call will have these settings applied
     * @param themeItem Definition to use
     */
    @Override
    public void applyTheme(Theme.Definition themeItem)
    {
        setForegroundColor(themeItem.foreground());
        setBackgroundColor(themeItem.background());
        setBoldMask(themeItem.isHighlighted());
    }

    /**
     * Replaces the content of the entire TextGraphic object with one character
     * @param character Character to fill the area with
     */
    @Override
    public void fillArea(char character)
    {
        fillRectangle(character, new TerminalPosition(0, 0), areaSize);
    }

    /**
     * Replaces the content of a rectangle within the TextGraphic drawing area
     * with a specified character
     * @param character Character to fill the area with
     */
    @Override
    public void fillRectangle(char character, TerminalPosition topLeft, TerminalSize rectangleSize)
    {
        StringBuilder emptyLineBuilder = new StringBuilder();
        for(int i = 0; i < rectangleSize.getColumns(); i++)
            emptyLineBuilder.append(character);
        String emptyLine = emptyLineBuilder.toString();
        for(int i = 0; i < rectangleSize.getRows(); i++)
            drawString(topLeft.getColumn(), topLeft.getRow() + i, emptyLine);
    }

    @Override
    public String toString()
    {
        return "TextGraphics {topLeft: " + topLeft.toString() + ", size: " + areaSize.toString() + "}";
    }
    
    private static class NullTextGraphics implements com.googlecode.lanterna.gui.TextGraphics {
        @Override
        public void applyTheme(Theme.Category category) { }

        @Override
        public void applyTheme(Theme.Definition themeItem) { }

        @Override
        public void drawString(int column, int row, String string, SGR... styles) { }

        @Override
        public void fillArea(char character) { }

        @Override
        public void fillRectangle(char character, TerminalPosition topLeft, TerminalSize rectangleSize) { }

        @Override
        public TextColor getBackgroundColor() { return TextColor.ANSI.BLACK; }

        @Override
        public TextColor getForegroundColor() { return TextColor.ANSI.WHITE; }

        @Override
        public int getHeight() { return 0; }

        @Override
        public TerminalSize getSize() { return new TerminalSize(0, 0); }

        @Override
        public Theme getTheme() { return Theme.getDefaultTheme(); }

        @Override
        public int getWidth() { return 0; }

        @Override
        public void setBackgroundColor(TextColor backgroundColor) { }

        @Override
        public void setBoldMask(boolean enabledBoldMask) { }

        @Override
        public void setForegroundColor(TextColor foregroundColor) { }

        @Override
        public com.googlecode.lanterna.gui.TextGraphics subAreaGraphics(TerminalPosition terminalPosition) {
            return new NullTextGraphics();
        }

        @Override
        public com.googlecode.lanterna.gui.TextGraphics subAreaGraphics(TerminalPosition topLeft, TerminalSize subAreaSize) {
            return new NullTextGraphics();
        }

        @Override
        public TerminalPosition translateToGlobalCoordinates(TerminalPosition pointInArea) {
            return new TerminalPosition(0, 0);
        }

    }

}
