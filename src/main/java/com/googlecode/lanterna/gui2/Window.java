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

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.TerminalSize;

import java.util.Set;

/**
 * Window is a base unit in the TextGUI system, it represents a collection of components grouped together, usually
 * surrounded by a border and a title. Modern computer system GUIs are normally based around the metaphor of windows,
 * so I don't think you should have any problems understanding what this means.
 * @author Martin
 */
public interface Window extends BasePane {
    /**
     * Window hints are meta-data stored along with the window that can be used to give the GUI system some ideas of how
     * this window wants to be treated. There are no guarantees that the hints will be honoured though. You can declare
     * your own window hints by sub-classing this class.
     */
    class Hint {
        /**
         * With this hint, the TextGUI system should not draw any decorations around the window. Decorated size will be
         * the same as the window size.
         */
        public static final Hint NO_DECORATIONS = new Hint();
        /**
         * With this hint, the TextGUI system should skip running any post renderers for the window. By default this
         * means the window won't have any shadow.
         */
        public static final Hint NO_POST_RENDERING = new Hint();
        /**
         * With this hint, the window wants to be at the center of the terminal instead of using the cascading layout
         * which is the standard.
         */
        public static final Hint CENTERED = new Hint();
        /**
         * With this hint, don't let the window grow larger than the terminal screen, rather set components to a smaller
         * size than they prefer.
         */
        public static final Hint FIT_TERMINAL_WINDOW = new Hint();
        /**
         * This hint tells the window manager that this window should have exclusive access to the keyboard input until
         * it is closed. For window managers that allows the user to switch between open windows, putting a window on
         * the screen with this hint should make the window manager temporarily disable that function until the window
         * is closed.
         */
        public static final Hint MODAL = new Hint();

        protected Hint() {
        }
    }

    @Override
    WindowBasedTextGUI getTextGUI();
    
    /**
     * DON'T CALL THIS METHOD YOURSELF, it is called automatically by the TextGUI system when you add a window. If you 
     * call it with the intention of adding the window to the specified TextGUI, you need to read the documentation
     * on how to use windows.
     * @param textGUI TextGUI this window belongs to from now on
     */
    void setTextGUI(WindowBasedTextGUI textGUI);

    /**
     * @return title of the window
     */
    String getTitle();

    /**
     * This values is optionally used by the window manager to decide if the windows should be drawn or not. In an
     * invisible state, the window is still considered active in the TextGUI but just not drawn and not receiving any
     * input events. Please note that window managers may choose not to implement this.
     *
     * @return Whether the window wants to be visible or not
     */
    boolean isVisible();

    /**
     * This method is used to determine if the window requires re-drawing. The most common cause for this is the some
     * of its components has changed and we need a re-draw to make these changes visible.
     * @return {@code true} if the window would like to be re-drawn, {@code false} if the window doesn't need
     */
    @Override
    boolean isInvalid();

    /**
     * Returns the size this window would like to be
     * @return Desired size of this window
     */
    TerminalSize getPreferredSize();

    /**
     * Closes the window, which will remove it from the GUI
     */
    void close();

    /**
     * Returns a set of window hints that can be used by the text gui system, the window manager or any other part that
     * is interacting with windows.
     * @return Set of hints defined for this window
     */
    Set<Hint> getHints();

    /**
     * Returns the position of the window, as last specified by the window manager. This position does not include
     * window decorations but is the top-left position of the first usable space of the window.
     * @return Position, relative to the top-left corner of the terminal, of the top-left corner of the window
     */
    TerminalPosition getPosition();

    /**
     * This method is called by the GUI system to update the window on where the window manager placed it. Calling this
     * yourself will have no effect other than making the {@code getPosition()} call incorrect until the next redraw.
     * @param topLeft Global coordinates of the top-left corner of the window
     */
    void setPosition(TerminalPosition topLeft);

