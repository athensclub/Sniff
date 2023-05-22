package smeen.component.code.block.control;

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
import smeen.logic.CodeExecutionHandler;
import smeen.logic.Executable;
import smeen.logic.SmeenContext;
import smeen.views.MainView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class WhileCodeBlock extends CodeBlock {
    private CodeBlockInputSlot<Boolean> conditionInput;

    private CodeBlockList toRun;

    public WhileCodeBlock(MainView main) {
        super(main);

        VBox root = new VBox();

        HBox condition = new HBox();
        condition.setAlignment(Pos.CENTER);
        condition.setSpacing(10);
        condition.setPadding(new Insets(10, 10, 5, 5));
        condition.setBackground(Background.fill(Color.ORANGE));

        Label first = new Label("วนซ้ำจนกระทั่ง");
        first.setTextFill(Color.WHITE);
        first.setFont(Fonts.SMALL_REGULAR_FONT);

        conditionInput = new CodeBlockInputSlot<>(main, SmeenConstants.Type.Boolean);

        condition.getChildren().addAll(first, conditionInput);

        toRun = new CodeBlockList(main);
        toRun.setMinSize(64, 48);
        toRun.setPadding(new Insets(2));
        toRun.setBorder(SmeenConstants.INNER_CODE_BLOCK_LIST_ORANGE_BORDER);

        root.getChildren().addAll(condition, toRun);

        getContent().setBackground(null);
        getContent().setPadding(Insets.EMPTY);
        getContent().getChildren().addAll(root);
    }

    @Override
    public CodeBlock copy() {
        WhileCodeBlock copy = new WhileCodeBlock(getMain());
        copy.conditionInput.getTextField().setText(conditionInput.getTextField().getText());
        return copy;
    }

    @Override
    public Executable.Result execute(SmeenContext context) {
        while (!conditionInput.getValue(context)) {
            Executable.Result result = toRun.execute(context);

            if (result.shouldStop() || Thread.currentThread().isInterrupted())
                return new Result(false, true);
            if (result.shouldBreak())
                break; // stop passing the shouldBreak chain bubbling up here.
            try {
                Thread.sleep(SmeenConstants.EXECUTION_DELAY);
            } catch (InterruptedException e) {
                return new Result(false, true);
            }
        }
        return new Executable.Result(false, false);
    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "WhileCodeBlock");
        result.put("condition", conditionInput.exportData());
        result.put("toRun", toRun.exportData());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        conditionInput.importData((Map<String, Object>) data.get("condition"));
        toRun.importData((Map<String, Object>) data.get("toRun"));
    }
}
