package smeen.component.code.block.control;

import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import smeen.component.code.CodeBlock;
import smeen.global.Fonts;
import smeen.logic.Executable;
import smeen.logic.SmeenContext;
import smeen.views.MainView;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BreakCodeBlock extends CodeBlock {
    public BreakCodeBlock(MainView main) {
        super(main);
        Label first = new Label("หยุด");
        first.setTextFill(Color.WHITE);
        first.setFont(Fonts.SMALL_REGULAR_FONT);

        getContent().setBackground(Background.fill(Color.ORANGE));

        getContent().getChildren().addAll(first);
    }

    @Override
    public CodeBlock copy() {
        BreakCodeBlock copy = new BreakCodeBlock(getMain());
        return copy;
    }

    @Override
    public Result execute(SmeenContext context) {
        return new Executable.Result(true, false);
    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "BreakCodeBlock");
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        // no op;
    }
}
