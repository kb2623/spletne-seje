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

import com.googlecode.lanterna.graphics.TextGraphics;

/**
 * Classes implementing this interface can be used along with DefaultWindowManagerTextGUI to put some extra processing
 * after a window has been rendered. This is used for making window shadows but can be used for anything.
 * @author Martin
 */
public interface WindowPostRenderer {
    /**
     * Called by DefaultWindowTextGUI after a Window has been rendered, to let you do any post-rendering. You will have
     * a TextGraphics object that can draw to the whole screen.
     * @param textGraphics Graphics object you can use to draw with
     * @param textGUI TextGUI that we are in
     * @param window Window that was just rendered
     */
    void postRender(
            TextGraphics textGraphics,
            TextGUI textGUI,
            Window window);
}
