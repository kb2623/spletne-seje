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
import com.googlecode.lanterna.input.KeyStroke;

public abstract class AbstractBasePane implements BasePane {
    protected final ContentHolder contentHolder;
    protected InteractableLookupMap interactableLookupMap;
    private Interactable focusedInteractable;
    private boolean invalid;
    private boolean strictFocusChange;

    protected AbstractBasePane() {
        this.contentHolder = new ContentHolder();
        this.interactableLookupMap = new InteractableLookupMap(new TerminalSize(80, 25));
        this.invalid = false;
        this.strictFocusChange = false;
    }

    @Override
    public boolean isInvalid() {
        return invalid || contentHolder.isInvalid();
    }

    protected void invalidate() {
        invalid = true;
    }

    @Override
    public void draw(TextGUIGraphics graphics) {
        graphics.applyThemeStyle(graphics.getThemeDefinition(Window.class).getNormal());
        graphics.fill(' ');
        contentHolder.draw(graphics);

        if(!interactableLookupMap.getSize().equals(graphics.getSize())) {
            interactableLookupMap = new InteractableLookupMap(graphics.getSize());
        }
        interactableLookupMap.reset();
        contentHolder.updateLookupMap(interactableLookupMap);
        //interactableLookupMap.debug();
        invalid = false;
    }

    @Override
    public boolean handleInput(KeyStroke key) {
        if(focusedInteractable != null) {
            Interactable next = null;
            Interactable.FocusChangeDirection direction = Interactable.FocusChangeDirection.TELEPORT; //Default
            switch (focusedInteractable.handleKeyStroke(key)) {
                case HANDLED:
                    return true;
                case UNHANDLED:
                    //Filter the event recursively through all parent containers until we hit null; give the containers
                    //a chance to absorb the event
                    Container parent = focusedInteractable.getParent();
                    while(parent != null) {
                        if(parent.handleInput(key)) {
                            return true;
                        }
                        parent = parent.getParent();
                    }
                    return false;
                case MOVE_FOCUS_NEXT:
                    next = contentHolder.nextFocus(focusedInteractable);
                    if(next == null) {
                        next = contentHolder.nextFocus(null);
                    }
                    direction = Interactable.FocusChangeDirection.NEXT;
                    break;
                case MOVE_FOCUS_PREVIOUS:
                    next = contentHolder.previousFocus(focusedInteractable);
                    if(next == null) {
                        next = contentHolder.previousFocus(null);
                    }
                    direction = Interactable.FocusChangeDirection.PREVIOUS;
                    break;
                case MOVE_FOCUS_DOWN:
                    next = interactableLookupMap.findNextDown(focusedInteractable);
                    direction = Interactable.FocusChangeDirection.DOWN;
                    if(next == null && !strictFocusChange) {
                        next = contentHolder.nextFocus(focusedInteractable);
                        direction = Interactable.FocusChangeDirection.NEXT;
                    }
                    break;
                case MOVE_FOCUS_LEFT:
                    next = interactableLookupMap.findNextLeft(focusedInteractable);
                    direction = Interactable.FocusChangeDirection.LEFT;
                    break;
                case MOVE_FOCUS_RIGHT:
                    next = interactableLookupMap.findNextRight(focusedInteractable);
                    direction = Interactable.FocusChangeDirection.RIGHT;
                    break;
                case MOVE_FOCUS_UP:
                    next = interactableLookupMap.findNextUp(focusedInteractable);
                    direction = Interactable.FocusChangeDirection.UP;
                    if(next == null && !strictFocusChange) {
                        next = contentHolder.previousFocus(focusedInteractable);
                        direction = Interactable.FocusChangeDirection.PREVIOUS;
                    }
                    break;
            }
            if(next != null) {
                setFocusedInteractable(next, direction);
            }
            return true;
        }
        return false;
    }

    @Override
    public Component getComponent() {
        return contentHolder.getComponent();
    }

    @Override
    public void setComponent(Component component) {
        contentHolder.setComponent(component);
    }

    @Override
    public Interactable getFocusedInteractable() {
        return focusedInteractable;
    }

    @Override
    public TerminalPosition getCursorPosition() {
        if(focusedInteractable == null) {
            return null;
        }
        TerminalPosition position = focusedInteractable.getCursorLocation();
        if(position == null) {
            return null;
        }
        //Don't allow the component to set the cursor outside of its own boundaries
        if(position.getColumn() < 0 ||
                position.getRow() < 0 ||
                position.getColumn() >= focusedInteractable.getSize().getColumns() ||
                position.getRow() >= focusedInteractable.getSize().getRows()) {
            return null;
        }
        return focusedInteractable.toBasePane(position);
    }

    @Override
    public void setFocusedInteractable(Interactable toFocus) {
        setFocusedInteractable(toFocus,
                toFocus != null ?
                    Interactable.FocusChangeDirection.TELEPORT : Interactable.FocusChangeDirection.RESET);
    }

    protected void setFocusedInteractable(Interactable toFocus, Interactable.FocusChangeDirection direction) {
        if(focusedInteractable != null) {
            focusedInteractable.onLeaveFocus(direction, focusedInteractable);
        }
        Interactable previous = focusedInteractable;
        focusedInteractable = toFocus;
        if(toFocus != null) {
            toFocus.onEnterFocus(direction, previous);
        }
    }

    /**
     * If set to true, up/down array keys will not translate to next/previous if there are no more components
     * above/below.
     * @param strictFocusChange Will not allow relaxed navigation if set to {@code true}
     */
    public void setStrictFocusChange(boolean strictFocusChange) {
        this.strictFocusChange = strictFocusChange;
    }

    protected class ContentHolder extends AbstractComposite<Container> {
        @Override
        public void setComponent(Component component) {
            super.setComponent(component);
            if(focusedInteractable == null && component instanceof Interactable) {
                setFocusedInteractable((Interactable)component);
            }
            else if(focusedInteractable == null && component instanceof Container) {
                setFocusedInteractable(((Container)component).nextFocus(null));
            }
        }

        @Override
        protected ComponentRenderer<Container> createDefaultRenderer() {
            return new ComponentRenderer<Container>() {
                @Override
                public TerminalSize getPreferredSize(Container component) {
                    Component subComponent = getComponent();
                    if(subComponent == null) {
                        return TerminalSize.ZERO;
                    }
                    return subComponent.getPreferredSize();
                }

                @Override
                public void drawComponent(TextGUIGraphics graphics, Container component) {
                    Component subComponent = getComponent();
                    if(subComponent == null) {
                        return;
                    }
                    subComponent.draw(graphics);
                }
            };
        }

        @Override
        public TerminalPosition toBasePane(TerminalPosition position) {
            return position;
        }

        @Override
        public BasePane getBasePane() {
            return AbstractBasePane.this;
        }
    }
}
