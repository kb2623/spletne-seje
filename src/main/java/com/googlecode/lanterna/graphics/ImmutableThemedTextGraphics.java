package com.googlecode.lanterna.graphics;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.screen.TabBehaviour;

import java.util.EnumSet;

/**
 * Implementation of ThemedTextGraphics that wraps a TextGraphics that all calls are delegated to, except for the
 * method from ThemedTextGraphics which are handled. The theme is set at construction time, but you can create a clone
 * of this object with a different theme.
 * @author Martin
 */
public class ImmutableThemedTextGraphics implements ThemedTextGraphics {
    private final TextGraphics backend;
    private final Theme theme;

    public ImmutableThemedTextGraphics(TextGraphics backend, Theme theme) {
        this.backend = backend;
        this.theme = theme;
    }

    public ImmutableThemedTextGraphics withTheme(Theme theme) {
        return new ImmutableThemedTextGraphics(backend, theme);
    }

    public TextGraphics getUnderlyingTextGraphics() {
        return backend;
    }

    public Theme getTheme() {
        return theme;
    }

    @Override
    public ThemeDefinition getThemeDefinition(Class<?> clazz) {
        return theme.getDefinition(clazz);
    }

    @Override
    public ImmutableThemedTextGraphics applyThemeStyle(ThemeStyle themeStyle) {
        setForegroundColor(themeStyle.getForeground());
        setBackgroundColor(themeStyle.getBackground());
        setModifiers(themeStyle.getSGRs());
        return this;
    }

    @Override
    public TerminalSize getSize() {
        return backend.getSize();
    }

    @Override
    public ImmutableThemedTextGraphics newTextGraphics(TerminalPosition topLeftCorner, TerminalSize size) throws IllegalArgumentException {
        return new ImmutableThemedTextGraphics(backend.newTextGraphics(topLeftCorner, size), theme);
    }

    @Override
    public TextColor getBackgroundColor() {
        return backend.getBackgroundColor();
    }

    @Override
    public ImmutableThemedTextGraphics setBackgroundColor(TextColor backgroundColor) {
        backend.setBackgroundColor(backgroundColor);
        return this;
    }

    @Override
    public TextColor getForegroundColor() {
        return backend.getForegroundColor();
    }

    @Override
    public ImmutableThemedTextGraphics setForegroundColor(TextColor foregroundColor) {
        backend.setForegroundColor(foregroundColor);
        return this;
    }

    @Override
    public ImmutableThemedTextGraphics enableModifiers(SGR... modifiers) {
        backend.enableModifiers(modifiers);
        return this;
    }

    @Override
    public ImmutableThemedTextGraphics disableModifiers(SGR... modifiers) {
        backend.disableModifiers(modifiers);
        return this;
    }

    @Override
    public ImmutableThemedTextGraphics setModifiers(EnumSet<SGR> modifiers) {
        backend.setModifiers(modifiers);
        return this;
    }

    @Override
    public ImmutableThemedTextGraphics clearModifiers() {
        backend.clearModifiers();
        return this;
    }

    @Override
    public EnumSet<SGR> getActiveModifiers() {
        return backend.getActiveModifiers();
    }

    @Override
    public TabBehaviour getTabBehaviour() {
        return backend.getTabBehaviour();
    }

    @Override
    public ImmutableThemedTextGraphics setTabBehaviour(TabBehaviour tabBehaviour) {
        backend.setTabBehaviour(tabBehaviour);
        return this;
    }

    @Override
    public ImmutableThemedTextGraphics fill(char c) {
        backend.fill(c);
        return this;
    }

    @Override
    public TextGraphics fillRectangle(TerminalPosition topLeft, TerminalSize size, char character) {
        backend.fillRectangle(topLeft, size, character);
        return this;
    }

    @Override
    public TextGraphics fillRectangle(TerminalPosition topLeft, TerminalSize size, TextCharacter character) {
        backend.fillRectangle(topLeft, size, character);
        return this;
    }

