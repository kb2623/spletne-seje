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
 * This interface marks a component as able to receive keyboard input from the user.
 * @author Martin
 */
public interface Interactable extends Component {
    /**
     * Returns, in local coordinates, where to put the cursor on the screen when this component has focus. If null, the
     * cursor should be hidden. If you component is 5x1 and you want to have the cursor in the middle (when in focus),
     * return [2,0]. The GUI system will convert the position to global coordinates.
     * @return Coordinates of where to place the cursor when this component has focus
     */
    TerminalPosition getCursorLocation();

    /**
     * Accepts a KeyStroke as input and processes this as a user input. Depending on what the component does with this
     * key-stroke, there are several results passed back to the GUI system that will decide what to do next. If the
     * event was not handled or ignored, {@code Result.UNHANDLED} should be returned. This will tell the GUI system that
     * the key stroke was not understood by this component and may be dealt with in another way. If event was processed
     * properly, it should return {@code Result.HANDLED}, which will make the GUI system stop processing this particular
     * key-stroke. Furthermore, if the component understood the key-stroke and would like to move focus to a different
     * component, there are the {@code Result.MOVE_FOCUS_*} values.
     * @param keyStroke What input was entered by the user
     * @return Result of processing the key-stroke
     */
    Result handleKeyStroke(KeyStroke keyStroke);

    /**
     * Method called when this component gained keyboard focus.
     * @param direction What direction did the focus come from
     * @param previouslyInFocus Which component had focus previously ({@code null} if none)
     */
    void onEnterFocus(FocusChangeDirection direction, Interactable previouslyInFocus);

    /**
     * Method called when keyboard focus moves away from this component
     * @param direction What direction is focus going in
     * @param nextInFocus Which component is receiving focus next (or {@code null} if none)
     */
    void onLeaveFocus(FocusChangeDirection direction, Interactable nextInFocus);

    /**
     * Returns {@code true} if this component currently has input focus in its root container.
     * @return {@code true} if the interactable has input focus, {@code false} otherwise
     */
    boolean isFocused();

    /**
     * Enum to represent the various results coming out of the handleKeyStroke method
     */
    enum Result {
        /**
         * This component didn't handle the key-stroke, either because it was not recognized or because it chose to
         * ignore it.
         */
        UNHANDLED,
        /**
         * This component has handled the key-stroke and it should be considered consumed.
         */
        HANDLED,
        /**
         * This component has handled the key-stroke and requests the GUI system to switch focus to next component in
         * an ordered list of components. This should generally be returned if moving focus by using the tab key.
         */
        MOVE_FOCUS_NEXT,
        /**
         * This component has handled the key-stroke and requests the GUI system to switch focus to previous component
         * in an ordered list of components. This should generally be returned if moving focus by using the reverse tab
         * key.
         */
        MOVE_FOCUS_PREVIOUS,
        /**
         * This component has handled the key-stroke and requests the GUI system to switch focus to next component in
         * the general left direction. By convention in Lanterna, if there is no component to the left, it will move up
         * instead. This should generally be returned if moving focus by using the left array key.
         */
        MOVE_FOCUS_LEFT,
        /**
         * This component has handled the key-stroke and requests the GUI system to switch focus to next component in
         * the general right direction. By convention in Lanterna, if there is no component to the right, it will move
         * down instead. This should generally be returned if moving focus by using the right array key.
         */
        MOVE_FOCUS_RIGHT,
        /**
         * This component has handled the key-stroke and requests the GUI system to switch focus to next component in
         * the general up direction. By convention in Lanterna, if there is no component above, it will move left
         * instead. This should generally be returned if moving focus by using the up array key.
         */
        MOVE_FOCUS_UP,
        /**
         * This component has handled the key-stroke and requests the GUI system to switch focus to next component in
         * the general down direction. By convention in Lanterna, if there is no component below, it will move up
         * instead. This should generally be returned if moving focus by using the down array key.
         */
        MOVE_FOCUS_DOWN,
        ;
    }

    /**
     * When focus has changed, which direction.
     */
    enum FocusChangeDirection {
        /**
         * The next interactable component, going down. This direction usually comes from the user pressing down array.
         */
        DOWN,
        /**
         * The next interactable component, going right. This direction usually comes from the user pressing right array.
         */
        RIGHT,
        /**
         * The next interactable component, going up. This direction usually comes from the user pressing up array.
         */
        UP,
        /**
         * The next interactable component, going left. This direction usually comes from the user pressing left array.
         */
        LEFT,
        /**
         * The next interactable component, in layout manager order (usually left->right, up->down). This direction
         * usually comes from the user pressing tab key.
         */
        NEXT,
        /**
         * The previous interactable component, reversed layout manager order (usually right-left, down->up). This
         * direction usually comes from the user pressing shift and tab key (reverse tab).
         */
        PREVIOUS,
        /**
         * Focus was changed by calling the {@code RootContainer.setFocusedInteractable(..)} method directly.
         */
        TELEPORT,
        /**
         * Focus has gone away and no component is now in focus
         */
        RESET,
        ;
    }
}
