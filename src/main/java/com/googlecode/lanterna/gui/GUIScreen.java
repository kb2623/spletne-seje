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

import com.googlecode.lanterna.gui.listener.WindowAdapter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * This is the main class of the GUI system in Lanterna. To setup a GUI, you
 * instantiate this class and call the showWindow(...) method on window. 
 * Please notice that this class doesn't have any start or stop methods, this
 * must be managed by the underlying screen which is the backend for the GUI.
 * @author Martin
 */
@SuppressWarnings({"SameParameterValue", "WeakerAccess", "EmptyCatchBlock"})
@Deprecated
public class GUIScreen
{
    private final Screen screen;
    private final LinkedList<WindowPlacement> windowStack;
    private final boolean swallowExceptions;
    protected final Queue<Action> actionToRunInEventThread;    
    private GUIScreenBackgroundRenderer backgroundRenderer;
    private Theme guiTheme;
    private boolean needsRefresh;
    private Thread eventThread;

    public GUIScreen(Screen screen)
    {
        this(screen, "");
    }
    
    public GUIScreen(Screen screen, String title)
    {
        this(screen, new DefaultBackgroundRenderer(title));
    }
    
    public GUIScreen(Screen screen, GUIScreenBackgroundRenderer backgroundRenderer)
    {
        this(screen, backgroundRenderer, true);
    }
    
    public GUIScreen(Screen screen, GUIScreenBackgroundRenderer backgroundRenderer, boolean swallowExceptions)
    {
        if(backgroundRenderer == null)
            throw new IllegalArgumentException("backgroundRenderer cannot be null");
        
        this.backgroundRenderer = backgroundRenderer;
        this.screen = screen;
        this.guiTheme = Theme.getDefaultTheme();
        this.windowStack = new LinkedList<>();
        this.actionToRunInEventThread = new LinkedList<>();
        this.needsRefresh = false;
        this.eventThread = Thread.currentThread();  //We'll be expecting the thread who created us is the same as will be the event thread later
        this.swallowExceptions = swallowExceptions;
    }
    
    /**
     * @param title Title to be displayed in the top-left corner
     * @deprecated Use a GUI background renderer instead
     */
    @Deprecated
    public void setTitle(String title)
    {
        if(title == null)
            title = "";
        
        if(backgroundRenderer instanceof DefaultBackgroundRenderer) {
            ((DefaultBackgroundRenderer)backgroundRenderer).setTitle(title);
        }
    }

    /**
     * Sets a new Theme for the entire GUI
     */
    public void setTheme(Theme newTheme)
    {
        if(newTheme == null)
            return;
        
        this.guiTheme = newTheme;
        needsRefresh = true;
    }

    public void setBackgroundRenderer(GUIScreenBackgroundRenderer backgroundRenderer) {
        if(backgroundRenderer == null)
            throw new IllegalArgumentException("backgroundRenderer cannot be null");
        
        this.backgroundRenderer = backgroundRenderer;
        needsRefresh = true;
    }

    public GUIScreenBackgroundRenderer getBackgroundRenderer() {
        return backgroundRenderer;
    }

    /**
     * Gets the underlying screen, which can be used for starting, stopping, 
     * querying for size and much more
     * @return The Screen which is backing this GUI
     */
    public Screen getScreen() {
        return screen;
    }
    
