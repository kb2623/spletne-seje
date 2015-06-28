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
import java.util.EnumMap;
import java.util.List;

/**
 * BorderLayout imitates the BorderLayout class from AWT, allowing you to add a center component with optional 
 * components around it in top, bottom, left and right locations. The edge components will be sized at their preferred
 * size and the center component will take up whatever remains.
 * @author martin
 */
public class BorderLayout implements LayoutManager {
    public enum Location implements LayoutData {
        CENTER,
        LEFT,
        RIGHT,
        TOP,
        BOTTOM,
        ;
    }
    
    @Override
    public TerminalSize getPreferredSize(List<Component> components) {
        EnumMap<Location, Component> layout = makeLookupMap(components);
        int preferredHeight = 
                (layout.containsKey(Location.TOP) ? layout.get(Location.TOP).getPreferredSize().getRows() : 0)
                +
                Math.max(
                    layout.containsKey(Location.LEFT) ? layout.get(Location.LEFT).getPreferredSize().getRows() : 0,
                    Math.max(
                        layout.containsKey(Location.CENTER) ? layout.get(Location.CENTER).getPreferredSize().getRows() : 0,
                        layout.containsKey(Location.RIGHT) ? layout.get(Location.RIGHT).getPreferredSize().getRows() : 0))
                +
                (layout.containsKey(Location.BOTTOM) ? layout.get(Location.BOTTOM).getPreferredSize().getRows() : 0);

        int preferredWidth = 
                Math.max(
                    (layout.containsKey(Location.LEFT) ? layout.get(Location.LEFT).getPreferredSize().getColumns() : 0) +
                        (layout.containsKey(Location.CENTER) ? layout.get(Location.CENTER).getPreferredSize().getColumns() : 0) +
                        (layout.containsKey(Location.RIGHT) ? layout.get(Location.RIGHT).getPreferredSize().getColumns() : 0),
                    Math.max(
                        layout.containsKey(Location.TOP) ? layout.get(Location.TOP).getPreferredSize().getColumns() : 0,
                        layout.containsKey(Location.BOTTOM) ? layout.get(Location.BOTTOM).getPreferredSize().getColumns() : 0));
        return new TerminalSize(preferredWidth, preferredHeight);
    }

    @Override
    public void doLayout(TerminalSize area, List<Component> components) {
        EnumMap<Location, Component> layout = makeLookupMap(components);
        int availableHorizontalSpace = area.getColumns();
        int availableVerticalSpace = area.getRows();
        
        //We'll need this later on
        int topComponentHeight = 0;
        int leftComponentWidth = 0;

        //First allocate the top
        if(layout.containsKey(Location.TOP)) {
            Component topComponent = layout.get(Location.TOP);
            topComponentHeight = Math.min(topComponent.getPreferredSize().getRows(), availableVerticalSpace);
            topComponent.setPosition(TerminalPosition.TOP_LEFT_CORNER);
            topComponent.setSize(new TerminalSize(availableHorizontalSpace, topComponentHeight));
            availableVerticalSpace -= topComponentHeight;
        }

        //Next allocate the bottom
        if(layout.containsKey(Location.BOTTOM)) {
            Component bottomComponent = layout.get(Location.BOTTOM);
            int bottomComponentHeight = Math.min(bottomComponent.getPreferredSize().getRows(), availableVerticalSpace);
            bottomComponent.setPosition(new TerminalPosition(0, area.getRows() - bottomComponentHeight));
            bottomComponent.setSize(new TerminalSize(availableHorizontalSpace, bottomComponentHeight));
            availableVerticalSpace -= bottomComponentHeight;
        }

        //Now divide the remaining space between LEFT, CENTER and RIGHT
        if(layout.containsKey(Location.LEFT)) {
            Component leftComponent = layout.get(Location.LEFT);
            leftComponentWidth = Math.min(leftComponent.getPreferredSize().getColumns(), availableHorizontalSpace);
            leftComponent.setPosition(new TerminalPosition(0, topComponentHeight));
            leftComponent.setSize(new TerminalSize(leftComponentWidth, availableVerticalSpace));
            availableHorizontalSpace -= leftComponentWidth;
        }
        if(layout.containsKey(Location.RIGHT)) {
            Component rightComponent = layout.get(Location.RIGHT);
            int rightComponentWidth = Math.min(rightComponent.getPreferredSize().getColumns(), availableHorizontalSpace);
            rightComponent.setPosition(new TerminalPosition(area.getColumns() - rightComponentWidth, topComponentHeight));
            rightComponent.setSize(new TerminalSize(rightComponentWidth, availableVerticalSpace));
            availableHorizontalSpace -= rightComponentWidth;
        }
        if(layout.containsKey(Location.CENTER)) {
            Component centerComponent = layout.get(Location.CENTER);
            centerComponent.setPosition(new TerminalPosition(leftComponentWidth, topComponentHeight));
            centerComponent.setSize(new TerminalSize(availableHorizontalSpace, availableVerticalSpace));
        }
        
        //Set the remaining components to 0x0
        components.stream().filter(component -> !layout.values().contains(component)).forEach(component -> {
            component.setPosition(TerminalPosition.TOP_LEFT_CORNER);
            component.setSize(TerminalSize.ZERO);
        });
    }
    
    private EnumMap<Location, Component> makeLookupMap(List<Component> components) {
        EnumMap<Location, Component> map = new EnumMap<>(Location.class);
        components.stream().filter(component -> component.getLayoutData() instanceof Location).forEach(component -> map.put((Location) component.getLayoutData(), component));
        return map;
    }
}