    @Override
    public TextGraphics drawRectangle(TerminalPosition topLeft, TerminalSize size, char character) {
        backend.drawRectangle(topLeft, size, character);
        return this;
    }

    @Override
    public TextGraphics drawRectangle(TerminalPosition topLeft, TerminalSize size, TextCharacter character) {
        backend.drawRectangle(topLeft, size, character);
        return this;
    }

    @Override
    public TextGraphics fillTriangle(TerminalPosition p1, TerminalPosition p2, TerminalPosition p3, char character) {
        backend.fillTriangle(p1, p2, p3, character);
        return this;
    }

    @Override
    public TextGraphics fillTriangle(TerminalPosition p1, TerminalPosition p2, TerminalPosition p3, TextCharacter character) {
        backend.fillTriangle(p1, p2, p3, character);
        return this;
    }

    @Override
    public TextGraphics drawTriangle(TerminalPosition p1, TerminalPosition p2, TerminalPosition p3, char character) {
        backend.drawTriangle(p1, p2, p3, character);
        return this;
    }

    @Override
    public TextGraphics drawTriangle(TerminalPosition p1, TerminalPosition p2, TerminalPosition p3, TextCharacter character) {
        backend.drawTriangle(p1, p2, p3, character);
        return this;
    }

    @Override
    public TextGraphics drawLine(TerminalPosition fromPoint, TerminalPosition toPoint, char character) {
        backend.drawLine(fromPoint, toPoint, character);
        return this;
    }

    @Override
    public TextGraphics drawLine(TerminalPosition fromPoint, TerminalPosition toPoint, TextCharacter character) {
        backend.drawLine(fromPoint, toPoint, character);
        return this;
    }

    @Override
    public TextGraphics drawLine(int fromX, int fromY, int toX, int toY, char character) {
        backend.drawLine(fromX, fromY, toX, toY, character);
        return this;
    }

    @Override
    public TextGraphics drawLine(int fromX, int fromY, int toX, int toY, TextCharacter character) {
        backend.drawLine(fromX, fromY, toX, toY, character);
        return this;
    }

    @Override
    public TextGraphics drawImage(TerminalPosition topLeft, TextImage image) {
        backend.drawImage(topLeft, image);
        return this;
    }

    @Override
    public TextGraphics drawImage(TerminalPosition topLeft, TextImage image, TerminalPosition sourceImageTopLeft, TerminalSize sourceImageSize) {
        backend.drawImage(topLeft, image, sourceImageTopLeft, sourceImageSize);
        return this;
    }

    @Override
    public TextGraphics setCharacter(TerminalPosition position, char character) {
        backend.setCharacter(position, character);
        return this;
    }

    @Override
    public TextGraphics setCharacter(TerminalPosition position, TextCharacter character) {
        backend.setCharacter(position, character);
        return this;
    }

    @Override
    public TextGraphics setCharacter(int column, int row, char character) {
        backend.setCharacter(column, row, character);
        return this;
    }

    @Override
    public TextGraphics setCharacter(int column, int row, TextCharacter character) {
        backend.setCharacter(column, row, character);
        return this;
    }

    @Override
    public ImmutableThemedTextGraphics putString(int column, int row, String string) {
        backend.putString(column, row, string);
        return this;
    }

    @Override
    public ImmutableThemedTextGraphics putString(TerminalPosition position, String string) {
        backend.putString(position, string);
        return this;
    }

    @Override
    public ImmutableThemedTextGraphics putString(int column, int row, String string, SGR extraModifier, SGR... optionalExtraModifiers) {
        backend.putString(column, row, string, extraModifier, optionalExtraModifiers);
        return this;
    }

    @Override
    public ImmutableThemedTextGraphics putString(TerminalPosition position, String string, SGR extraModifier, SGR... optionalExtraModifiers) {
        backend.putString(position, string, extraModifier, optionalExtraModifiers);
        return this;
    }
}
