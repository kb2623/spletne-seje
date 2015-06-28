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

/**
 * BasePane is the base container in a Text GUI. A text gui may have several base panes, although they are
 * always independent. One common example of this is a multi-window system where each window is a base pane.
 * @author Martin
 */
public interface BasePane extends Composite {
    
    /**
     * Returns the TextGUI this BasePane belongs to or {@code null} if none. One example of when this method returns
     * {@code null} is when calling it on a Window that hasn't been displayed yet.
     * @return The TextGUI this BasePane belongs to
     */
    TextGUI getTextGUI();
    
    /**
     * Called by the GUI system (or something imitating the GUI system) to draw the root container. The TextGUIGraphics
     * object should be used to perform the drawing operations.
     * @param graphics TextGraphics object to draw with
     */
    void draw(TextGUIGraphics graphics);

    /**
     * Checks if this root container (i.e. any of its child components) has signaled that what it's currently displaying
     * is out of date and needs re-drawing.
     * @return {@code true} if the container's content is invalid and needs redrawing, {@code false} otherwise
     */
    boolean isInvalid();

    /**
     * Called by the GUI system to delegate a keyboard input event. The root container will decide what to do with this
     * input, usually sending it to one of its sub-components, but if it isn't able to find any handler for this input
     * it should return {@code false} so that the GUI system can take further decisions on what to do with it.
     * @param key Keyboard input
     * @return {@code true} If the root container could handle the input, false otherwise
     */
    boolean handleInput(KeyStroke key);

    /**
     * Returns the component that is the content of the BasePane. This is probably the root of a hierarchy of nested
     * Panels but it could also be a single component.
     * @return Component which is the content of this BasePane
     */
    @Override
    Component getComponent();

    /**
     * Sets the top-level component inside this BasePane. If you want it to contain only one component, you can set it
     * directly, but for more complicated GUIs you probably want to create a hierarchy of panels and set the first one
     * here.
     * @param component Component which this BasePane is using as it's content
     */
    @Override
    void setComponent(Component component);

    /**
     * Returns the component in the root container that currently has input focus. There can only be one component at a
     * time being in focus.
     * @return Interactable component that is currently in receiving input focus
     */
    Interactable getFocusedInteractable();

    /**
     * Sets the component currently in focus within this root container, or sets no component in focus if {@code null}
     * is passed in.
     * @param interactable Interactable to focus, or {@code null} to clear focus
     */
    void setFocusedInteractable(Interactable interactable);

    /**
     * Returns the position of where to put the terminal cursor according to this root container. This is typically
     * derived from which component has focus, or {@code null} if no component has focus or if the root container doesn't
     * want the cursor to be visible. Note that the coordinates are in local coordinate space, relative to the top-left
     * corner of the root container. You can use your TextGUI implementation to translate these to global coordinates.
     * @return Local position of where to place the cursor, or {@code null} if the cursor shouldn't be visible
     */
    TerminalPosition getCursorPosition();

    /**
     * Returns a position in a root container's local coordinate space to global coordinates
     * @param localPosition The local position to translate
     * @return The local position translated to global coordinates
     */
    TerminalPosition toGlobal(TerminalPosition localPosition);
}
