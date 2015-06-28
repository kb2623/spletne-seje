package com.googlecode.lanterna.gui2;

public class SameTextGUIThread extends AbstractTextGUIThread {

    private final Thread guiThread;

    public SameTextGUIThread(TextGUI textGUI) {
        super(textGUI);
        guiThread = Thread.currentThread();
    }

    @Override
    public Thread getThread() {
        return guiThread;
    }

    @Override
    public void invokeAndWait(Runnable runnable) throws IllegalStateException, InterruptedException {
        if(guiThread == null || guiThread == Thread.currentThread()) {
            runnable.run();
        }
        super.invokeAndWait(runnable);
    }

    public static class Factory implements TextGUIThreadFactory {
        @Override
        public TextGUIThread createTextGUIThread(TextGUI textGUI) {
            return new SameTextGUIThread(textGUI);
        }
    }
}
