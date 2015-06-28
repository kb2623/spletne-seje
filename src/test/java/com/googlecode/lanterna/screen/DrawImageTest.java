package com.googlecode.lanterna.screen;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.BasicTextImage;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.graphics.TextImage;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

/**
 * Test to try out drawImage in TextGraphics
 */
public class DrawImageTest {
    public static void main(String[] args) throws IOException {
        //Setup a standard Screen
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        Screen screen = new TerminalScreen(terminal);
        screen.startScreen();
        screen.setCursorPosition(null);

        //Create an 'image' that we fill with recognizable characters
        TextImage image = new BasicTextImage(5, 5);
        TextCharacter imageCharacter = new TextCharacter('X');
        TextGraphics textGraphics = image.newTextGraphics();
        textGraphics.drawRectangle(
                TerminalPosition.TOP_LEFT_CORNER,
                new TerminalSize(5, 5),
                imageCharacter.withBackgroundColor(TextColor.ANSI.RED));
        textGraphics.drawRectangle(
                TerminalPosition.OFFSET_1x1,
                new TerminalSize(3, 3),
                imageCharacter.withBackgroundColor(TextColor.ANSI.MAGENTA));
        textGraphics.setCharacter(2, 2,
                imageCharacter.withBackgroundColor(TextColor.ANSI.CYAN));

        TextGraphics screenGraphics = screen.newTextGraphics();
        screenGraphics.setBackgroundColor(TextColor.Indexed.fromRGB(50, 50, 50));
        screenGraphics.fill(' ');
        screenGraphics.drawImage(TerminalPosition.OFFSET_1x1, image);
        screenGraphics.drawImage(new TerminalPosition(8, 1), image, TerminalPosition.TOP_LEFT_CORNER, image.getSize().withRelativeColumns(-4));
        screenGraphics.drawImage(new TerminalPosition(10, 1), image, TerminalPosition.TOP_LEFT_CORNER, image.getSize().withRelativeColumns(-3));
        screenGraphics.drawImage(new TerminalPosition(13, 1), image, TerminalPosition.TOP_LEFT_CORNER, image.getSize().withRelativeColumns(-2));
        screenGraphics.drawImage(new TerminalPosition(17, 1), image, TerminalPosition.TOP_LEFT_CORNER, image.getSize().withRelativeColumns(-1));
        screenGraphics.drawImage(new TerminalPosition(22, 1), image);
        screenGraphics.drawImage(new TerminalPosition(28, 1), image, new TerminalPosition(1, 0), image.getSize());
        screenGraphics.drawImage(new TerminalPosition(33, 1), image, new TerminalPosition(2, 0), image.getSize());
        screenGraphics.drawImage(new TerminalPosition(37, 1), image, new TerminalPosition(3, 0), image.getSize());
        screenGraphics.drawImage(new TerminalPosition(40, 1), image, new TerminalPosition(4, 0), image.getSize());

        //Try to draw bigger than the image size, this should ignore the extra size
        screenGraphics.drawImage(new TerminalPosition(1, 7), image, TerminalPosition.TOP_LEFT_CORNER, image.getSize().withRelativeColumns(10));

        //0 size should draw nothing
        screenGraphics.drawImage(new TerminalPosition(8, 7), image, TerminalPosition.TOP_LEFT_CORNER, TerminalSize.ZERO);

        //Drawing with a negative source image offset will move the target position
        screenGraphics.drawImage(new TerminalPosition(8, 7), image, new TerminalPosition(-2, -2), image.getSize());

        screen.refresh();
        screen.readInput();
        screen.stopScreen();
    }
}
