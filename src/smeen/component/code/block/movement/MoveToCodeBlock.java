package smeen.component.code.block.movement;

import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.util.converter.IntegerStringConverter;
import smeen.component.SpriteObject;
import smeen.component.code.CodeBlock;
import smeen.component.code.CodeBlockInputSlot;
import smeen.global.Fonts;
import smeen.global.SmeenConstants.Type;
import smeen.logic.Executable;
import smeen.logic.SmeenContext;
import smeen.views.MainView;

import java.util.Objects;

public class MoveToCodeBlock extends CodeBlock {

    private CodeBlockInputSlot<Double> amountX, amountY;

    public MoveToCodeBlock(MainView main) {
        super(main);
        Label first = new Label("เคลื่อนที่ไปที่ x:");
        first.setTextFill(Color.WHITE);
        first.setFont(Fonts.SMALL_REGULAR_FONT);

        amountX = new CodeBlockInputSlot<>(main, Type.Double);

        Label second = new Label("y:");
        second.setTextFill(Color.WHITE);
        second.setFont(Fonts.SMALL_REGULAR_FONT);

        amountY = new CodeBlockInputSlot<>(main, Type.Double);

        getContent().setBackground(Background.fill(Color.DODGERBLUE));

        getContent().getChildren().addAll(first, amountX, second, amountY);
    }

    @Override
    public CodeBlock copy() {
        MoveToCodeBlock copy = new MoveToCodeBlock(getMain());
        copy.amountX.getTextField().setText(amountX.getTextField().getText());
        copy.amountY.getTextField().setText(amountY.getTextField().getText());
        return copy;
    }

    @Override
    public Executable.Result execute(SmeenContext context) {
        if(getCodeArea() == null)
            return new Executable.Result(false, false);

        SpriteObject sprite = getCodeArea().getSprite();
        if (sprite == null || Objects.equals(getMain().draggingProperty().get(),sprite))
            return new Executable.Result(false, false); // no op on movement code that is not in sprite or the sprite is being dragged.

        Platform.runLater(() -> sprite.relocate(amountX.getValue(context), amountY.getValue(context)));
        return new Executable.Result(false, false);
    }
    
    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "MoveToCodeBlock");
        result.put("amountX", amountX.exportData());
        result.put("amountY", amountY.exportData());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        amountX.importData((Map<String, Object>) data.get("amountX"));
        amountY.importData((Map<String, Object>) data.get("amountY"));
    }
}