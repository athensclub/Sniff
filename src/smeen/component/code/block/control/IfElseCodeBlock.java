package smeen.component.code.block.control;

import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import smeen.component.code.CodeBlock;
import smeen.component.code.CodeBlockInputSlot;
import smeen.component.code.CodeBlockList;
import smeen.global.Fonts;
import smeen.global.SmeenConstants;
import smeen.global.SmeenConstants.Type;
import smeen.logic.Executable;
import smeen.logic.SmeenContext;
import smeen.views.MainView;

public class IfElseCodeBlock extends CodeBlock {
    private CodeBlockInputSlot<Boolean> conditionInput;

    private CodeBlockList caseTrue;

    private CodeBlockList caseFalse;

    public IfElseCodeBlock(MainView main) {
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

        conditionInput = new CodeBlockInputSlot<>(main, Type.Boolean);

        Label second = new Label("แล้ว");
        second.setTextFill(Color.WHITE);
        second.setFont(Fonts.SMALL_REGULAR_FONT);

        condition.getChildren().addAll(first, conditionInput, second);

        caseTrue = new CodeBlockList(main);
        caseTrue.setMinSize(64, 24);
        caseTrue.setPadding(new Insets(2));
        caseTrue.setBorder(new Border(new BorderStroke(Color.ORANGE, Color.ORANGE, Color.ORANGE, Color.ORANGE, BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 0, 24), Insets.EMPTY)));

        Label third = new Label("นอกจากนั้น");
        third.setTextFill(Color.WHITE);
        third.setFont(Fonts.SMALL_REGULAR_FONT);
        HBox thirdWrap = new HBox(third);
        thirdWrap.setPadding(new Insets(10, 10, 5, 5));
        thirdWrap.setBackground(Background.fill(Color.ORANGE));
        thirdWrap.setAlignment(Pos.CENTER);

        caseFalse = new CodeBlockList(main);
        caseFalse.setMinSize(64, 48);
        caseFalse.setPadding(new Insets(2));
        caseFalse.setBorder(SmeenConstants.INNER_CODE_BLOCK_LIST_ORANGE_BORDER);

        root.getChildren().addAll(condition, caseTrue, thirdWrap, caseFalse);

        getContent().setBackground(null);
        getContent().setPadding(Insets.EMPTY);
        getContent().getChildren().addAll(root);
    }

    @Override
    public CodeBlock copy() {
        IfElseCodeBlock copy = new IfElseCodeBlock(getMain());
        copy.conditionInput.getTextField().setText(conditionInput.getTextField().getText());
        return copy;
    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "IfElseCodeBlock");
        result.put("condition", conditionInput.exportData());
        result.put("caseTrue", caseTrue.exportData());
        result.put("caseFalse", caseFalse.exportData());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        conditionInput.importData((Map<String, Object>) data.get("condition"));
        caseTrue.importData((Map<String, Object>) data.get("caseTrue"));
        caseFalse.importData((Map<String, Object>) data.get("caseFalse"));
    }

    @Override
    public Executable.Result execute(SmeenContext context) {
        if (conditionInput.getValue(context))
            return caseTrue.execute(context);
        else
            return caseFalse.execute(context);
    }
}
