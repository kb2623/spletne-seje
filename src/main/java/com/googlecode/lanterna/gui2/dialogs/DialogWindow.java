package com.googlecode.lanterna.gui2.dialogs;

import com.googlecode.lanterna.gui2.BasicWindow;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class DialogWindow extends BasicWindow {

    private static final Set<Hint> GLOBAL_DiALOG_HINTS =
            Collections.unmodifiableSet(new HashSet<>(Arrays.asList(Hint.MODAL)));

    protected DialogWindow(String title) {
        super(title);
    }

    @Override
    public Set<Hint> getHints() {
        return GLOBAL_DiALOG_HINTS;
    }
}
