package org.hsl.compiler.debug;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.*;

/**
 * Represents an enumeration of console color and format codes.
 *
 * @author AdvancedAntiSkid
 */
@RequiredArgsConstructor
public enum Format {
    BLACK("Black", new Color(0, 0, 0), 30, 40),
    RED("Red", new Color(197, 15, 31), 31, 41),
    GREEN("Green", new Color(19, 161, 14), 32, 42),
    YELLOW("Yellow", new Color(193, 156, 0), 33, 43),
    BLUE("Blue", new Color(58, 150, 221), 34, 44),
    MAGENTA("Magenta", new Color(117, 23, 152), 35, 45),
    CYAN("Cyan", new Color(50, 150, 221), 36, 46),
    LIGHT_GRAY("Light Gray", new Color(204, 204, 204), 37, 47),
    DARK_GRAY("Dark Gray", new Color(118, 118, 118), 90, 100),
    LIGHT_RED("Light Red", new Color(231, 72, 75), 91, 101),
    LIGHT_GREEN("Light Green", new Color(19, 198, 13), 92, 102),
    LIGHT_YELLOW("Light Yellow", new Color(249, 241, 120), 93, 103),
    LIGHT_BLUE("Light Blue", new Color(59, 120, 255), 94, 104),
    LIGHT_MAGENTA("Light Magenta", new Color(180, 0, 158), 96, 106),
    LIGHT_CYAN("Light Cyan", new Color(97, 214, 214), 96, 106),
    WHITE("White", new Color(242, 242, 242), 97, 107),

    BOLD("Bold", null, 1, -1),
    UNDERLINE("Underline", null, 4, -1),
    NO_UNDERLINE("No Underline", null, 24, -1),
    NEGATIVE("Negative", null, 7, -1),
    POSITIVE("Positive", null, 27, -1),
    DEFAULT("Default", null, 0, -1);

    @Getter
    private final String name;

    @Getter
    private final Color color;

    private final int foreground;

    private final int background;

    public String getForeground() {
        return foreground(this);
    }

    public String getBackground() {
        return background(this);
    }

    @Override
    public String toString() {
        return getForeground();
    }

    public static String foreground(Format format) {
        return "\033[" + format.foreground + "m";
    }

    public static String background(Format format) {
        return "\033[" + format.background + "m";
    }

    public static String style(Format format) {
        return foreground(format);
    }
}
