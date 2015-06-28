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

package com.googlecode.lanterna.gui.listener;

import com.googlecode.lanterna.gui.Component;
import com.googlecode.lanterna.gui.component.InteractableComponent;

/**
 *
 * @author Martin
 */
@SuppressWarnings({"EmptyMethod", "UnusedParameters"})
@Deprecated
public interface ComponentListener
{
    /**
	 * Will be called when a component is signaling that a value it is
	 * presenting has been changed
	 * 
	 * @param component the Component that's value changed
	 */
    void onComponentValueChanged(InteractableComponent component);

    /**
     * Will be called when a component is signaling it's content has been invalidated and needs to
     * be repainted by the GUI system.
     * @param component Component that was invalidated
     */
    void onComponentInvalidated(Component component);
    
    /**
     * Will be called when a component has gained the input focus of the GUI system.
     * @param interactableComponent Component that gained focus
     */
    void onComponentReceivedFocus(InteractableComponent interactableComponent);
    
    /**
     * Will be called when input focus was taken away from a component by the GUI system.
     * @param interactableComponent Component that lost focus
     */
    void onComponentLostFocus(InteractableComponent interactableComponent);
}