    private synchronized void repaint() throws IOException
    {
        screen.doResizeIfNecessary();
        final TextGraphics textGraphics = new TextGraphicsImpl(new TerminalPosition(0, 0),
                screen.getTerminalSize(), screen, guiTheme);

        backgroundRenderer.drawBackground(textGraphics);
        
        int screenSizeColumns = screen.getTerminalSize().getColumns();
        int screenSizeRows = screen.getTerminalSize().getRows();

        //Go through the windows
        for(WindowPlacement windowPlacement: windowStack) {
            if(hasSoloWindowAbove(windowPlacement))
                continue;
            if(hasFullScreenWindowAbove(windowPlacement))
                continue;
            
            TerminalPosition topLeft = windowPlacement.getTopLeft();
            TerminalSize preferredSize;
            if(windowPlacement.getPositionPolicy() == Position.FULL_SCREEN)
                preferredSize = new TerminalSize(screenSizeColumns, screenSizeRows);
            else
                preferredSize = windowPlacement.getWindow().getPreferredSize();
            
            if(windowPlacement.positionPolicy == Position.CENTER) {
                if(windowPlacement.getWindow().maximisesHorisontally())
                    topLeft = topLeft.withColumn(2);
                else
                    topLeft = topLeft.withColumn((screenSizeColumns / 2) - (preferredSize.getColumns() / 2));

                if(windowPlacement.getWindow().maximisesVertically())
                    topLeft = topLeft.withRow(1);
                else
                    topLeft = topLeft.withRow((screenSizeRows / 2) - (preferredSize.getRows() / 2));
            }            
            int maxSizeWidth = screenSizeColumns - windowPlacement.getTopLeft().getColumn() - 1;
            int maxSizeHeight = screenSizeRows - windowPlacement.getTopLeft().getRow() - 1;

            if(preferredSize.getColumns() > maxSizeWidth || windowPlacement.getWindow().maximisesHorisontally())
                preferredSize = preferredSize.withColumns(maxSizeWidth);
            if(preferredSize.getRows() > maxSizeHeight || windowPlacement.getWindow().maximisesVertically())
                preferredSize = preferredSize.withRows(maxSizeHeight);
            
            if(windowPlacement.getPositionPolicy() == Position.FULL_SCREEN) {
                preferredSize = preferredSize.withColumns(preferredSize.getColumns() + 1);
                preferredSize = preferredSize.withRows(preferredSize.getRows() + 1);
            }
            
            if(topLeft.getColumn() < 0)
                topLeft = topLeft.withColumn(0);
            if(topLeft.getRow() < 0)
                topLeft = topLeft.withRow(0);

            TextGraphics subGraphics = textGraphics.subAreaGraphics(topLeft,
                    new TerminalSize(preferredSize.getColumns(), preferredSize.getRows()));

            if(windowPlacement.getWindow().isDrawShadow()) {
                //First draw the shadow
                textGraphics.applyTheme(guiTheme.getDefinition(Theme.Category.SHADOW));
                textGraphics.fillRectangle(' ', new TerminalPosition(topLeft.getColumn() + 2, topLeft.getRow() + 1),
                        new TerminalSize(subGraphics.getWidth(), subGraphics.getHeight()));
            }

            //Then draw the window
            windowPlacement.getWindow().repaint(subGraphics);
        }

        if(windowStack.size() > 0 && windowStack.getLast().getWindow().getWindowHotspotPosition() != null)
            screen.setCursorPosition(windowStack.getLast().getWindow().getWindowHotspotPosition());
        else
            screen.setCursorPosition(null);
        screen.refresh();
    }

    protected boolean update() throws IOException {
        if(needsRefresh || screen.doResizeIfNecessary() != null) {
            repaint();
            needsRefresh = false;
            return true;
        }
        return false;
    }

    protected Queue<Action> getActionsToRunInEventThreadQueue() {
        return actionToRunInEventThread;
    }

    /**
     * Signals the the entire screen needs to be re-drawn
     */
    public void invalidate()
    {
        needsRefresh = true;
    }

    protected void doEventLoop() throws IOException {
        int currentStackLength = windowStack.size();
        if(currentStackLength == 0)
            return;

        while(true) {
            if(currentStackLength > windowStack.size()) {
                //The window was removed from the stack ( = it was closed)
                break;
            }

            try {
                List<Action> actions = null;
                synchronized(actionToRunInEventThread) {
                    if(!actionToRunInEventThread.isEmpty()) {
                        actions = new ArrayList<>(actionToRunInEventThread);
                        actionToRunInEventThread.clear();
                    }
                }

                if(actions != null) {
                    for(Action nextAction: actions) {
                        try {
                            nextAction.doAction();
                        }
                        catch(Throwable e) {
                            e.printStackTrace();
                        }
                    }
                }

                //Make sure we have a component in focus if there is one available
                windowStack.getLast().window.checkFocus();

                boolean repainted = update();

                KeyStroke nextKey = screen.pollInput();
                if(nextKey != null) {
                    if(nextKey.getKeyType() == KeyType.EOF) {
                        break;
                    }
                    windowStack.getLast().window.onKeyPressed(nextKey);
                    invalidate();
                }
                else {
                    if(!repainted) {
                        try {
                            Thread.sleep(1);
                        } catch(InterruptedException e) {}
                    }
                }
            } catch(IOException | RuntimeException e) {
                if(swallowExceptions) {
                    e.printStackTrace();
                }
                else {
                    throw e;
                }
            }
        }
    }

    /**
     * Same as calling showWindow(window, Position.OVERLAPPING)
     * @param window Window to be shown
     * @throws java.io.IOException If there was an underlying I/O error
     */
    public void showWindow(Window window) throws IOException
    {
        showWindow(window, Position.OVERLAPPING);
    }

