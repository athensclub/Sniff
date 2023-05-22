package smeen.component.code.block.operator.inputDouble;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

public class RandomNumberCodeBlockInput extends CodeBlockInput<Double>{

	private CodeBlockInputSlot<Double> slot1 , slot2;
	public RandomNumberCodeBlockInput(MainView main) {
		super(main);
		slot1 = new CodeBlockInputSlot<>(main,Type.Double);
		slot2 = new CodeBlockInputSlot<>(main,Type.Double);
		
		Label str1 = new Label("สุ่มเลขตั้งแต่");
        str1.setTextFill(Color.WHITE);
        str1.setFont(Fonts.SMALL_REGULAR_FONT);
        Label str2 = new Label("ถึง");
        str2.setTextFill(Color.WHITE);
        str2.setFont(Fonts.SMALL_REGULAR_FONT);
        
        getContent().setBackground(new Background(new BackgroundFill(Color.LIMEGREEN, new CornerRadii(20), null)));
        getContent().getChildren().addAll(str1 , slot1 , str2 , slot2);
        
        // to check T type
        setColorBorder(); 
	}
	
	@Override
    public CodeBlockInput<?> copy() {
    	RandomNumberCodeBlockInput copy = new RandomNumberCodeBlockInput(getMain());
		copy.slot1.getTextField().setText(slot1.getTextField().getText());
    	copy.slot2.getTextField().setText(slot2.getTextField().getText());
        return copy;
    }

	@Override
	public Double getValue(SmeenContext context) {
		double num1 = slot1.getValue(context);
		double num2 = slot2.getValue(context);
		if (num1 > num2) {
			// swap if num1 > num2 , idk how to do a better swap method ;(
			double temp = num1;
			num1 = num2;
			num2 = temp;
		}
		Random ran = new Random();
		// case of random int
		if (Math.floor(num1) == num1 && Math.floor(num2) == num2) {
			return num1 + (double)ran.nextInt((int)(num2 - num1 + 1));
		}
		return  num1 + ran.nextDouble(num2 - num1 + 1);
	}

	@Override
	public Type getType() {
		return Type.Double;
	}

	@Override
	public Map<String, Object> exportData() {
		Map<String, Object> result = new HashMap<>();
		result.put("type", "RandomNumberCodeBlockInput");
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
