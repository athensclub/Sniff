package smeen.component.code.block.event;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import smeen.component.code.CodeBlock;
import smeen.global.Fonts;
import smeen.logic.SmeenContext;
import smeen.views.MainView;

public class WhenClickedCodeBlock extends CodeBlock {

    public WhenClickedCodeBlock(MainView main){
        super(main);
        Label first = new Label("เมื่อถูกกด");
        first.setTextFill(Color.WHITE);
        first.setFont(Fonts.SMALL_REGULAR_FONT);
        
        
        getContent().setBackground(Background.fill(Color.GOLD));
        
        getContent().getChildren().addAll(first);
    }

    @Override
    public boolean mustBeFirstBlock() {
        return true;
    }

    @Override
    public CodeBlock copy() {
    	WhenClickedCodeBlock copy = new WhenClickedCodeBlock(getMain());
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
        result.put("type", "WhenClickedCodeBlock");
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
    	
    }
}
