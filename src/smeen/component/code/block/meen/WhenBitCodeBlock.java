package smeen.component.code.block.meen;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import smeen.component.code.CodeBlock;
import smeen.global.Fonts;
import smeen.views.MainView;

public class WhenBitCodeBlock extends CodeBlock {
	
	public WhenBitCodeBlock(MainView main){
        super(main);
        Label first = new Label("เมื่อโดนบิด");
        first.setTextFill(Color.WHITE);
        first.setFont(Fonts.SMALL_REGULAR_FONT);
        
        getContent().setBackground(Background.fill(Color.LIGHTSKYBLUE));
        
        getContent().getChildren().addAll(first);
    }

    @Override
    public boolean mustBeFirstBlock() {
        return true;
    }

    @Override
    public CodeBlock copy() {
    	WhenBitCodeBlock copy = new WhenBitCodeBlock(getMain());
        return copy;
    }
    
    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "WhenBitCodeBlock");
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
    	
    }
}
