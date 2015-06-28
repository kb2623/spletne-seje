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
package com.googlecode.lanterna.terminal.swing;

import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.TextCharacter;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.List;

/**
 * This class encapsulates the font information used by a SwingTerminal
 * @author martin
 */
public class SwingTerminalFontConfiguration {

    /**
     * Controls how the SGR bold will take effect when enabled on a character. Mainly this is controlling if the 
     * character should be rendered with a bold font or not. The reason for this is that some characters, notably the
     * lines and double-lines in defined in Symbol, usually doesn't look very good with bold font when you try to 
     * construct a GUI. 
     */
    public enum BoldMode {
        /**
         * All characters with SGR Bold enabled will be rendered using a bold font
         */
        EVERYTHING,
        /**
         * All characters with SGR Bold enabled, except for the characters defined as constants in Symbols class, will 
         * be rendered using a bold font
         */
        EVERYTHING_BUT_SYMBOLS,
        /**
         * Bold font will not be used for characters with SGR bold enabled
         */
        NOTHING,
        ;
    }

    private static final List<Font> DEFAULT_WINDOWS_FONTS = Collections.unmodifiableList(Arrays.asList(
            new Font("Courier New", Font.PLAIN, 14), //Monospaced can look pretty bad on Windows, so let's override it
            new Font("Monospaced", Font.PLAIN, 14)
    ));

    private static final List<Font> DEFAULT_LINUX_FONTS = Collections.unmodifiableList(Arrays.asList(
            new Font("Monospaced", Font.PLAIN, 14),
            new Font("Ubuntu Mono", Font.PLAIN, 14),
            new Font("FreeMono", Font.PLAIN, 14),
            new Font("DejaVu Sans Mono", Font.PLAIN, 14),
            new Font("Liberation Mono", Font.PLAIN, 14)
    ));

    private static final List<Font> DEFAULT_FONTS = Collections.unmodifiableList(Arrays.asList(
            new Font("Monospaced", Font.PLAIN, 14)
    ));

    private static Font[] selectDefaultFont() {
        String osName = System.getProperty("os.name", "").toLowerCase();
        if(osName.contains("win")) {
            return DEFAULT_WINDOWS_FONTS.toArray(new Font[DEFAULT_WINDOWS_FONTS.size()]);
        }
        else if(osName.contains("linux")) {
            return DEFAULT_LINUX_FONTS.toArray(new Font[DEFAULT_LINUX_FONTS.size()]);
        }
        else {
            return DEFAULT_FONTS.toArray(new Font[DEFAULT_FONTS.size()]);
        }
    }

    /**
     * This is the default font settings that will be used if you don't specify anything
     */
    public static final SwingTerminalFontConfiguration DEFAULT = newInstance(filterMonospaced(selectDefaultFont()));

    /**
     * Given an array of fonts, returns another array with only the ones that are monospaced. The fonts in the result
     * will have the same order as in which they came in. A font is considered monospaced if the width of 'i' and 'W' is
     * the same.
     * @param fonts Fonts to filter monospaced fonts from
     * @return Array with the fonts from the input parameter that were monospaced
     */
    public static Font[] filterMonospaced(Font... fonts) {
        List<Font> result = new ArrayList<>(fonts.length);
        for(Font font: fonts) {
            if (isFontMonospaced(font)) {
                result.add(font);
            }
        }
        return result.toArray(new Font[result.size()]);
    }

    /**
     * Creates a new font configuration from a list of fonts in order of priority. This works by having the terminal
     * attempt to draw each character with the fonts in the order they are specified in and stop once we find a font
     * that can actually draw the character. For ASCII characters, it's very likely that the first font will always be
     * used.
     * @param fontsInOrderOfPriority Fonts to use when drawing text, in order of priority
     * @return Font configuration built from the font list
     */
    @SuppressWarnings("WeakerAccess")
    public static SwingTerminalFontConfiguration newInstance(Font... fontsInOrderOfPriority) {
        return new SwingTerminalFontConfiguration(true, BoldMode.EVERYTHING_BUT_SYMBOLS, fontsInOrderOfPriority);
    }

