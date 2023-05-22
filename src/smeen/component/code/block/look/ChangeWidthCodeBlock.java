package smeen.component.code.block.look;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

public class ChangeWidthCodeBlock extends CodeBlock {

    private CodeBlockInputSlot<Double> amount;

    public ChangeWidthCodeBlock(MainView main) {
        super(main);
        Label first = new Label("เปลี่ยนความยาวเป็น");
        first.setTextFill(Color.WHITE);
        first.setFont(Fonts.SMALL_REGULAR_FONT);

        amount = new CodeBlockInputSlot<>(main, Type.Double);

        getContent().setBackground(Background.fill(Color.MEDIUMORCHID));

        getContent().getChildren().addAll(first, amount);
    }

    @Override
    public CodeBlock copy() {
        ChangeWidthCodeBlock copy = new ChangeWidthCodeBlock(getMain());
        copy.amount.getTextField().setText(amount.getTextField().getText());
        return copy;
    }

    @Override
    public Result execute(SmeenContext context) {
        if (getCodeArea() == null)
            return new Executable.Result(false, false);

        SpriteObject sprite = getCodeArea().getSprite();
        if (sprite == null || Objects.equals(getMain().draggingProperty().get(), sprite))
            return new Executable.Result(false, false); // no op on movement code that is not in sprite or the sprite is being dragged.

        double amt = amount.getValue(context);
        Platform.runLater(() -> sprite.getImageView().setFitWidth(amt));
        return new Result(false, false);
    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "ChangeWidthCodeBlock");
        result.put("amount", amount.exportData());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        amount.importData((Map<String, Object>) data.get("amount"));
    }

}
