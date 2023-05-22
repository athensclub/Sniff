package smeen.component.code.block.event;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import smeen.component.code.CodeBlock;
import smeen.component.code.CodeBlockInputSlot;
import smeen.global.Fonts;
import smeen.global.SmeenConstants.Type;
import smeen.logic.SmeenContext;
import smeen.views.MainView;

public class BroadcastCodeBlock extends CodeBlock {

    private CodeBlockInputSlot<String> broadcast;

    public BroadcastCodeBlock(MainView main) {
        super(main);
        Label first = new Label("ประกาศคำสั่ง");
        first.setTextFill(Color.WHITE);
        first.setFont(Fonts.SMALL_REGULAR_FONT);

        broadcast = new CodeBlockInputSlot<>(main, Type.String);

        getContent().setBackground(Background.fill(Color.GOLD));

        getContent().getChildren().addAll(first, broadcast);
    }

    @Override
    public CodeBlock copy() {
        BroadcastCodeBlock copy = new BroadcastCodeBlock(getMain());
        copy.broadcast.getTextField().setText(broadcast.getTextField().getText());
        return copy;
    }

    @Override
    public Result execute(SmeenContext context) {
        String name = broadcast.getValue(context);
        getMain().getCodeExecutionHandler().broadcast(name);
        return new Result(false, false);
    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "BroadcastCodeBlock");
        result.put("broadcast", broadcast.exportData());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        broadcast.importData((Map<String, Object>) data.get("broadcast"));
    }
}
