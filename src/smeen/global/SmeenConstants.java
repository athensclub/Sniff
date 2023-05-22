package smeen.global;

import java.lang.reflect.Array;
import java.security.Key;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.function.Function;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public class SmeenConstants {

    /**
     * The distance (in pixels) that two CodeBlockList can have at most to be able to snap.
     */
    public static final double SNAP_THRESHOLD = 15;

    /**
     * The width of the stage.
     */
    public static final double STAGE_WIDTH = 440;

    /**
     * The height of the stage.
     */
    public static final double STAGE_HEIGHT = 280;

    /**
     * The default border with light gray color.
     */
    public static final Border DEFAULT_BORDER = new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, new CornerRadii(8), BorderWidths.DEFAULT));

    /**
     * The blue border representing a selected object.
     */
    public static final Border HIGHLIGHTED_BORDER = new Border(new BorderStroke(Color.DODGERBLUE, BorderStrokeStyle.SOLID, new CornerRadii(8), BorderWidths.DEFAULT));

    /**
     * A border with large width to cover a code block list.
     */
    public static final Border INNER_CODE_BLOCK_LIST_ORANGE_BORDER = new Border(new BorderStroke(Color.ORANGE, Color.ORANGE, Color.ORANGE, Color.ORANGE, BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 24, 24), Insets.EMPTY));

    /**
     * The minimum width/height (in pixels) that code area will extends from the furthest
     * child in each direction to create empty space.
     */
    public static final double CODE_AREA_EXTRA_SPACE = 100;

    /**
     * The delay (in milliseconds) between code block executions.
     */
    public static final long EXECUTION_DELAY = 15;

    /**
     * The thread pool to create threads to execute code blocks.
     */
    public static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();

    /**
     * Object used to lock (using synchronized) sprite asking so that only 1 sprite can be asking
     * at any time.
     */
    public static final Object ANSWER_LOCK = new Object();

    /**
     * The default background image for the stage.
     */
    public static final Image DEFAULT_BACKGROUND;

    /**
     * The first costume image for the default sprite.
     */
    public static final Image DOG_SPRITE_1;

    /**
     * The second costume image for the default sprite.
     */
    public static final Image DOG_SPRITE_2;

    static {
        WritableImage bg = new WritableImage((int) STAGE_WIDTH, (int) STAGE_HEIGHT);
        PixelWriter pw = bg.getPixelWriter();
        for (int i = 0; i < bg.getWidth(); i++)
            for (int j = 0; j < bg.getHeight(); j++)
                pw.setColor(i, j, Color.WHITE);
        DEFAULT_BACKGROUND = bg;

        DOG_SPRITE_1 = new Image(SmeenConstants.class.getResourceAsStream("/picture/dog-move1.png"));
        DOG_SPRITE_2 = new Image(SmeenConstants.class.getResourceAsStream("/picture/dog-move2.png"));
    }


    // Enum for Input type

    /**
     * Represent a type of code block input.
     */
    public enum Type {
        /**
         * Represent a String type.
         */
        String,
        /**
         * Represent a double type.
         */
        Double,
        /**
         * Represent a boolean type.
         */
        Boolean,
    }

    /**
     * Represent a key type to check for key pressed blocks.
     */
    public enum KeyPress {
        ANY(code -> true), SPACEBAR(KeyCode.SPACE), ENTER(KeyCode.ENTER), CTRL(KeyCode.CONTROL), SHIFT(KeyCode.SHIFT), KEY_UP(code -> code.equals(KeyCode.UP) || code.equals(KeyCode.KP_UP)), KEY_DOWN(code -> code.equals(KeyCode.DOWN) || code.equals(KeyCode.KP_DOWN)), KEY_LEFT(code -> code.equals(KeyCode.LEFT) || code.equals(KeyCode.KP_LEFT)), KEY_RIGHT(code -> code.equals(KeyCode.RIGHT) || code.equals(KeyCode.KP_RIGHT)),
        KEY_A(KeyCode.A), KEY_B(KeyCode.B), KEY_C(KeyCode.C), KEY_D(KeyCode.D), KEY_E(KeyCode.E), KEY_F(KeyCode.F), KEY_G(KeyCode.G), KEY_H(KeyCode.H), KEY_I(KeyCode.I), KEY_J(KeyCode.J), KEY_K(KeyCode.K), KEY_L(KeyCode.L), KEY_M(KeyCode.M), KEY_N(KeyCode.N), KEY_O(KeyCode.O), KEY_P(KeyCode.P), KEY_Q(KeyCode.Q), KEY_R(KeyCode.R), KEY_S(KeyCode.S), KEY_T(KeyCode.T), KEY_U(KeyCode.U), KEY_V(KeyCode.V), KEY_W(KeyCode.W), KEY_X(KeyCode.X), KEY_Y(KeyCode.Y), KEY_Z(KeyCode.Z),
        KEY_0(code -> code.equals(KeyCode.DIGIT0) || code.equals(KeyCode.NUMPAD0)), KEY_1(code -> code.equals(KeyCode.DIGIT1) || code.equals(KeyCode.NUMPAD1)), KEY_2(code -> code.equals(KeyCode.DIGIT2) || code.equals(KeyCode.NUMPAD2)), KEY_3(code -> code.equals(KeyCode.DIGIT3) || code.equals(KeyCode.NUMPAD3)), KEY_4(code -> code.equals(KeyCode.DIGIT4) || code.equals(KeyCode.NUMPAD4)), KEY_5(code -> code.equals(KeyCode.DIGIT5) || code.equals(KeyCode.NUMPAD5)), KEY_6(code -> code.equals(KeyCode.DIGIT6) || code.equals(KeyCode.NUMPAD6)), KEY_7(code -> code.equals(KeyCode.DIGIT7) || code.equals(KeyCode.NUMPAD7)), KEY_8(code -> code.equals(KeyCode.DIGIT8) || code.equals(KeyCode.NUMPAD8)), KEY_9(code -> code.equals(KeyCode.DIGIT9) || code.equals(KeyCode.NUMPAD9));

        private Function<KeyCode, Boolean> checker;

        /**
         * Check whether the given javafx KeyCode will trigger this KeyPress event.
         *
         * @param code the javafx KeyCode
         * @return whether the given javafx KeyCode will trigger this KeyPress event.
         */
        public boolean checkKeyCode(KeyCode code) {
            return checker.apply(code);
        }

        /**
         * Constructor for the KeyPressEvent. This will check for key code equality.
         * @param code
         */
        KeyPress(KeyCode code) {
            this.checker = c -> c.equals(code);
        }

        /**
         * Constructor for the KeyPressEvent.
         * @param checker function that take in key code and check if that keycode being pressed mean this key code is pressed.
         */
        KeyPress(Function<KeyCode, Boolean> checker) {
            this.checker = checker;
        }
    }

    /**
     * Represent math functions to be used in math function block.
     */
    public enum MathFunction {

        /**
         * Floor function.
         */
        floor(num -> Math.floor(num), "Floor"),

        /**
         * Ceiling function.
         */
        ceiling(num -> Math.ceil(num), "Ceiling"),

        /**
         * Sine function.
         */
        sin(num -> Math.sin(num)),

        /**
         * Cosine function.
         */
        cos(num -> Math.cos(num)),

        /**
         * Tangent function.
         */
        tan(num -> Math.tan(num)),

        /**
         * Inverse sine function.
         */
        arcsin(num -> Math.asin(num)),

        /**
         * Inverse cosine function.
         * */
        arccos(num -> Math.acos(num)),

        /**
         * Inverse tangent function.
         */
        arctan(num -> Math.atan(num)),

        /**
         * Absolute function.
         */
        abs(num -> Math.abs(num)),

        /**
         * Square root function.
         */
        sqrt(num -> Math.sqrt(num)),

        /**
         * Natural logarithm function.
         */
        ln(num -> Math.log(num)),

        /**
         * Logarithm (base 10) function.
         */
        log(num -> Math.log10(num)),

        /**
         * Exponential function.
         */
        ePow(num -> Math.pow(Math.E, num), "e^"),;

        private Function<Double, Double> func;
        private String name;

        /**
         * The constructor of MathFunction.
         * @param func the function that perform calculation.
         */
        MathFunction(Function<Double, Double> func) {
            this.func = func;
            this.name = this.toString();
        }

        /**
         * The constructor of MathFunction.
         * @param func
         * @param name
         */
        MathFunction(Function<Double, Double> func, String name) {
            this.func = func;
            this.name = name;
        }

        /**
         * Perform calculation on the given input.
         * @param num
         * @return
         */
        public double calculation(Double num) {
            return func.apply(num);
        }

        /**
         *
         * @return the name of this function.
         */
        public String getName() {
            return name;
        }
    }

}