    /**
     * Returns the last known size of the window. This is in general derived from the last drawing operation, how large
     * area the window was allowed to draw on. This size does not include window decorations.
     * @return Size of the window
     */
    TerminalSize getSize();

    /**
     * Returns the last known size of the window including window decorations put on by the window manager. The value
     * returned here is passed in during drawing by the TextGUI through {@code setDecoratedSize(..)}.
     * @return Size of the window, including window decorations
     */
    TerminalSize getDecoratedSize();

    /**
     * This method is called by the GUI system to update the window on how large it is, counting window decorations too.
     * Calling this yourself will have no effect other than making the {@code getDecoratedSize()} call incorrect until
     * the next redraw.
     * @param decoratedSize Size of the window, including window decorations
     */
    void setDecoratedSize(TerminalSize decoratedSize);

    /**
     * This method is called by the GUI system to update the window on, as of the last drawing operation, the distance
     * from the top-left position of the window including decorations to the top-left position of the actual content
     * area. If this window has no decorations, it will be always 0x0. Do not call this method yourself.
     * @param offset Offset from the top-left corner of the window (including decorations) to the top-left corner of
     *               the content area.
     */
    void setContentOffset(TerminalPosition offset);

    /**
     * Waits for the window to close. Please note that this can cause deadlocks if care is not taken. Also, this method
     * will swallow any interrupts, if you need a wait method that throws InterruptedException, you'll have to implement
     * this yourself.
     */
    void waitUntilClosed();

    ///////////////////////////////////////////////////////////////
    //// Below here are methods from BasePane                  ////
    //// We duplicate them here to make the JavaDoc more clear ////
    ///////////////////////////////////////////////////////////////
    /**
     * Called by the GUI system (or something imitating the GUI system) to draw the window. The TextGUIGraphics object
     * should be used to perform the drawing operations.
     * @param graphics TextGraphics object to draw with
     */
    @Override
    void draw(TextGUIGraphics graphics);

    /**
     * Called by the GUI system's window manager when it has decided that this window should receive the keyboard input.
     * The window will decide what to do with this input, usually sending it to one of its sub-components, but if it
     * isn't able to find any handler for this input it should return {@code false} so that the window manager can take
     * further decisions on what to do with it.
     * @param key Keyboard input
     * @return {@code true} If the window could handle the input, false otherwise
     */
    @Override
    boolean handleInput(KeyStroke key);

    /**
     * Sets the top-level component in the window, this will be the only component unless it's a container of some kind
     * that you add child-components to.
     * @param component Component to use as the top-level object in the Window
     */
    @Override
    void setComponent(Component component);

    /**
     * Returns the component which is the top-level in the component hierarchy inside this window.
     * @return Top-level component in the window
     */
    @Override
    Component getComponent();

    /**
     * Returns the component in the window that currently has input focus. There can only be one component at a time
     * being in focus.
     * @return Interactable component that is currently in receiving input focus
     */
    @Override
    Interactable getFocusedInteractable();

    /**
     * Sets the component currently in focus within this window, or sets no component in focus if {@code null}
     * is passed in.
     * @param interactable Interactable to focus, or {@code null} to clear focus
     */
    @Override
    void setFocusedInteractable(Interactable interactable);

    /**
     * Returns the position of where to put the terminal cursor according to this window. This is typically
     * derived from which component has focus, or {@code null} if no component has focus or if the window doesn't
     * want the cursor to be visible. Note that the coordinates are in local coordinate space, relative to the top-left
     * corner of the window. You can use your TextGUI implementation to translate these to global coordinates.
     * @return Local position of where to place the cursor, or {@code null} if the cursor shouldn't be visible
     */
    @Override
    TerminalPosition getCursorPosition();

    /**
     * Returns a position in the window's local coordinate space to global coordinates
     * @param localPosition The local position to translate
     * @return The local position translated to global coordinates
     */
    @Override
    TerminalPosition toGlobal(TerminalPosition localPosition);
}
