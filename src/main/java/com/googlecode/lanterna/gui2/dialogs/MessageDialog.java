package com.googlecode.lanterna.gui2.dialogs;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;

public class MessageDialog extends DialogWindow {

    private MessageDialogButton result;

    MessageDialog(
            String title,
            String text,
            MessageDialogButton... buttons) {

        super(title);
        this.result = null;
        if(buttons == null || buttons.length == 0) {
            buttons = new MessageDialogButton[] { MessageDialogButton.OK };
        }

        Panel buttonPanel = new Panel();
        buttonPanel.setLayoutManager(new GridLayout(buttons.length).setHorizontalSpacing(1));
        for(final MessageDialogButton button: buttons) {
            buttonPanel.addComponent(new Button(button.toString(), () -> {
                result = button;
                close();
            }));
        }

        Panel mainPanel = new Panel();
        mainPanel.setLayoutManager(
                new GridLayout(1)
                        .setLeftMarginSize(1)
                        .setRightMarginSize(1));
        mainPanel.addComponent(new Label(text));
        mainPanel.addComponent(new EmptySpace(TerminalSize.ONE));
        buttonPanel.setLayoutData(
                GridLayout.createLayoutData(
                        GridLayout.Alignment.END,
                        GridLayout.Alignment.CENTER,
                        false,
                        false))
                .addTo(mainPanel);
        setComponent(mainPanel);
    }

    public MessageDialogButton showDialog(WindowBasedTextGUI textGUI) {
        result = null;
        textGUI.addWindow(this);

        //Wait for the window to close, in case the window manager doesn't honor the MODAL hint
        waitUntilClosed();

        return result;
    }

    public static MessageDialogButton showMessageDialog(
            WindowBasedTextGUI textGUI,
            String title,
            String text,
            MessageDialogButton... buttons) {
        MessageDialogBuilder builder = new MessageDialogBuilder()
                .setTitle(title)
                .setText(text);
        for(MessageDialogButton button: buttons) {
            builder.addButton(button);
        }
        return builder.build().showDialog(textGUI);
    }
}
