package smeen.component.code.block.sensing;

import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.converter.IntegerStringConverter;
import smeen.component.SpriteObject;
import smeen.component.code.CodeBlock;
import smeen.component.code.CodeBlockInput;
import smeen.component.code.CodeBlockInputSlot;
import smeen.global.Fonts;
import smeen.global.SmeenConstants;
import smeen.global.SmeenConstants.Type;
import smeen.logic.SmeenContext;
import smeen.views.MainView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TouchingObjCodeBlockInput extends CodeBlockInput<Boolean> {

    private CodeBlockInputSlot<Double> posSprite;

    public TouchingObjCodeBlockInput(MainView main) {
        super(main);
        Label first = new Label("แตะตัวละครที่");
        first.setTextFill(Color.WHITE);
        first.setFont(Fonts.SMALL_REGULAR_FONT);

        posSprite = new CodeBlockInputSlot<>(main, Type.Double);

        Label second = new Label("?");
        second.setTextFill(Color.WHITE);
        second.setFont(Fonts.SMALL_REGULAR_FONT);

        getContent().setBackground(new Background(new BackgroundFill(Color.DARKTURQUOISE, new CornerRadii(20), null)));
        getContent().getChildren().addAll(first, posSprite, second);

        // to check T type
        setColorBorder();
    }

    @Override
    public CodeBlockInput<?> copy() {
        TouchingObjCodeBlockInput copy = new TouchingObjCodeBlockInput(getMain());
        copy.posSprite.getTextField().setText(posSprite.getTextField().getText());
        return copy;
    }

    public Type getType() {
        return Type.Boolean;
    }

    public Boolean getValue(SmeenContext context) {
        if (getCodeArea() == null)
            return false;

        SpriteObject sprite = getCodeArea().getSprite();
        if (sprite == null || Objects.equals(getMain().draggingProperty().get(), sprite))
            return false;

        int order = posSprite.getValue(context).intValue();
        if (order >= 1 && order <= getMain().getSpriteEditor().getSpriteCount()) {
            SpriteObject sprite2 = getMain().getSpriteEditor().getSpriteOption(order).getSprite();
            if (Objects.equals(getMain().draggingProperty().get(), sprite2))
                return false;
            Bounds a = sprite.localToScene(sprite.getBoundsInLocal());
            Bounds b = sprite2.localToScene(sprite2.getBoundsInLocal());
            return a.intersects(b);
        }
        return false;
    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "TouchingObjCodeBlockInput");
        result.put("posSprite", posSprite.exportData());
        result.put("x", getLayoutX());
        result.put("y", getLayoutY());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        posSprite.importData((Map<String, Object>) data.get("posSprite"));
        relocate((double) data.get("x"), (double) data.get("y"));
    }
}
