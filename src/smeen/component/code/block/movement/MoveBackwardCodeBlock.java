package smeen.component.code.block.movement;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import smeen.component.SpriteObject;
import smeen.component.code.CodeBlock;
import smeen.component.code.CodeBlockInputSlot;
import smeen.global.Fonts;
import smeen.global.SmeenConstants.Type;
import smeen.logic.Executable;
import smeen.logic.SmeenContext;
import smeen.views.MainView;

public class MoveBackwardCodeBlock extends CodeBlock {
	private CodeBlockInputSlot<Double> amount;
    public MoveBackwardCodeBlock(MainView main) {
        super(main);
        Label first = new Label("เคลื่อนที่ไปข้างหลัง");
        first.setTextFill(Color.WHITE);
        first.setFont(Fonts.SMALL_REGULAR_FONT);

        amount = new CodeBlockInputSlot<>(main, Type.Double);

        Label second = new Label("ก้าว");
        second.setTextFill(Color.WHITE);
        second.setFont(Fonts.SMALL_REGULAR_FONT);

        getContent().setBackground(Background.fill(Color.DODGERBLUE));

        getContent().getChildren().addAll(first, amount, second);
    }

    @Override
    public CodeBlock copy() {
        MoveBackwardCodeBlock copy = new MoveBackwardCodeBlock(getMain());
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

        double angle = Math.toRadians(sprite.getImageView().getRotate());
        double amt = amount.getValue(context);
        double dx = -amt * Math.cos(angle);
        double dy = -amt * Math.sin(angle);
        Platform.runLater(() -> sprite.relocate(sprite.getLayoutX() + dx, sprite.getLayoutY() + dy));
        return new Executable.Result(false, false);
    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "MoveBackwardCodeBlock");
        result.put("amount", amount.exportData());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        amount.importData((Map<String, Object>) data.get("amount"));
    }
}
