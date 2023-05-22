package smeen.component.code.block.operator.inputBoolean;

import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import smeen.component.code.CodeBlockInput;
import smeen.component.code.CodeBlockInputSlot;
import smeen.global.Fonts;
import smeen.global.SmeenConstants.Type;
import smeen.logic.SmeenContext;
import smeen.views.MainView;

import java.util.HashMap;
import java.util.Map;

public class AndCodeBlockInput extends CodeBlockInput<Boolean> {
    private CodeBlockInputSlot<Boolean> slot1, slot2;

    public AndCodeBlockInput(MainView main) {
        super(main);
        slot1 = new CodeBlockInputSlot<>(main, Type.Boolean);
        slot2 = new CodeBlockInputSlot<>(main, Type.Boolean);

        Label op = new Label("และ");
        op.setTextFill(Color.WHITE);
        op.setFont(Fonts.SMALL_REGULAR_FONT);

        getContent().setBackground(new Background(new BackgroundFill(Color.LIMEGREEN, new CornerRadii(20), null)));
        getContent().getChildren().addAll(slot1, op, slot2);

        // to check T type
        setColorBorder();
    }

    @Override
    public CodeBlockInput<?> copy() {
        AndCodeBlockInput copy = new AndCodeBlockInput(getMain());
        copy.slot1.getTextField().setText(slot1.getTextField().getText());
        copy.slot2.getTextField().setText(slot2.getTextField().getText());
        return copy;
    }

    @Override
    public Boolean getValue(SmeenContext context) {
        return slot1.getValue(context) && slot2.getValue(context);
    }

    @Override
    public Type getType() {
        return Type.Boolean;
    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "AndCodeBlockInput");
        result.put("slot1", slot1.exportData());
        result.put("slot2", slot2.exportData());
        result.put("x", getLayoutX());
        result.put("y", getLayoutY());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        slot1.importData((Map<String, Object>) data.get("slot1"));
        slot2.importData((Map<String, Object>) data.get("slot2"));
        relocate((double) data.get("x"), (double) data.get("y"));
    }
}