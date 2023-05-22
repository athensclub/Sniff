package smeen.component.code.block.control;

import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import smeen.component.code.CodeBlock;
import smeen.component.code.CodeBlockInputSlot;
import smeen.component.code.CodeBlockList;
import smeen.global.Fonts;
import smeen.global.SmeenConstants;
import smeen.logic.Executable;
import smeen.logic.SmeenContext;
import smeen.util.SmeenLayouts;
import smeen.views.MainView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class IfCodeBlock extends CodeBlock {

    private CodeBlockInputSlot<Boolean> conditionInput;

    private CodeBlockList caseTrue;

    public IfCodeBlock(MainView main) {
        super(main);

        VBox root = new VBox();

        HBox condition = new HBox();
        condition.setAlignment(Pos.CENTER);
        condition.setSpacing(10);
        condition.setPadding(new Insets(10, 10, 5, 5));
        condition.setBackground(Background.fill(Color.ORANGE));

        Label first = new Label("ถ้า");
        first.setTextFill(Color.WHITE);
        first.setFont(Fonts.SMALL_REGULAR_FONT);

        conditionInput = new CodeBlockInputSlot<>(main, SmeenConstants.Type.Boolean);

        Label second = new Label("แล้ว");
        second.setTextFill(Color.WHITE);
        second.setFont(Fonts.SMALL_REGULAR_FONT);

        condition.getChildren().addAll(first, conditionInput, second);

        caseTrue = new CodeBlockList(main);
        caseTrue.setMinSize(64, 48);
        caseTrue.setPadding(new Insets(2));
        caseTrue.setBorder(SmeenConstants.INNER_CODE_BLOCK_LIST_ORANGE_BORDER);

        root.getChildren().addAll(condition, caseTrue);

        getContent().setBackground(null);
        getContent().setPadding(Insets.EMPTY);
        getContent().getChildren().addAll(root);
    }

    @Override
    public CodeBlock copy() {
        IfCodeBlock copy = new IfCodeBlock(getMain());
        copy.conditionInput.getTextField().setText(conditionInput.getTextField().getText());
        return copy;
    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "IfCodeBlock");
        result.put("condition", conditionInput.exportData());
        result.put("caseTrue", caseTrue.exportData());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        conditionInput.importData((Map<String, Object>) data.get("condition"));
        caseTrue.importData((Map<String, Object>) data.get("caseTrue"));
    }

    @Override
    public Executable.Result execute(SmeenContext context) {
        if (conditionInput.getValue(context))
            return caseTrue.execute(context);
        return new Executable.Result(false, false);
    }
}