    /**
     * This method starts the GUI system with an initial window. The method
     * does not return until the window has been closed, so you need to provide
     * a mechanism for closing the window using the GUI.
     * 
     * If you call this method when already in GUI mode, it will create the new
     * window stacked on top of any previous window(s) and won't return until 
     * this new window has been closed.
     * @param window Window to display
     * @param position Where to position the new window
     * @throws java.io.IOException If there was an underlying I/O error
     */
    public void showWindow(Window window, Position position) throws IOException
    {
        if(window == null)
            return;
        if(position == null)
            position = Position.OVERLAPPING;

        int newWindowX = 2;
        int newWindowY = 1;

        if(position == Position.OVERLAPPING &&
                windowStack.size() > 0) {
            WindowPlacement lastWindow = windowStack.getLast();
            if(lastWindow.getPositionPolicy() != Position.CENTER) {
                newWindowX = lastWindow.getTopLeft().getColumn() + 2;
                newWindowY = lastWindow.getTopLeft().getRow() + 1;
            }
        }

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void onWindowInvalidated(Window window)
            {
                needsRefresh = true;
            }
        });
        windowStack.add(new WindowPlacement(window, position, new TerminalPosition(newWindowX, newWindowY)));
        window.setOwner(this);
        window.onVisible();
        needsRefresh = true;
        doEventLoop();
    }
    
    /**
     * @deprecated Call getActiveWindow().close() instead
     */
    @Deprecated
    public void closeWindow() {
        Window activeWindow = getActiveWindow();
        if(activeWindow != null) activeWindow.close();
    }

    /**
     * Used internally to close a window; API users should call Window.close() instead
     */
    protected void closeWindow(Window window)
    {
        if(windowStack.size() == 0)
            return;
        
        for(Iterator<WindowPlacement> iterator = windowStack.iterator(); iterator.hasNext(); ) {
            WindowPlacement placement = iterator.next();
            if(placement.window == window) {
                iterator.remove();
                placement.window.onClosed();
                return;
            }
        }
    }
    
    /**
     * Returns the top window in the window stack, the one which currently has user input focus.
     * @return The uppermost window in the window stack, or null if there are no windows
     */
    public Window getActiveWindow() {
        if(windowStack.isEmpty()) {
            return null;
        }
        else {
            return windowStack.getLast().getWindow();
        }
    }

    /**
     * Since Lanterna isn't thread safe, here's a way to run code on the same
     * thread as the GUI system is using. Pass an action in and it will be 
     * queued for execution.
     * @param codeToRun Code to be executed on the same thread as the GUI
     */
    public void runInEventThread(Action codeToRun)
    {
        synchronized(actionToRunInEventThread) {
            actionToRunInEventThread.add(codeToRun);
        }
    }

    /**
     * @return True if the current thread calling this method is the same thread
     * as the GUI system is using
     */
    public boolean isInEventThread()
    {
        return eventThread == Thread.currentThread();
    }

    /**
     * Where to position a window that is to be put on the screen
     */
    public enum Position
    {
        /**
         * Starting from the top left corner, created windows overlapping in
         * a down-right direction (similar to Microsoft Windows)
         */
        OVERLAPPING,
        /**
         * This window will be placed in the top-left corner, any windows 
         * created with overlapping after it will be positioned relative
         */
        NEW_CORNER_WINDOW,
        /**
         * At the center of the screen
         */
        CENTER,
        /**
         * The window will be maximized and take up the entire screen
         */
        FULL_SCREEN,
        ;
    }

    private boolean hasSoloWindowAbove(WindowPlacement windowPlacement)
    {
        int index = windowStack.indexOf(windowPlacement);
        for(int i = index + 1; i < windowStack.size(); i++) {
            if(windowStack.get(i).window.isSoloWindow())
                return true;
        }
        return false;
    }
    
    private boolean hasFullScreenWindowAbove(WindowPlacement windowPlacement) {
        int index = windowStack.indexOf(windowPlacement);
        for(int i = index + 1; i < windowStack.size(); i++) {
            if(windowStack.get(i).positionPolicy == Position.FULL_SCREEN)
                return true;
        }
        return false;
    }

    private class WindowPlacement
    {
        private Window window;
        private final Position positionPolicy;
        private TerminalPosition topLeft;

        public WindowPlacement(Window window, Position positionPolicy, TerminalPosition topLeft)
        {
            this.window = window;
            this.positionPolicy = positionPolicy;
            this.topLeft = topLeft;
        }

        public TerminalPosition getTopLeft()
        {
            if(positionPolicy == Position.FULL_SCREEN)
                return new TerminalPosition(0, 0);
            return topLeft;
        }

        @SuppressWarnings("unused")
		public void setTopLeft(TerminalPosition topLeft)
        {
            this.topLeft = topLeft;
        }

        public Window getWindow()
        {
            return window;
        }

        @SuppressWarnings("unused")
		public void setWindow(Window window)
        {
            this.window = window;
        }

        public Position getPositionPolicy()
        {
            return positionPolicy;
        }
    }
}
