package smeen.component.code.block.control;

import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import smeen.component.code.CodeBlock;
import smeen.component.code.CodeBlockInputSlot;
import smeen.component.code.CodeBlockList;
import smeen.global.Fonts;
import smeen.global.SmeenConstants;
import smeen.logic.SmeenContext;
import smeen.views.MainView;

public class WaitUntilCodeBlock extends CodeBlock {
    private CodeBlockInputSlot<Boolean> conditionInput;

    public WaitUntilCodeBlock(MainView main) {
        super(main);

        VBox root = new VBox();

        HBox condition = new HBox();
        condition.setAlignment(Pos.CENTER);
        condition.setSpacing(10);
        condition.setPadding(new Insets(10, 10, 5, 5));
        condition.setBackground(Background.fill(Color.ORANGE));

        Label first = new Label("รอจนกระทั่ง");
        first.setTextFill(Color.WHITE);
        first.setFont(Fonts.SMALL_REGULAR_FONT);

        conditionInput = new CodeBlockInputSlot<>(main, SmeenConstants.Type.Boolean);

        condition.getChildren().addAll(first, conditionInput);

        root.getChildren().addAll(condition);

        getContent().setBackground(null);
        getContent().setPadding(Insets.EMPTY);
        getContent().getChildren().addAll(root);
    }

    @Override
    public CodeBlock copy() {
        WaitUntilCodeBlock copy = new WaitUntilCodeBlock(getMain());
        copy.conditionInput.getTextField().setText(conditionInput.getTextField().getText());
        return copy;
    }

    @Override
    public Result execute(SmeenContext context) {
        while (!conditionInput.getValue(context)) {
            try {
                Thread.sleep(SmeenConstants.EXECUTION_DELAY);
            } catch (InterruptedException e) {
                return new Result(false, true);
            }
        }
        return new Result(false, false);
    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "WaitUntilCodeBlock");
        result.put("condition", conditionInput.exportData());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        conditionInput.importData((Map<String, Object>) data.get("condition"));
    }
}
