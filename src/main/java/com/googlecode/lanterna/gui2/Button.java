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
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.ThemeDefinition;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

/**
 * Simple labeled button with an option action attached to it. You trigger the action by pressing the Enter key on the
 * keyboard.
 * @author Martin
 */
public class Button extends AbstractInteractableComponent<Button> {
    private final Runnable action;
    private String label;

    public Button(String label) {
        this(label, () -> {});
    }

    public Button(String label, Runnable action) {
        this.action = action;
        setLabel(label);
    }

    @Override
    protected ButtonRenderer createDefaultRenderer() {
        return new DefaultButtonRenderer();
    }

    @Override
    public TerminalPosition getCursorLocation() {
        return getRenderer().getCursorLocation(this);
    }

    @Override
    public Result handleKeyStroke(KeyStroke keyStroke) {
        if(keyStroke.getKeyType() == KeyType.Enter) {
            action.run();
            return Result.HANDLED;
        }
        return super.handleKeyStroke(keyStroke);
    }

    public final void setLabel(String label) {
        if(label == null) {
            throw new IllegalArgumentException("null label to a button is not allowed");
        }
        if(label.isEmpty()) {
            label = " ";
        }
        this.label = label;
        invalidate();
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return "Button{" + label + "}";
    }

    public static abstract class ButtonRenderer implements InteractableRenderer<Button> {
    }

    public static class DefaultButtonRenderer extends ButtonRenderer {
        @Override
        public TerminalPosition getCursorLocation(Button button) {
            return new TerminalPosition(1 + getLabelShift(button, button.getSize()), 0);
        }

        @Override
        public TerminalSize getPreferredSize(Button button) {
            return new TerminalSize(Math.max(8, button.getLabel().length() + 2), 1);
        }

        @Override
        public void drawComponent(TextGUIGraphics graphics, Button button) {
            if(button.isFocused()) {
                graphics.applyThemeStyle(getThemeDefinition(graphics).getActive());
            }
            else {
                graphics.applyThemeStyle(getThemeDefinition(graphics).getInsensitive());
            }
            graphics.fill(' ');
            graphics.setCharacter(0, 0, getThemeDefinition(graphics).getCharacter("LEFT_BORDER", '<'));
            graphics.setCharacter(graphics.getSize().getColumns() - 1, 0, getThemeDefinition(graphics).getCharacter("RIGHT_BORDER", '>'));

            if(button.isFocused()) {
                graphics.applyThemeStyle(getThemeDefinition(graphics).getActive());
            }
            else {
                graphics.applyThemeStyle(getThemeDefinition(graphics).getPreLight());
            }
            int labelShift = getLabelShift(button, graphics.getSize());
            graphics.setCharacter(1 + labelShift, 0, button.getLabel().charAt(0));

            if(button.getLabel().length() == 1) {
                return;
            }
            if(button.isFocused()) {
                graphics.applyThemeStyle(getThemeDefinition(graphics).getSelected());
            }
            else {
                graphics.applyThemeStyle(getThemeDefinition(graphics).getNormal());
            }
            graphics.putString(1 + labelShift + 1, 0, button.getLabel().substring(1));
        }

        private int getLabelShift(Button button, TerminalSize size) {
            int availableSpace = size.getColumns() - 2;
            if(availableSpace <= 0) {
                return 0;
            }
            int labelShift = 0;
            if(availableSpace > button.getLabel().length()) {
                labelShift = (size.getColumns() - 2 - button.getLabel().length()) / 2;
            }
            return labelShift;
        }
    }

    private static ThemeDefinition getThemeDefinition(TextGUIGraphics graphics) {
        return graphics.getThemeDefinition(Button.class);
    }
}