    private final List<Font> fontPriority;
    private final int fontWidth;
    private final int fontHeight;
    private final boolean useAntiAliasing;
    private final BoldMode boldMode;

    @SuppressWarnings("WeakerAccess")
    protected SwingTerminalFontConfiguration(boolean useAntiAliasing, BoldMode boldMode, Font... fontsInOrderOfPriority) {
        if(fontsInOrderOfPriority == null || fontsInOrderOfPriority.length == 0) {
            throw new IllegalArgumentException("Must pass in a valid list of fonts to SwingTerminalFontConfiguration");
        }
        this.useAntiAliasing = useAntiAliasing;
        this.boldMode = boldMode;
        this.fontPriority = new ArrayList<>(Arrays.asList(fontsInOrderOfPriority));
        this.fontWidth = getFontWidth(fontPriority.get(0));
        this.fontHeight = getFontHeight(fontPriority.get(0));

        //Make sure all the fonts are monospace
        for(Font font: fontPriority) {
            if(!isFontMonospaced(font)) {
                throw new IllegalArgumentException("Font " + font + " isn't monospaced!");
            }
        }

        //Make sure all lower-priority fonts are less or equal in width and height, shrink if necessary
        for(int i = 1; i < fontPriority.size(); i++) {
            Font font = fontPriority.get(i);
            while(getFontWidth(font) > fontWidth || getFontHeight(font) > fontHeight) {
                float newSize = font.getSize2D() - 0.5f;
                if(newSize < 0.01) {
                    throw new IllegalStateException("Unable to shrink font " + (i+1) + " to fit the size of highest priority font " + fontPriority.get(0));
                }
                font = font.deriveFont(newSize);
                fontPriority.set(i, font);
            }
        }
    }

    Font getFontForCharacter(TextCharacter character) {
        Font normalFont = getFontForCharacter(character.getCharacter());
        if(boldMode == BoldMode.EVERYTHING || (boldMode == BoldMode.EVERYTHING_BUT_SYMBOLS && isNotASymbol(character.getCharacter()))) {
            if(character.isBold()) {
                normalFont = normalFont.deriveFont(Font.BOLD);
            }
        }
        return normalFont;
    }

    private Font getFontForCharacter(char c) {
        for(Font font: fontPriority) {
            if(font.canDisplay(c)) {
                return font;
            }
        }
        //No available font here, what to do...?
        return fontPriority.get(0);
    }

    int getFontWidth() {
        return fontWidth;
    }

    int getFontHeight() {
        return fontHeight;
    }

    private static boolean isFontMonospaced(Font font) {
        FontRenderContext frc = new FontRenderContext(
                null,
                RenderingHints.VALUE_TEXT_ANTIALIAS_OFF,
                RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT);
        Rectangle2D iBounds = font.getStringBounds("i", frc);
        Rectangle2D mBounds = font.getStringBounds("W", frc);
        return iBounds.getWidth() == mBounds.getWidth();
    }

    private int getFontWidth(Font font) {
        return (int)font.getStringBounds("W", getFontRenderContext()).getWidth();
    }

    private int getFontHeight(Font font) {
        return (int)font.getStringBounds("W", getFontRenderContext()).getHeight();
    }

    private FontRenderContext getFontRenderContext() {
        return new FontRenderContext(
                null,
                useAntiAliasing ?
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF,
                RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT);
    }

    
    private static final Set<Character> SYMBOLS_CACHE = new HashSet<>();
    static {
        for(Field field: Symbols.class.getFields()) {
            if(field.getType() == char.class &&
                    (field.getModifiers() & Modifier.FINAL) != 0 &&
                    (field.getModifiers() & Modifier.STATIC) != 0) {
                try {
                    SYMBOLS_CACHE.add(field.getChar(null));
                }
                catch(IllegalArgumentException | IllegalAccessException ignore) {
                    //Should never happen!
                }
            }
        }
    }
    
    private boolean isNotASymbol(char character) {
        return !SYMBOLS_CACHE.contains(character);
    }
}
