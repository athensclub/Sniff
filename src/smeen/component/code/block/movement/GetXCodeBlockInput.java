package smeen.component.code.block.movement;

import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import smeen.component.SpriteObject;
import smeen.component.code.CodeBlockInput;
import smeen.global.Fonts;
import smeen.global.SmeenConstants.Type;
import smeen.logic.Executable;
import smeen.logic.SmeenContext;
import smeen.views.MainView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GetXCodeBlockInput extends CodeBlockInput<Double> {
    public GetXCodeBlockInput(MainView main) {
        super(main);
        Label first = new Label("ค่า X");
        first.setTextFill(Color.WHITE);
        first.setFont(Fonts.SMALL_REGULAR_FONT);


        getContent().setBackground(new Background(new BackgroundFill(Color.DODGERBLUE, new CornerRadii(20), null)));
        getContent().getChildren().addAll(first);

        // to check T type
        setColorBorder();
    }

    @Override
    public CodeBlockInput<?> copy() {
        GetXCodeBlockInput copy = new GetXCodeBlockInput(getMain());
        return copy;
    }

    public Type getType() {
        return Type.Double;
    }

    @Override
    public Double getValue(SmeenContext context) {
        if (getCodeArea() == null)
            return 0.0;

        SpriteObject sprite = getCodeArea().getSprite();
        if (sprite == null)
            return 0.0;

        if (Objects.equals(getMain().draggingProperty().get(), sprite))
            return sprite.getOldPos().getX(); // use the old x position when the sprite is being dragged.

        return sprite.getLayoutX();
    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "GetXCodeBlockInput");
        result.put("x", getLayoutX());
        result.put("y", getLayoutY());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        relocate((double) data.get("x"), (double) data.get("y"));
    }
}
