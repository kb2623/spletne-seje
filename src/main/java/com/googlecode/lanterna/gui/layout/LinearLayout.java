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
package com.googlecode.lanterna.gui.layout;

import com.googlecode.lanterna.gui.Component;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Martin
 */
@SuppressWarnings({"WeakerAccess", "MismatchedQueryAndUpdateOfCollection"})
@Deprecated
public abstract class LinearLayout implements LayoutManager {

    public static final LayoutParameter MAXIMIZES_HORIZONTALLY = new LayoutParameter("LinearLayout.MAXIMIZES_HORIZONTALLY");
    public static final LayoutParameter MAXIMIZES_VERTICALLY = new LayoutParameter("LinearLayout.MAXIMIZES_VERTICALLY");
    public static final LayoutParameter GROWS_HORIZONTALLY = new LayoutParameter("LinearLayout.GROWS_HORIZONTALLY");
    public static final LayoutParameter GROWS_VERTICALLY = new LayoutParameter("LinearLayout.GROWS_VERTICALLY");
    private final List<LinearLayoutComponent> componentList;
    private int padding;

    LinearLayout() {
        this.componentList = new ArrayList<>();
        this.padding = 0;
    }

    @Override
    public void addComponent(Component component, LayoutParameter... layoutParameters) {
        Set<LayoutParameter> asSet = new HashSet<>(Arrays.asList(layoutParameters));
        if(asSet.contains(MAXIMIZES_HORIZONTALLY) && asSet.contains(GROWS_HORIZONTALLY))
            throw new IllegalArgumentException("Component " + component + 
                    " cannot be both maximizing and growing horizontally at the same time");
        
        if(asSet.contains(MAXIMIZES_VERTICALLY) && asSet.contains(GROWS_VERTICALLY))
            throw new IllegalArgumentException("Component " + component + 
                    " cannot be both maximizing and growing vertically at the same time");
        
        componentList.add(new LinearLayoutComponent(component, asSet));
    }

