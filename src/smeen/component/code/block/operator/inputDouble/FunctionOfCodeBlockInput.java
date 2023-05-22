package smeen.component.code.block.operator.inputDouble;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import smeen.component.code.CodeBlockInput;
import smeen.component.code.CodeBlockInputSlot;
import smeen.global.Fonts;
import smeen.global.SmeenConstants.KeyPress;
import smeen.global.SmeenConstants.MathFunction;
import smeen.global.SmeenConstants.Type;
import smeen.logic.SmeenContext;
import smeen.views.MainView;

import java.util.HashMap;
import java.util.Map;

public class FunctionOfCodeBlockInput extends CodeBlockInput<Double> {
	private CodeBlockInputSlot<Double> slot1;
	private ComboBox<MathFunction> optionBox;
	public FunctionOfCodeBlockInput(MainView main) {
		super(main);
		slot1 = new CodeBlockInputSlot<>(main,Type.Double);
		
		optionBox = new ComboBox<>();
        optionBox.getItems().addAll(MathFunction.values());
        optionBox.setValue(optionBox.getItems().get(0));
        
        // set selectBox of optionBox size
        optionBox.setPrefHeight(25);
        optionBox.setCellFactory(param -> new ListCell<MathFunction>() {
            {
                // set the height of the cell
                setPrefHeight(25);
            }
            @Override
            protected void updateItem(MathFunction item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && item != null) {
                	setText(item.getName());
                } else {
                	setText(null);
                }
            }
        });
        optionBox.setVisibleRowCount(5);
        
        Label str = new Label("ของ");
        str.setFont(Fonts.SMALL_REGULAR_FONT);
        str.setTextFill(Color.WHITE);
        
        getContent().setBackground(new Background(new BackgroundFill(Color.LIMEGREEN, new CornerRadii(20), null)));
        getContent().getChildren().addAll(optionBox, str , slot1);
        
        // to check T type
        setColorBorder(); 
	}
	
	@Override
    public CodeBlockInput<?> copy() {
    	FunctionOfCodeBlockInput copy = new FunctionOfCodeBlockInput(getMain());
		copy.slot1.getTextField().setText(slot1.getTextField().getText());
		copy.optionBox.setValue(optionBox.getValue());
        return copy;
    }

	@Override
	public Double getValue(SmeenContext context) {
		return  optionBox.getValue().calculation(slot1.getValue(context));
	}

	@Override
	public Type getType() {
		return Type.Double;
	}

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "FunctionOfCodeBlockInput");
        result.put("slot1", slot1.exportData());
        result.put("option", optionBox.getValue());
        result.put("x", getLayoutX());
        result.put("y", getLayoutY());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        slot1.importData((Map<String, Object>) data.get("slot1"));
        optionBox.setValue((MathFunction) data.get("option"));
        relocate((double) data.get("x"), (double) data.get("y"));
    }
}
