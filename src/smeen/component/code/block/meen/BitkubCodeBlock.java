package smeen.component.code.block.meen;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import smeen.component.code.CodeBlock;
import smeen.global.Fonts;
import smeen.views.MainView;

public class BitkubCodeBlock extends CodeBlock{
	
	public BitkubCodeBlock(MainView main) {
		super(main);
		Label first = new Label("บิดครับ");
        first.setTextFill(Color.WHITE);
        first.setFont(Fonts.SMALL_REGULAR_FONT);

        getContent().setBackground(Background.fill(Color.LIGHTSKYBLUE));

        getContent().getChildren().addAll(first);
	}
	
	public CodeBlock copy() {
    	BitkubCodeBlock copy = new BitkubCodeBlock(getMain());
        return copy;
    }
	
	@Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "BitkubCodeBlock");
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
    	
    }
}
