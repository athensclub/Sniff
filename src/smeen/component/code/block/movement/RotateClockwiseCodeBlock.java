package smeen.component.code.block.movement;

import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
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

public class RotateClockwiseCodeBlock extends CodeBlock {

    private CodeBlockInputSlot<Double> amount;
    public RotateClockwiseCodeBlock(MainView main){
        super(main);
        Label first = new Label("หมุนตามเข็ม");
        first.setTextFill(Color.WHITE);
        first.setFont(Fonts.SMALL_REGULAR_FONT);

        amount = new CodeBlockInputSlot<>(main, Type.Double);

        Label second = new Label("องศา");
        second.setTextFill(Color.WHITE);
        second.setFont(Fonts.SMALL_REGULAR_FONT);

        getContent().setBackground(Background.fill(Color.DODGERBLUE));

        getContent().getChildren().addAll(first, amount, second);
    }

    @Override
    public CodeBlock copy() {
    	RotateClockwiseCodeBlock copy = new RotateClockwiseCodeBlock(getMain());
        copy.amount.getTextField().setText(amount.getTextField().getText());
        return copy;
    }

    @Override
    public Executable.Result execute(SmeenContext context) {
        if(getCodeArea() == null)
            return new Executable.Result(false, false);

        SpriteObject sprite = getCodeArea().getSprite();
        if (sprite == null || Objects.equals(getMain().draggingProperty().get(),sprite))
            return new Executable.Result(false, false); // no op on movement code that is not in sprite or the sprite is being dragged.


        ImageView iv = sprite.getImageView();
        Platform.runLater(() -> iv.setRotate(iv.getRotate() + amount.getValue(context)));
        return new Executable.Result(false, false);
    }
    
    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "RotateClockwiseCodeBlock");
        result.put("amount", amount.exportData());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        amount.importData((Map<String, Object>) data.get("amount"));
    }
}