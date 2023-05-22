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

public class TrueCodeBlockInput extends CodeBlockInput<Boolean> {

    public TrueCodeBlockInput(MainView main) {
        super(main);
        Label first = new Label("จริง");
        first.setTextFill(Color.WHITE);
        first.setFont(Fonts.SMALL_REGULAR_FONT);

        getContent().setBackground(new Background(new BackgroundFill(Color.ORANGE, new CornerRadii(20), null)));
        getContent().getChildren().addAll(first);

        // to check T type
        setColorBorder();
    }

    @Override
    public CodeBlockInput<?> copy() {
        TrueCodeBlockInput copy = new TrueCodeBlockInput(getMain());
        return copy;
    }

    public Type getType() {
        return Type.Boolean;
    }

    @Override
    public Boolean getValue(SmeenContext context) {
        return true;
    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "TrueCodeBlockInput");
        result.put("x", getLayoutX());
        result.put("y", getLayoutY());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        relocate((double) data.get("x"), (double) data.get("y"));
    }
}
