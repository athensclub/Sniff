package smeen.global;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * A global class for handling font.
 */
public final class Fonts {

    /**
     * The thai font family name.
     */
    public static final String THAI_FONT_FAMILY;

    /**
     * A thai font with standard size and weight.
     */
    public static final Font BASIC_REGULAR_FONT;

    /**
     * A thai font with standard size and bold weight.
     */
    public static final Font BASIC_BOLD_FONT;

    /**
     * A thai font with small size and standard weight.
     */
    public static final Font SMALL_REGULAR_FONT;

    /**
     * A thai font with extra small size and standard weight.
     */
    public static final Font EXTRA_SMALL_REGULAR_FONT;

    static{
        BASIC_BOLD_FONT = Font.loadFont(Fonts.class.getResourceAsStream("/fonts/IBMPlexSansThai-Bold.ttf"), 24);
        BASIC_REGULAR_FONT = Font.loadFont(Fonts.class.getResourceAsStream("/fonts/IBMPlexSansThai-Regular.ttf"), 24);
        SMALL_REGULAR_FONT  = Font.loadFont(Fonts.class.getResourceAsStream("/fonts/IBMPlexSansThai-Bold.ttf"), 14);
        EXTRA_SMALL_REGULAR_FONT = Font.loadFont(Fonts.class.getResourceAsStream("/fonts/IBMPlexSansThai-Bold.ttf"), 10);
        THAI_FONT_FAMILY = BASIC_BOLD_FONT.getFamily();
    }

}
