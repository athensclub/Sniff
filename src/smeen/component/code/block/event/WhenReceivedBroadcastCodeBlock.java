package smeen.component.code.block.event;

import java.util.HashMap;
import java.util.Map;

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

public class WhenReceivedBroadcastCodeBlock extends CodeBlock {

    private CodeBlockInputSlot<String> broadcast;
    public WhenReceivedBroadcastCodeBlock(MainView main){
        super(main);
        Label first = new Label("เมื่อได้รับคำสั่ง");
        first.setTextFill(Color.WHITE);
        first.setFont(Fonts.SMALL_REGULAR_FONT);

        broadcast = new CodeBlockInputSlot<>(main, Type.String);
        
        getContent().setBackground(Background.fill(Color.GOLD));
        
        getContent().getChildren().addAll(first, broadcast);
    }

    @Override
    public boolean mustBeFirstBlock() {
        return true;
    }

    public String getBroadcastName(SmeenContext context){
        return broadcast.getValue(context);
    }

    @Override
    public CodeBlock copy() {
    	WhenReceivedBroadcastCodeBlock copy = new WhenReceivedBroadcastCodeBlock(getMain());
        copy.broadcast.getTextField().setText(broadcast.getTextField().getText());
        return copy;
    }

    @Override
    public Result execute(SmeenContext context) {
        // no op
        return new Result(false,false);
    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "WhenReceivedBroadcastCodeBlock");
        result.put("broadcast", broadcast.exportData());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        broadcast.importData((Map<String, Object>) data.get("broadcast"));
    }
}
