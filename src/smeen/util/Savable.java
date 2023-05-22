package smeen.util;

import javafx.scene.image.*;
import smeen.component.code.CodeBlockInput;
import smeen.component.code.block.control.FalseCodeBlockInput;
import smeen.component.code.block.control.TrueCodeBlockInput;
import smeen.component.code.block.movement.GetXCodeBlockInput;
import smeen.component.code.block.movement.GetYCodeBlockInput;
import smeen.component.code.block.operator.inputBoolean.*;
import smeen.component.code.block.operator.inputDouble.*;
import smeen.component.code.block.operator.inputString.ContainCodeBlockInput;
import smeen.component.code.block.operator.inputString.JoinCodeBlockInput;
import smeen.component.code.block.operator.inputString.LetterOfCodeBlockInput;
import smeen.component.code.block.sensing.AnswerCodeBlockInput;
import smeen.component.code.block.sensing.KeyPressedCodeBlockInput;
import smeen.component.code.block.sensing.MouseDownCodeBlockInput;
import smeen.component.code.block.sensing.TouchingObjCodeBlockInput;
import smeen.component.code.block.variable.VariableCodeBlockInput;
import smeen.views.MainView;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a class that can be serialized to Map.
 */
public interface Savable {

    /**
     * Convert current data of this object to Map.
     * @return Map data representation of this object.
     */
    Map<String, Object> exportData();

    /**
     * Import the data that is a representation of this object into this object.
     * @param data the data to import.
     */
    void importData(Map<String, Object> data);

    /**
     * Export image data into data for saving.
     *
     * @param image the image to import.
     * @return
     * @see <a href="https://stackoverflow.com/questions/37008675/pure-javafx-convert-image-to-bytes-array-opposit-operation-whats-wrong">https://stackoverflow.com/questions/37008675/pure-javafx-convert-image-to-bytes-array-opposit-operation-whats-wrong</a>
     */
    static Map<String, Object> exportImageData(Image image) {
        PixelReader pr = image.getPixelReader();
        int[] buffer = new int[(int) image.getWidth() * (int) image.getHeight()];
        pr.getPixels(0, 0, (int) image.getWidth(), (int) image.getHeight(), PixelFormat.getIntArgbInstance(), buffer, 0, (int) image.getWidth());

        Map<String, Object> result = new HashMap<>();
        result.put("width", (int) image.getWidth());
        result.put("height", (int) image.getHeight());
        result.put("buffer", buffer);
        return result;
    }

    /**
     * Convert exported image data back to image instance.
     *
     * @param data
     * @return
     */
    static Image importImageData(Map<String, Object> data) {
        int width = (int) data.get("width");
        int height = (int) data.get("height");
        int[] buffer = (int[]) data.get("buffer");

        WritableImage image = new WritableImage(width, height);
        PixelWriter pixelWriter = image.getPixelWriter();
        pixelWriter.setPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), buffer, 0, width);
        return image;
    }

    /**
     * Convert exported code block input data back to code block input instance.
     *
     * @param data
     * @param main
     * @return
     */
    static CodeBlockInput<?> importCodeBlockInputData(Map<String, Object> data, MainView main) {
        switch ((String) data.get("type")) {
            case "AndCodeBlockInput":
                AndCodeBlockInput and = new AndCodeBlockInput(main);
                and.importData(data);
                return and;
            case "EqualCodeBlockInput":
                EqualCodeBlockInput equal = new EqualCodeBlockInput(main);
                equal.importData(data);
                return equal;
            case "GreaterCodeBlockInput":
                GreaterCodeBlockInput greater = new GreaterCodeBlockInput(main);
                greater.importData(data);
                return greater;
            case "LessCodeBlockInput":
                LessCodeBlockInput less = new LessCodeBlockInput(main);
                less.importData(data);
                return less;
            case "NotCodeBlockInput":
                NotCodeBlockInput not = new NotCodeBlockInput(main);
                not.importData(data);
                return not;
            case "OrCodeBlockInput":
                OrCodeBlockInput or = new OrCodeBlockInput(main);
                or.importData(data);
                return or;
            case "AddCodeBlockInput":
                AddCodeBlockInput add = new AddCodeBlockInput(main);
                add.importData(data);
                return add;
            case "DivideCodeBlockInput":
                DivideCodeBlockInput div = new DivideCodeBlockInput(main);
                div.importData(data);
                return div;
            case "FunctionOfCodeBlockInput":
                FunctionOfCodeBlockInput func = new FunctionOfCodeBlockInput(main);
                func.importData(data);
                return func;
            case "MinusCodeBlockInput":
                MinusCodeBlockInput minus = new MinusCodeBlockInput(main);
                minus.importData(data);
                return minus;
            case "ModCodeBlockInput":
                ModCodeBlockInput mod = new ModCodeBlockInput(main);
                mod.importData(data);
                return mod;
            case "MultiCodeBlockInput":
                MultiCodeBlockInput multi = new MultiCodeBlockInput(main);
                multi.importData(data);
                return multi;
            case "PowerCodeBlockInput":
                PowerCodeBlockInput pow = new PowerCodeBlockInput(main);
                pow.importData(data);
                return pow;
            case "RandomNumberCodeBlockInput":
                RandomNumberCodeBlockInput rand = new RandomNumberCodeBlockInput(main);
                rand.importData(data);
                return rand;
            case "RoundCodeBlockInput":
                RoundCodeBlockInput round = new RoundCodeBlockInput(main);
                round.importData(data);
                return round;
            case "ContainCodeBlockInput":
                ContainCodeBlockInput contain = new ContainCodeBlockInput(main);
                contain.importData(data);
                return contain;
            case "JoinCodeBlockInput":
                JoinCodeBlockInput join = new JoinCodeBlockInput(main);
                join.importData(data);
                return join;
            case "LetterOfCodeBlockInput":
                LetterOfCodeBlockInput letterOf = new LetterOfCodeBlockInput(main);
                letterOf.importData(data);
                return letterOf;
            case "AnswerCodeBlockInput":
                AnswerCodeBlockInput answer = new AnswerCodeBlockInput(main);
                answer.importData(data);
                return answer;
            case "KeyPressedCodeBlockInput":
                KeyPressedCodeBlockInput keyPressed = new KeyPressedCodeBlockInput(main);
                keyPressed.importData(data);
                return keyPressed;
            case "MouseDownCodeBlockInput":
                MouseDownCodeBlockInput mouseDown = new MouseDownCodeBlockInput(main);
                mouseDown.importData(data);
                return mouseDown;
            case "TouchingObjCodeBlockInput":
                TouchingObjCodeBlockInput touching = new TouchingObjCodeBlockInput(main);
                touching.importData(data);
                return touching;
            case "GetXCodeBlockInput":
                GetXCodeBlockInput getX = new GetXCodeBlockInput(main);
                getX.importData(data);
                return getX;
            case "GetYCodeBlockInput":
                GetYCodeBlockInput getY = new GetYCodeBlockInput(main);
                getY.importData(data);
                return getY;
            case "TrueCodeBlockInput":
                TrueCodeBlockInput trueCb = new TrueCodeBlockInput(main);
                trueCb.importData(data);
                return trueCb;
            case "FalseCodeBlockInput":
                FalseCodeBlockInput falseCb = new FalseCodeBlockInput(main);
                falseCb.importData(data);
                return falseCb;
            case "VariableCodeBlockInput":
                VariableCodeBlockInput<?> varCb = new VariableCodeBlockInput<>(main);
                varCb.importData(data);
                return varCb;
            default:
                throw new IllegalStateException("Unknown CodeBlockInput type: " + data.get("type"));
        }
    }
}
