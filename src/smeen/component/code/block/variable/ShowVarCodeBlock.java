package smeen.component.code.block.variable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import smeen.component.code.CodeBlock;
import smeen.component.code.CodeSelector;
import smeen.global.Fonts;
import smeen.logic.SmeenContext;
import smeen.views.MainView;

public class ShowVarCodeBlock extends CodeBlock {
	private CodeSelector codeSelector;
	private ComboBox<String> optionBox;
	public ShowVarCodeBlock(MainView main, CodeSelector codeSelector) {
		super(main);
		this.codeSelector = codeSelector;
		Label text = new Label("แสดงตัวแปร");
		text.setFont(Fonts.SMALL_REGULAR_FONT);
        text.setTextFill(Color.WHITE);
		optionBox = new ComboBox<>();
		optionBox.itemsProperty().bind(codeSelector.createdVarNameProperty());
        //optionBox.setValue(optionBox.getItems().get(0));
        
        // set selectBox of optionBox size
        optionBox.setPrefHeight(25);
        optionBox.setCellFactory(param -> new ListCell<String>() {
            {
                // set the height of the cell
                setPrefHeight(25);
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && item != null) {
                	setText(item);
                } else {
                	setText(null);
                }
            }
        });
        optionBox.setVisibleRowCount(5);
        
        getContent().getChildren().addAll(text, optionBox);
        getContent().setBackground(Background.fill(Color.PALEVIOLETRED));
	}

	@Override
	public CodeBlock copy() {
		ShowVarCodeBlock copy = new ShowVarCodeBlock(getMain(),getMain().getCodeSelector());
		copy.optionBox.setValue(optionBox.getValue());
		return copy;
	}
	
	public void updateOption(ObservableList<String> newOptionList) {
		optionBox.setItems(newOptionList);
		if (newOptionList.size() == 0) return;
		optionBox.setValue(newOptionList.get(0));
	}

	@Override
	public Result execute(SmeenContext context) {
		String name = optionBox.getValue();
		Platform.runLater(() -> getMain().getStageArea().shownVariableProperty().add(name));
		return new Result(false,false);
	}

	@Override
	public Map<String, Object> exportData() {
		Map<String, Object> result = new HashMap<>();
		result.put("type","ShowVarCodeBlock");
		result.put("option",optionBox.getValue());
		return result;
	}

	@Override
	public void importData(Map<String, Object> data) {
		optionBox.setValue((String) data.get("option"));
	}
}
