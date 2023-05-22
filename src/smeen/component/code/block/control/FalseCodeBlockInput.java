package smeen.component.code.block.control;

import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import smeen.component.code.CodeBlockInput;
import smeen.global.Fonts;
import smeen.global.SmeenConstants.Type;
import smeen.logic.SmeenContext;
import smeen.views.MainView;

import java.util.HashMap;
import java.util.Map;

public class FalseCodeBlockInput extends CodeBlockInput<Boolean> {
    public FalseCodeBlockInput(MainView main) {
        super(main);
        Label first = new Label("เท็จ");
        first.setTextFill(Color.WHITE);
        first.setFont(Fonts.SMALL_REGULAR_FONT);


        getContent().setBackground(new Background(new BackgroundFill(Color.ORANGE, new CornerRadii(20), null)));
        getContent().getChildren().addAll(first);

        // to check T type
        setColorBorder();
    }

    @Override
    public CodeBlockInput<?> copy() {
        FalseCodeBlockInput copy = new FalseCodeBlockInput(getMain());
        return copy;
    }

    public Type getType() {
        return Type.Boolean;
    }

    @Override
    public Boolean getValue(SmeenContext context) {
        return false;
    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "FalseCodeBlockInput");
        result.put("x", getLayoutX());
        result.put("y", getLayoutY());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        relocate((double) data.get("x"), (double) data.get("y"));
    }
}