    @Override
    public void removeComponent(Component component) {
        Iterator<LinearLayoutComponent> iterator = componentList.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().component == component) {
                iterator.remove();
                return;
            }
        }
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    @Override
    public TerminalSize getPreferredSize() {
        if(componentList.isEmpty())
            return new TerminalSize(0, 0);
        
        TerminalSize preferredSize = new TerminalSize(0, 0);
        for (LinearLayoutComponent axisLayoutComponent : componentList) {
            final TerminalSize componentPreferredSize = axisLayoutComponent.component.getPreferredSize();
            preferredSize = setMajorAxis(preferredSize, getMajorAxis(preferredSize) + getMajorAxis(componentPreferredSize));
            preferredSize = setMinorAxis(preferredSize, Math.max(getMinorAxis(preferredSize), getMinorAxis(componentPreferredSize)));
        }
        preferredSize = setMajorAxis(preferredSize, getMajorAxis(preferredSize) + (padding * (componentList.size() - 1)));
        return preferredSize;
    }

    @Override
    public List<? extends LaidOutComponent> layout(TerminalSize layoutArea) {
        List<DefaultLaidOutComponent> result = new ArrayList<>();
        Map<Component, TerminalSize> minimumSizeMap = new IdentityHashMap<>();
        Map<Component, TerminalSize> preferredSizeMap = new IdentityHashMap<>();
        Map<Component, Set<LayoutParameter>> layoutParameterMap = new IdentityHashMap<>();
        for(LinearLayoutComponent llc: componentList) {
            minimumSizeMap.put(llc.component, llc.component.getMinimumSize());
            preferredSizeMap.put(llc.component, llc.component.getPreferredSize());
            layoutParameterMap.put(llc.component, llc.layoutParameters);
        }
        
        int availableMajorAxisSpace = getMajorAxis(layoutArea);
        int availableMinorAxisSpace = getMinorAxis(layoutArea);

        result.addAll(componentList.stream().map(llc -> new DefaultLaidOutComponent(llc.component, new TerminalSize(0, 0), new TerminalPosition(0, 0))).collect(Collectors.toList()));
        
        //Set minor axis - easy!
        for(DefaultLaidOutComponent lloc: result) {
            if(layoutParameterMap.get(lloc.component).contains(getMinorMaximizesParameter()) ||
                    layoutParameterMap.get(lloc.component).contains(getMinorGrowingParameter()) ||
                    (lloc.component instanceof Panel && maximisesOnMinorAxis((Panel)lloc.component))) {
                lloc.size = setMinorAxis(lloc.size, availableMinorAxisSpace);
            }
            else {
                int preferred = getMinorAxis(preferredSizeMap.get(lloc.component));
                lloc.size = setMinorAxis(lloc.size, preferred <= availableMinorAxisSpace ? preferred : availableMinorAxisSpace);
            }            
        }
        
        //Start dividing the major axis - hard!
        while(availableMajorAxisSpace > 0) {
            boolean changedSomething = false;
            for(DefaultLaidOutComponent lloc: result) {
                int preferred = getMajorAxis(preferredSizeMap.get(lloc.component));
                if(availableMajorAxisSpace > 0 && preferred > getMajorAxis(lloc.getSize())) {
                    availableMajorAxisSpace--;
                    lloc.size = setMajorAxis(lloc.getSize(), getMajorAxis(lloc.getSize()) + 1);
                    changedSomething = true;
                }
            }
            if(!changedSomething)
                break;
        }
        
        //Add padding, if any (this could cause availableMajorAxisSpace to go negative!! Beware!)
        availableMajorAxisSpace -= ((result.size() - 1) * padding);
        
        //Now try to accomodate the growing major axis components
        List<DefaultLaidOutComponent> growingComponents = new ArrayList<>();
        for(DefaultLaidOutComponent lloc: result) {
            if(layoutParameterMap.get(lloc.component).contains(getMajorMaximizesParameter()) ||
                    layoutParameterMap.get(lloc.component).contains(getMajorGrowingParameter())) {
                growingComponents.add(lloc);
            }
            
            if(lloc.component instanceof Panel && maximisesOnMajorAxis((Panel)lloc.component)) {
                growingComponents.add(lloc);
            }
        }
        
        while(availableMajorAxisSpace > 0 && !growingComponents.isEmpty()) {
            for(DefaultLaidOutComponent lloc: growingComponents) {
                if(availableMajorAxisSpace > 0) {
                    availableMajorAxisSpace--;
                    lloc.size = setMajorAxis(lloc.getSize(), getMajorAxis(lloc.getSize()) + 1);
                }
            }
        }
        
        //Finally, recalculate the topLeft position of each component
        int nextMajorPosition = 0;
        for(DefaultLaidOutComponent laidOutComponent: result) {
            laidOutComponent.topLeftPosition = setMajorAxis(laidOutComponent.topLeftPosition, nextMajorPosition);
            
            //Make sure not to add padding to the last component
            if(result.get(result.size() - 1) != laidOutComponent)
                nextMajorPosition += getMajorAxis(laidOutComponent.size) + padding;
            else
                nextMajorPosition += getMajorAxis(laidOutComponent.size);
        }        
        return result;
    }
    
    @Override
    public boolean maximisesHorisontally() {
        for(LinearLayoutComponent llc: componentList) {
            if(llc.layoutParameters.contains(MAXIMIZES_HORIZONTALLY))
                return true;
        }
        return false;
    }

    @Override
    public boolean maximisesVertically() {
        for(LinearLayoutComponent llc: componentList) {
            if(llc.layoutParameters.contains(MAXIMIZES_VERTICALLY))
                return true;
        }
        return false;
    }

    protected abstract boolean maximisesOnMajorAxis(Panel panel);
    
    protected abstract boolean maximisesOnMinorAxis(Panel panel);
    
    protected abstract TerminalSize setMajorAxis(TerminalSize terminalSize, int majorAxisValue);

    protected abstract TerminalSize setMinorAxis(TerminalSize terminalSize, int minorAxisValue);

    protected abstract TerminalPosition setMajorAxis(TerminalPosition terminalPosition, int majorAxisValue);

    protected abstract int getMajorAxis(TerminalSize terminalSize);

    protected abstract int getMinorAxis(TerminalSize terminalSize);
    
    protected abstract LayoutParameter getMajorMaximizesParameter();
    
    protected abstract LayoutParameter getMinorMaximizesParameter();
    
    protected abstract LayoutParameter getMajorGrowingParameter();
    
    protected abstract LayoutParameter getMinorGrowingParameter();

    protected List<Panel> getSubPanels() {
        return componentList.stream().filter(axisLayoutComponent -> axisLayoutComponent.component instanceof Panel).map(axisLayoutComponent -> (Panel) axisLayoutComponent.component).collect(Collectors.toList());
    }

    protected static class LinearLayoutComponent {
        public final Component component;
        public final Set<LayoutParameter> layoutParameters;

        public LinearLayoutComponent(Component component, Set<LayoutParameter> layoutParameters) {
            this.component = component;
            this.layoutParameters = layoutParameters;
        }
    }
}
