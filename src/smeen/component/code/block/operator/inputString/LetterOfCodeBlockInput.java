package smeen.component.code.block.operator.inputString;

import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import smeen.component.code.CodeBlockInput;
import smeen.component.code.CodeBlockInputSlot;
import smeen.global.Fonts;
import smeen.global.SmeenConstants.Type;
import smeen.logic.SmeenContext;
import smeen.views.MainView;

import java.util.HashMap;
import java.util.Map;

public class LetterOfCodeBlockInput extends CodeBlockInput<String> {
	private CodeBlockInputSlot<Double> slot1;
	private CodeBlockInputSlot<String> slot2;
	public LetterOfCodeBlockInput(MainView main) {
		super(main);
		slot1 = new CodeBlockInputSlot<>(main,Type.Double);
		slot2 = new CodeBlockInputSlot<>(main,Type.String);
		
		Label str1 = new Label("ตัวอักษรที่");
        str1.setTextFill(Color.WHITE);
        str1.setFont(Fonts.SMALL_REGULAR_FONT);
        Label str2 = new Label("ใน");
        str2.setTextFill(Color.WHITE);
        str2.setFont(Fonts.SMALL_REGULAR_FONT);
        
        getContent().setBackground(new Background(new BackgroundFill(Color.LIMEGREEN, new CornerRadii(20), null)));
        getContent().getChildren().addAll(str1, slot1 , str2 , slot2);
        
        // to check T type
        setColorBorder(); 
	}
	
	@Override
    public CodeBlockInput<?> copy() {
    	LetterOfCodeBlockInput copy = new LetterOfCodeBlockInput(getMain());
		copy.slot1.getTextField().setText(slot1.getTextField().getText());
    	copy.slot2.getTextField().setText(slot2.getTextField().getText());
        return copy;
    }

	@Override
	public String getValue(SmeenContext context) {
		return String.valueOf(slot2.getValue(context).charAt((int) Math.round(slot1.getValue(context))));
	}

	@Override
	public Type getType() {
		return Type.String;
	}

	@Override
	public Map<String, Object> exportData() {
		Map<String, Object> result = new HashMap<>();
		result.put("type", "LetterOfCodeBlockInput");
		result.put("slot1", slot1.exportData());
		result.put("slot2", slot2.exportData());
		result.put("x", getLayoutX());
		result.put("y", getLayoutY());
		return result;
	}

	@Override
	public void importData(Map<String, Object> data) {
		slot1.importData((Map<String, Object>) data.get("slot1"));
		slot2.importData((Map<String, Object>) data.get("slot2"));
		relocate((double) data.get("x"), (double) data.get("y"));
	}
}
