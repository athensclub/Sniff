package smeen.component.code.block.sensing;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.util.converter.IntegerStringConverter;
import smeen.component.code.CodeBlock;
import smeen.component.code.CodeBlockInput;
import smeen.global.Fonts;
import smeen.global.SmeenConstants;
import smeen.global.SmeenConstants.KeyPress;
import smeen.global.SmeenConstants.Type;
import smeen.logic.SmeenContext;
import smeen.views.MainView;

public class KeyPressedCodeBlockInput extends CodeBlockInput<Boolean> {

	private ComboBox<KeyPress> optionBox;
    public KeyPressedCodeBlockInput(MainView main){
        super(main);
        Label first = new Label("กดปุ่ม");
        first.setTextFill(Color.WHITE);
        first.setFont(Fonts.SMALL_REGULAR_FONT);
        
        optionBox = new ComboBox<>();
        List<KeyPress> keyList = Arrays.asList(KeyPress.values());
        optionBox.setItems(FXCollections.observableArrayList(keyList));
        optionBox.setValue(optionBox.getItems().get(0));
        
        // set selectBox of optionBox size
        optionBox.setPrefHeight(30);
        optionBox.setCellFactory(param -> new ListCell<KeyPress>() {
            {
                // set the height of the cell
                setPrefHeight(30);
            }

            @Override
            protected void updateItem(KeyPress item, boolean empty) {
                super.updateItem(item, empty);
               	if (!empty && item != null) {
               		setText(item.toString());
               	} else {
               		setText(null);
               	}
            }
        });

        optionBox.setVisibleRowCount(5);
        
        Label second = new Label("?");
        second.setTextFill(Color.WHITE);
        second.setFont(Fonts.SMALL_REGULAR_FONT);
        
        getContent().setBackground(new Background(new BackgroundFill(Color.DARKTURQUOISE, new CornerRadii(20), null)));
        getContent().getChildren().addAll(first, optionBox, second);
        
        // to check T type
        setColorBorder(); 
    }

    @Override
    public CodeBlockInput<?> copy() {
    	KeyPressedCodeBlockInput copy = new KeyPressedCodeBlockInput(getMain());
    	copy.optionBox.setValue(optionBox.getValue());
        return copy;
    }
    
    public Type getType() {
    	return Type.Boolean;
    }
    
    public Boolean getValue(SmeenContext context) {
        return context.isKeyActive(optionBox.getValue());
    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "KeyPressedCodeBlockInput");
        result.put("option", optionBox.getValue());
        result.put("x", getLayoutX());
        result.put("y", getLayoutY());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        optionBox.setValue((KeyPress) data.get("option"));
        relocate((double) data.get("x"), (double) data.get("y"));
    }
}
